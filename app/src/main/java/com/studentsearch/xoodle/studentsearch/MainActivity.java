package com.studentsearch.xoodle.studentsearch;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.View.OnKeyListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import com.google.gson.Gson;
import com.studentsearch.xoodle.studentsearch.database.DbHelper;
import com.studentsearch.xoodle.studentsearch.utils.ConstantUtils;
import com.studentsearch.xoodle.studentsearch.utils.MappingUtils;

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
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

  public DbHelper dbHelper;
  private EditText mEditText;
  private Button mButton;
  private ProgressDialog mProgressDialog;
  public static Integer[] mThumbIds = {R.drawable.iitk_image_2, R.drawable.iitk2,R.drawable.iitk_image_4,R.drawable.iitk_imgiiii, R.drawable.iitk_image_5, R.drawable.iitk_image_6};
  public ImageView iv;
  int i;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    iv = (ImageView) findViewById(R.id.image_fit);
    i = 0;
    t.start();

    ActionBar ab = getSupportActionBar();
    Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
    if (ab != null) {
      ab.setHomeAsUpIndicator(drawable);
      ab.setDisplayHomeAsUpEnabled(true);
    }
    long cnt = DatabaseUtils.queryNumEntries(DbHelper.getDbHelperInstance(getApplicationContext(), DbHelper.TABLE_NAME, 1).getReadableDatabase(), "students");
    if(cnt == 0) {
      refreshDatabase();
    }

    mEditText = (EditText) findViewById(R.id.edit_text);
    mEditText.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        final int DRAWABLE_RIGHT = 2;
        final int DRAWABLE_LEFT = 0;

        if(event.getAction() == MotionEvent.ACTION_UP) {
          if(event.getRawX() >= (mEditText.getRight() - mEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()-mEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
            mEditText.setText("");
            return true;
          }
          if(event.getRawX() <= (mEditText.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width()+(mEditText.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())))
          {
            Intent intent = SearchResultActivity.getNewIntent(MainActivity.this);
            passQueryFilterParams(intent);
            startActivity(intent);
            return true;
          }


        }
        return false;

      }
    });
    mEditText.setFocusableInTouchMode(true);
    mEditText.requestFocus();
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
  Thread t = new Thread() {
    @Override
    public void run() {
      try {
        while (!isInterrupted()) {
          Thread.sleep(7800);
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              iv.setImageResource(mThumbIds[i]);
              Animation fadeIn = new AlphaAnimation(0, 1);
              fadeIn.setInterpolator(new DecelerateInterpolator());
              fadeIn.setDuration(3000);

              iv.setAnimation(fadeIn);
              i++;
              if (i >= mThumbIds.length) {
                i = 0;
              }
            }
          });
        }
      } catch (InterruptedException e) {
      }
    }
  };

  private void setFilterSpinnerEntries() {
    SQLiteDatabase db = DbHelper.getDbHelperInstance(this, DbHelper.TABLE_NAME, 1).getReadableDatabase();
    ArrayList<String> listOfEntries;
    SpinnerAdapter spinnerAdapter;
    String tempEntry;
    MappingUtils mpu = new MappingUtils();

    Cursor cursor = db.rawQuery("SELECT DISTINCT " + DbHelper.COLUMN_HALL + " FROM " + DbHelper.TABLE_NAME, null);
    cursor.moveToFirst();
    listOfEntries = new ArrayList<>();
    listOfEntries.add("");
    for(int x=0;x<cursor.getCount();x++) {
      tempEntry = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_HALL));
      if(!tempEntry.equals(""))
        listOfEntries.add(tempEntry);
      cursor.moveToNext();
    }
    spinnerAdapter = new SpinnerAdapter(this, R.layout.support_simple_spinner_dropdown_item, listOfEntries, ConstantUtils.HALL);
    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    ((Spinner) findViewById(R.id.spinner_hall)).setAdapter(spinnerAdapter);
    cursor.close();

    listOfEntries = new ArrayList<>();
    listOfEntries.add("");
    for(String s : ConstantUtils.BLOOD_GROUP_LIST) {
      listOfEntries.add(s);
    }

    spinnerAdapter = new SpinnerAdapter(this,android.R.layout.simple_spinner_item, listOfEntries, ConstantUtils.BLOOD_GROUP);
    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    ((Spinner) findViewById(R.id.spinner_blood_group)).setAdapter(spinnerAdapter);

    cursor = db.rawQuery("SELECT DISTINCT " + DbHelper.COLUMN_DEPT + " FROM " + DbHelper.TABLE_NAME, null);
    cursor.moveToFirst();
    listOfEntries = new ArrayList<>();
    listOfEntries.add("");
    for(int x=0;x<cursor.getCount();x++) {
      tempEntry = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_DEPT));
      if(!tempEntry.equals(""))
        listOfEntries.add(tempEntry);
      cursor.moveToNext();
    }
    Collections.sort(listOfEntries);
    spinnerAdapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, listOfEntries, ConstantUtils.DEPT);
    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    ((Spinner) findViewById(R.id.spinner_dept)).setAdapter(spinnerAdapter);
    cursor.close();

    cursor = db.rawQuery("SELECT DISTINCT " + DbHelper.COLUMN_PROGRAMME + " FROM " + DbHelper.TABLE_NAME, null);
    cursor.moveToFirst();
    listOfEntries = new ArrayList<>();
    listOfEntries.add("");
    for(int x=0;x<cursor.getCount();x++) {
      tempEntry = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_PROGRAMME));
      if(!tempEntry.equals(""))
        listOfEntries.add(tempEntry);
      cursor.moveToNext();
    }
    Collections.sort(listOfEntries);
    spinnerAdapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, listOfEntries, ConstantUtils.PROGRAMME);
    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    ((Spinner) findViewById(R.id.spinner_programme)).setAdapter(spinnerAdapter);
    cursor.close();

    listOfEntries = new ArrayList<>();
    listOfEntries.add("");
    for(String s : ConstantUtils.GENDER_LIST) {
      listOfEntries.add(s);
    }
    spinnerAdapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, listOfEntries, ConstantUtils.GENDER);
    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    ((Spinner) findViewById(R.id.spinner_gender)).setAdapter(spinnerAdapter);

    cursor = db.rawQuery("SELECT DISTINCT " + DbHelper.COLUMN_YEAR + " FROM " + DbHelper.TABLE_NAME, null);
    cursor.moveToFirst();

    listOfEntries = new ArrayList<>();
    listOfEntries.add("");
    for(int x=0;x<cursor.getCount();x++) {
      tempEntry = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_YEAR));
      if(!tempEntry.equals("")) {
        if(mpu.getYearMap().get(tempEntry) != null)
          listOfEntries.add(tempEntry);
      }
      cursor.moveToNext();
    }
    listOfEntries.add("Others");
    spinnerAdapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, listOfEntries, ConstantUtils.YEAR);
    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    ((Spinner) findViewById(R.id.spinner_year)).setAdapter(spinnerAdapter);
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
    switch (item.getItemId()) {
      case R.id.download_images:
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            switch (which) {
              case DialogInterface.BUTTON_POSITIVE:
                new ImageDownloader().execute();
                break;

              case DialogInterface.BUTTON_NEGATIVE:
                //No button clicked
                break;
            }
          }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to download images now ?").setNegativeButton("No", dialogClickListener)
          .setPositiveButton("Yes", dialogClickListener).show();

        break;
      case R.id.update:
        // check for update
        refreshDatabase();
        break;
      case R.id.about_app:
        // info about app
        Intent intent = new Intent(this, AboutAppActivity.class);
        this.startActivity(intent);
        break;
      case R.id.reset_entries:
        //reset all entries
        Spinner sp1 = (Spinner) findViewById(R.id.spinner_dept);
        sp1.setSelection(0);
        Spinner sp2 = (Spinner) findViewById(R.id.spinner_hall);
        sp2.setSelection(0);
        Spinner sp3 = (Spinner) findViewById(R.id.spinner_year);
        sp3.setSelection(0);
        Spinner sp4 = (Spinner) findViewById(R.id.spinner_gender);
        sp4.setSelection(0);
        Spinner sp5 = (Spinner) findViewById(R.id.spinner_programme);
        sp5.setSelection(0);
        Spinner sp6 = (Spinner) findViewById(R.id.spinner_blood_group);
        sp6.setSelection(0);
        EditText editText =(EditText) findViewById(R.id.edit_text);
        editText.setText("");
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
    intent.putExtra(DbHelper.COLUMN_ROLL_NO, queryName);
    intent.putExtra(DbHelper.COLUMN_YEAR, getYearFilter());
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

  public String getYearFilter() {
    Spinner spinnerGender = (Spinner) findViewById(R.id.spinner_year);
    return spinnerGender.getSelectedItem().toString();
  }

  private void refreshDatabase() {
    new JsonTask().execute("https://search.pclub.in/api/students");
  }

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
      StudentData[] students = gson.fromJson(json[0], StudentData[].class);
      dbHelper = DbHelper.getDbHelperInstance(getApplicationContext(), "students", 1);
      dbHelper.insertStudents(students);
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      if (mProgressDialog.isShowing())
        mProgressDialog.dismiss();
      recreate();
      // calling this funtion to execute an dialox box on first launch only.
      imageDownloadAlertbox();
    }

    private void imageDownloadAlertbox() {
      DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
              new ImageDownloader().execute();
              break;

            case DialogInterface.BUTTON_NEGATIVE:
              //No button clicked
              break;
          }
        }
      };
      AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
      builder.setMessage("Do you want to download images now ?").setNegativeButton("No", dialogClickListener)
        .setPositiveButton("Yes", dialogClickListener).show();
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
      imageDownloaderDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Do in Background", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          dialog.dismiss();
        }
      });
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