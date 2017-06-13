package com.example.kaushal.studentsearch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SearchResultActivity extends MainActivity {

    public TextView JsonText;
    public ProgressDialog pd;

    public static Intent getNewIntent(Context c) {
        return new Intent(c, SearchResultActivity.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
      // get action bar
      ActionBar actionBar = getSupportActionBar();
      // Enabling Up / Back navigation
       actionBar.setDisplayHomeAsUpEnabled(true);
      JsonText = (TextView) findViewById(R.id.tvJsontext);
      new JsonTask().execute("https://yashsriv.org/api");

    }
    // get action bar
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.meu_search_activity, menu);
    return super.onCreateOptionsMenu(menu);
  }
  public class JsonTask extends AsyncTask<String, String, String> {
  protected void onPreExecute() {
    super.onPreExecute();

    pd = new ProgressDialog(SearchResultActivity.this);
    pd.setMessage("Please wait");
    pd.setCancelable(false);
    pd.show();
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
        buffer.append(line+"\n");
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
    if (pd.isShowing()){
      pd.dismiss();
    }
    JsonText.setText(result);
  }
}
}

