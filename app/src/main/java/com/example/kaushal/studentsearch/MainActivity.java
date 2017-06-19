package com.example.kaushal.studentsearch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kaushal.studentsearch.database.DbHelper;
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
    mEditText = (EditText) findViewById(R.id.edit_text);
    mButton = (Button) findViewById(R.id.button_go);
    mButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = SearchResultActivity.getNewIntent(MainActivity.this);
        intent.putExtra("name", getSearchQuery());
        startActivity(intent);
      }
    });
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
        refreshDatabase();
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

  public DbHelper getDbHelper() {
    return dbHelper;
  }

  public String getSearchQuery() {
    String name = mEditText.getText().toString();
    return name;
  }

  private void refreshDatabase() {
    new JsonTask().execute("https://yashsriv.org/api");
  }

  public class JsonTask extends AsyncTask<String, String, String> {
    protected void onPreExecute() {
      super.onPreExecute();
      mProgressDialog = new ProgressDialog(MainActivity.this);
      mProgressDialog.setMessage("Please wait");
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
      Gson gson = new Gson();
      StudentData student;
      dbHelper = DbHelper.getDbHelperInstance(getApplicationContext(), "students", 1);
      Matcher m = Pattern.compile("\\{(.*?)\\}").matcher(result);
      while (m.find()) {
        student = gson.fromJson("{" + m.group(1) + "}", StudentData.class);
        dbHelper.insertStudent(student);
      }
      if (mProgressDialog.isShowing())
        mProgressDialog.dismiss();
    }
  }
}