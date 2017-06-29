package com.studentsearch.xoodle.studentsearch;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.studentsearch.xoodle.studentsearch.database.DbHelper;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

  private EditText mEditText;
  private Button mButton;
  private ProgressDialog mProgressDialog;
  public DbHelper dbHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    long cnt = DatabaseUtils.queryNumEntries(DbHelper.getDbHelperInstance(getApplicationContext(), DbHelper.TABLE_NAME, 1).getReadableDatabase(), "students");
    if(cnt == 0) {
      refreshDatabase();
    }
    mEditText = (EditText) findViewById(R.id.edit_text);
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
    for(int x=0;x<cursor.getCount();x++) {
      arrayAdapter.add(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_HALL)));
      cursor.moveToNext();
    }
    arrayAdapter.add("");
    ((Spinner) findViewById(R.id.spinner_hall)).setAdapter(arrayAdapter);
    cursor.close();

    cursor = db.rawQuery("SELECT DISTINCT " + DbHelper.COLUMN_BLOOD_GROUP + " FROM " + DbHelper.TABLE_NAME, null);
    cursor.moveToFirst();
    arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    for(int x=0;x<cursor.getCount();x++) {
      arrayAdapter.add(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_BLOOD_GROUP)));
      cursor.moveToNext();
    }
    arrayAdapter.add("");
    ((Spinner) findViewById(R.id.spinner_blood_group)).setAdapter(arrayAdapter);
    cursor.close();

    cursor = db.rawQuery("SELECT DISTINCT " + DbHelper.COLUMN_DEPT + " FROM " + DbHelper.TABLE_NAME, null);
    cursor.moveToFirst();
    arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    for(int x=0;x<cursor.getCount();x++) {
      arrayAdapter.add(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_DEPT)));
      cursor.moveToNext();
    }
    arrayAdapter.add("");
    ((Spinner) findViewById(R.id.spinner_dept)).setAdapter(arrayAdapter);
    cursor.close();

    cursor = db.rawQuery("SELECT DISTINCT " + DbHelper.COLUMN_PROGRAMME + " FROM " + DbHelper.TABLE_NAME, null);
    cursor.moveToFirst();
    arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    for(int x=0;x<cursor.getCount();x++) {
      arrayAdapter.add(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_PROGRAMME)));
      cursor.moveToNext();
    }
    arrayAdapter.add("");
    ((Spinner) findViewById(R.id.spinner_programme)).setAdapter(arrayAdapter);
    cursor.close();

    cursor = db.rawQuery("SELECT DISTINCT " + DbHelper.COLUMN_GENDER + " FROM " + DbHelper.TABLE_NAME, null);
    cursor.moveToFirst();
    arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    for(int x=0;x<cursor.getCount();x++) {
      arrayAdapter.add(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_GENDER)));
      cursor.moveToNext();
    }
    arrayAdapter.add("");
    ((Spinner) findViewById(R.id.spinner_gender)).setAdapter(arrayAdapter);
    cursor.close();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.meu_main_activity, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Take appropriate action for each action item click
    switch (item.getItemId()) {
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

  public class JsonTask extends AsyncTask<String, String, String> {
    protected void onPreExecute() {
      super.onPreExecute();
      mProgressDialog = new ProgressDialog(MainActivity.this);
      mProgressDialog.setMessage("Getting data from the web...");
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
          Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
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
        mProgressDialog.setMessage("Preparing database...");
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
}