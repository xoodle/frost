package com.studentsearch.xoodle.studentsearch.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import com.studentsearch.xoodle.studentsearch.StudentData;
import com.studentsearch.xoodle.studentsearch.database.DbHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchData extends AsyncTask<String, String, String> {
  private static boolean isExecuting = false;
  private Context context;
  private ProgressDialog mProgressDialog;

  public FetchData(Context context, ProgressDialog progressDialog) {
    this.context = context;
    this.mProgressDialog = progressDialog;
  }

  public static boolean isExecuting() {
    return isExecuting;
  }

  protected void onPreExecute() {
    super.onPreExecute();
    if(isExecuting || ImageDownloader.isExecuting()) {
      cancel(true);
      Toast.makeText(context, "Try later. A conflicting process is active.", Toast.LENGTH_LONG).show();
    } else {
      isExecuting = true;
      mProgressDialog = new ProgressDialog(context);
      mProgressDialog.setMessage("Getting data from the net...\nMake sure you are connected to IITK network");
      mProgressDialog.setCancelable(false);
      mProgressDialog.show();
    }
  }

  protected String doInBackground(String... params) {
    if(isCancelled()) {
      return null;
    }
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
      AlertDialog.Builder builder = new AlertDialog.Builder(context);
      builder.setMessage("Please connect to the internet and try again");
      AlertDialog dialog = builder.create();
      dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
          // or anything else appropriate
          ((Container)context).finish();
        }
      });
      dialog.show();
    } else {
      mProgressDialog.setMessage("Preparing database...\nIt may take some time");
      new WriteDatabaseAsync().executeOnExecutor(THREAD_POOL_EXECUTOR, result);
    }
  }

  private class WriteDatabaseAsync extends AsyncTask<String, Void, Void> {
    private DbHelper dbHelper;

    @Override
    protected Void doInBackground(String... json) {
      if(isCancelled()) {
        return null;
      }
      Gson gson = new Gson();
      StudentData[] students = gson.fromJson(json[0], StudentData[].class);
      dbHelper = DbHelper.getDbHelperInstance(context.getApplicationContext(), "students", 1);
      dbHelper.insertStudents(students);
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      isExecuting = false;
      if (mProgressDialog.isShowing())
        mProgressDialog.dismiss();
      ((Container)context).setFilterSpinnerEntries();
      // calling this funtion to execute an dialox box on first launch only.
      Intent intent = ((Container)context).getIntent();
      intent.putExtra("first_launch",true);
      ((Container)context).finish();
      context.startActivity(intent);
    }
  }
}