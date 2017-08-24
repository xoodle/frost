package com.studentsearch.xoodle.studentsearch.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import com.studentsearch.xoodle.studentsearch.database.DbHelper;
import com.studentsearch.xoodle.studentsearch.utils.ConstantUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageDownloader extends AsyncTask<Void, Integer, Void> {
  private Context context;
  private static boolean isExecuting = false;
  private ProgressDialog imageDownloaderDialog;

  public ImageDownloader(Context context) {
    this.context = context;
    imageDownloaderDialog = new ProgressDialog(context);
  }

  public static boolean isExecuting() {
    return isExecuting;
  }

  @Override
  protected void onPreExecute() {
    if(isExecuting || FetchData.isExecuting()) {
      cancel(true);
      Toast.makeText(context, "Try later. A conflicting process is active.", Toast.LENGTH_LONG).show();
    } else {
      isExecuting = true;
      imageDownloaderDialog.setTitle("Fetching Images...");
      imageDownloaderDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
      imageDownloaderDialog.setIndeterminate(false);
      imageDownloaderDialog.setCancelable(false);
      imageDownloaderDialog.setMax(100);
      DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          switch (which) {
            case DialogInterface.BUTTON_NEUTRAL:
              dialog.dismiss();
              break;
            case DialogInterface.BUTTON_NEGATIVE:
              cancel(true);
              isExecuting = false;
              break;
          }
        }
      };
      imageDownloaderDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Do in background", listener);
      imageDownloaderDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", listener);
      imageDownloaderDialog.show();
    }
    super.onPreExecute();
  }

  @Override
  protected Void doInBackground(Void... arg0) {
    Bitmap mIcon;
    File directory = null;

    if (isExternalStorageWritable()) {
      directory = new File(context.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "studentPics");
    } else {
      return null;
    }
    if (!directory.mkdirs()) {
      Log.e("imageDownloader", "Directory not created");
    }

    SQLiteDatabase db = DbHelper.getDbHelperInstance(context.getApplicationContext(), DbHelper.TABLE_NAME, 1).getReadableDatabase();
    Cursor cursor = db.rawQuery("SELECT ALL " + DbHelper.COLUMN_ROLL_NO + " FROM " + DbHelper.TABLE_NAME, null);
    cursor.moveToFirst();
    final int total = cursor.getCount();

    for (int x = 0; x < total; x++) {
      if (isCancelled()) {
        Log.i("ImageDownloader", "getPics: cancelled");
        break;
      }
      String rollno = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_ROLL_NO));
      try {
        InputStream in = new java.net.URL(ConstantUtils.ImageUrl + rollno + "_0.jpg").openStream();
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
    isExecuting = false;
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