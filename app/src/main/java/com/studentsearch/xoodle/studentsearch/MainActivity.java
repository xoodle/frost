package com.studentsearch.xoodle.studentsearch;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.studentsearch.xoodle.studentsearch.database.DbHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

  public DbHelper dbHelper;
  private EditText mEditText;
  private Button mButton;
  private ProgressDialog mProgressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    long cnt = DatabaseUtils.queryNumEntries(DbHelper.getDbHelperInstance(getApplicationContext(), DbHelper.TABLE_NAME, 1).getReadableDatabase(), "students");
    if(cnt == 0) {
      refreshDatabase();
    }
    mEditText = (EditText) findViewById(R.id.edit_text);
    mEditText.setOnKeyListener(new View.OnKeyListener()
    {
      public boolean onKey(View v, int keyCode, KeyEvent event)
      {
        if (event.getAction() == KeyEvent.ACTION_DOWN)
        {
          switch (keyCode)
          {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
              getSearchQuery();
              Intent intent = SearchResultActivity.getNewIntent(MainActivity.this);
              passQueryFilterParams(intent);
              startActivity(intent);
              return true;
            default:
              break;
          }
        }
        return false;
      }
    });

    mButton = (Button) findViewById(R.id.button_go);
    mButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = SearchResultActivity.getNewIntent(MainActivity.this);
        passQueryFilterParams(intent);
        startActivity(intent);
      }
    });
    setFilterSpinnerEntries();
  }

  private void setFilterSpinnerEntries() {
    SQLiteDatabase db = DbHelper.getDbHelperInstance(this, DbHelper.TABLE_NAME, 1).getReadableDatabase();
    Cursor cursor = db.rawQuery("SELECT DISTINCT " + DbHelper.COLUMN_HALL + " FROM " + DbHelper.TABLE_NAME, null);
    cursor.moveToFirst();
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    arrayAdapter.add("Hall");
    for(int x=0;x<cursor.getCount();x++) {
      arrayAdapter.add(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_HALL)));
      cursor.moveToNext();
    }
    ((Spinner) findViewById(R.id.spinner_hall)).setAdapter(arrayAdapter);
    cursor.close();

    cursor = db.rawQuery("SELECT DISTINCT " + DbHelper.COLUMN_BLOOD_GROUP + " FROM " + DbHelper.TABLE_NAME, null);
    cursor.moveToFirst();
    arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item);
    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    arrayAdapter.add("");
    for(int x=0;x<cursor.getCount();x++) {
      arrayAdapter.add(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_BLOOD_GROUP)));
      cursor.moveToNext();
    }
    ((Spinner) findViewById(R.id.spinner_blood_group)).setAdapter(arrayAdapter);
    cursor.close();

    cursor = db.rawQuery("SELECT DISTINCT " + DbHelper.COLUMN_DEPT + " FROM " + DbHelper.TABLE_NAME, null);
    cursor.moveToFirst();
    arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    arrayAdapter.add("");
    for(int x=0;x<cursor.getCount();x++) {
      arrayAdapter.add(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_DEPT)));
      cursor.moveToNext();
    }
    ((Spinner) findViewById(R.id.spinner_dept)).setAdapter(arrayAdapter);
    cursor.close();

    cursor = db.rawQuery("SELECT DISTINCT " + DbHelper.COLUMN_PROGRAMME + " FROM " + DbHelper.TABLE_NAME, null);
    cursor.moveToFirst();
    arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    arrayAdapter.add("");
    for(int x=0;x<cursor.getCount();x++) {
      arrayAdapter.add(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_PROGRAMME)));
      cursor.moveToNext();
    }
    ((Spinner) findViewById(R.id.spinner_programme)).setAdapter(arrayAdapter);
    cursor.close();

    cursor = db.rawQuery("SELECT DISTINCT " + DbHelper.COLUMN_GENDER + " FROM " + DbHelper.TABLE_NAME, null);
    cursor.moveToFirst();
    arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    arrayAdapter.add("");
    for(int x=0;x<cursor.getCount();x++) {
      arrayAdapter.add(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_GENDER)));
      cursor.moveToNext();
    }
    ((Spinner) findViewById(R.id.spinner_gender)).setAdapter(arrayAdapter);
    cursor.close();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_main_activity, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Take appropriate action for each action item click
    switch (item.getItemId()) {
      case R.id.download_images:
        new ImageDownloader().execute();
        break;
      case R.id.action_refresh_database:
        break;
      case R.id.menu_settings:
        // refresh
        break;
      case R.id.menu_main:
        // help action
        break;
      case R.id.menu_search:
        // check for updates action
        break;
      default:
    }
    return super.onOptionsItemSelected(item);
  }

  private void passQueryFilterParams(Intent intent) {
    String queryName = getSearchQuery();
    queryName = queryName.trim().replace(" ", "%");
    intent.putExtra(DbHelper.COLUMN_NAME, queryName);
    intent.putExtra(DbHelper.COLUMN_HALL, getHallFilter());
    intent.putExtra(DbHelper.COLUMN_BLOOD_GROUP, getBloodGroupFilter());
    intent.putExtra(DbHelper.COLUMN_DEPT, getDeptFilter());
    intent.putExtra(DbHelper.COLUMN_PROGRAMME, getProgrammeFilter());
    intent.putExtra(DbHelper.COLUMN_GENDER, getGenderFilter());
  }

  public String getSearchQuery() {
    String name = mEditText.getText().toString();
    return name;
  }

  public String getHallFilter() {
    Spinner spinnerHall = (Spinner) findViewById(R.id.spinner_hall);
    return spinnerHall.getSelectedItem().toString();
  }

  public String getBloodGroupFilter() {
    Spinner spinnerBloodGroup = (Spinner) findViewById(R.id.spinner_blood_group);
    return spinnerBloodGroup.getSelectedItem().toString();
  }

  public String getDeptFilter() {
    Spinner spinnerDept = (Spinner) findViewById(R.id.spinner_dept);
    return spinnerDept.getSelectedItem().toString();
  }

  public String getProgrammeFilter() {
    Spinner spinnerProgramme = (Spinner) findViewById(R.id.spinner_programme);
    return spinnerProgramme.getSelectedItem().toString();
  }

  public String getGenderFilter() {
    Spinner spinnerGender = (Spinner) findViewById(R.id.spinner_gender);
    return spinnerGender.getSelectedItem().toString();
  }

  private void refreshDatabase() {
    new JsonTask().execute("https://yashsriv.org/api");
  }

//  private void getStudentImages() {
//    new ImageDownloader(getApplicationContext()).execute();
//  }

  public class JsonTask extends AsyncTask<String, String, String> {
    protected void onPreExecute() {
      super.onPreExecute();
      mProgressDialog = new ProgressDialog(MainActivity.this);
      mProgressDialog.setMessage("Getting data from the net...\nMake sure you are connected to IITK network");
      mProgressDialog.setCancelable(false);
      mProgressDialog.show();
    }

    protected String doInBackground(String... params) {
      HttpURLConnection connection = null;
      BufferedReader reader = null;

      try {
        URL url = new URL(params[0]);
        connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        InputStream stream = connection.getInputStream();
        reader = new BufferedReader(new InputStreamReader(stream));

        StringBuffer buffer = new StringBuffer();
        String line = "";

        while ((line = reader.readLine()) != null) {
          buffer.append(line + "\n");
          Log.d("Response: ", "> " + line);   //here you will get the whole response
        }
        return buffer.toString();
      } catch (MalformedURLException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        if (connection != null) {
          connection.disconnect();
        }
        try {
          if (reader != null) {
            reader.close();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      return null;
    }

    @Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);
      if(result == null) {
        mProgressDialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Please connect to the internet and try again");
        AlertDialog dialog = builder.create();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
          @Override
          public void onCancel(DialogInterface dialog) {
            // or anything else appropriate
            finish();
          }
        });
        dialog.show();
      } else {
        mProgressDialog.setMessage("Preparing database...\nIt may take some time");
        new WriteDatabaseAsync().execute(result);
      }
    }
  }

  public class WriteDatabaseAsync extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... json) {
      Gson gson = new Gson();
      StudentData student;
      dbHelper = DbHelper.getDbHelperInstance(getApplicationContext(), "students", 1);
      Matcher m = Pattern.compile("\\{(.*?)\\}").matcher(json[0]);
      while (m.find()) {
        student = gson.fromJson("{" + m.group(1) + "}", StudentData.class);
        dbHelper.insertStudent(student);
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      if (mProgressDialog.isShowing())
        mProgressDialog.dismiss();
      recreate();
    }
  }

  public class ImageDownloader extends AsyncTask<Void, Integer, Void> {

    ProgressDialog imageDownloaderDialog = new ProgressDialog(MainActivity.this);

    @Override
    protected void onPreExecute() {
      imageDownloaderDialog.setTitle("Fetching Images...");
      imageDownloaderDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
      imageDownloaderDialog.setIndeterminate(false);
      imageDownloaderDialog.setCancelable(false);
      imageDownloaderDialog.setMax(100);
      imageDownloaderDialog.show();
      super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... arg0) {
      Bitmap mIcon;
      File directory = null;

      if (isExternalStorageWritable()) {
        directory = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "studentPics");
      } else {
        return null;
      }
      if (!directory.mkdirs()) {
        Log.e("imageDownloader", "Directory not created");
      }

      SQLiteDatabase db = DbHelper.getDbHelperInstance(getApplicationContext(), DbHelper.TABLE_NAME, 1).getReadableDatabase();
      Cursor cursor = db.rawQuery("SELECT ALL " + DbHelper.COLUMN_ROLL_NO + " FROM " + DbHelper.TABLE_NAME, null);
      cursor.moveToFirst();
      final int total = cursor.getCount();

      for (int x = 0; x < total; x++) {
        String rollno = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_ROLL_NO));
        try {
          InputStream in = new java.net.URL("http://oa.cc.iitk.ac.in/Oa/Jsp/Photo/" + rollno + "_0.jpg").openStream();
          mIcon = BitmapFactory.decodeStream(in);

          try {
            File imageFile = new File(directory, rollno + "_0"); // Create image file
            FileOutputStream out = new FileOutputStream(imageFile);
            mIcon.compress(
                    Bitmap.CompressFormat.JPEG,
                    100, out);
            out.flush();
            out.close();
          } catch (FileNotFoundException e) {
            Log.e("ImageDownloader", "doInBackground: File Not Found");
            cursor.moveToNext();
            continue;
          } catch (IOException e) {
            Log.e("ImageDownloader", "doInBackground: IO Exception");
            cursor.moveToNext();
            continue;
          }

        } catch (Exception e) {
          Log.e("ImageDownloader", "doInBackground: " + rollno + " Error" + e);
          cursor.moveToNext();
          continue;
        }

        publishProgress((int) ((x * 100) / total));
        cursor.moveToNext();

        if (isCancelled()) {
          Log.i("ImageDownloader", "getPics: cancelled");
          break;
        }

      }
      cursor.close();
      Log.i("ImageDownloader", "getPics: done");
      return null;
    }

    protected void onProgressUpdate(Integer... values) {
      imageDownloaderDialog.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Void result) {
      imageDownloaderDialog.dismiss();
      super.onPostExecute(result);
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
      String state = Environment.getExternalStorageState();
      return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
      String state = Environment.getExternalStorageState();
      return Environment.MEDIA_MOUNTED.equals(state) ||
              Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

  }


}