package com.studentsearch.xoodle.studentsearch.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by utkarsh on 30/6/17.
 */

public class ImageDownloader {

  private Context context;

  public ImageDownloader(Context context) {

    this.context = context;
  }

  public void getPics() {
    SQLiteDatabase db = DbHelper.getDbHelperInstance(context, DbHelper.TABLE_NAME, 1).getReadableDatabase();
    Cursor cursor = db.rawQuery("SELECT ALL " + DbHelper.COLUMN_ROLL_NO + " FROM " + DbHelper.TABLE_NAME, null);
    cursor.moveToFirst();
    for (int x = 0; x < 15; x++) {
      String rollno;
      rollno = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_ROLL_NO));
      Log.i("ImageDownloader", rollno);
      Picasso.with(context)
              .load("http://oa.cc.iitk.ac.in/Oa/Jsp/Photo/" + rollno + "_0.jpg")
              .into(picassoImageTarget("studentPics", rollno + "_0.jpg"));
      cursor.moveToNext();
    }
    cursor.close();
    Log.i("ImageDownloader", "getPics: done");

    Picasso.with(context)
            .load("http://oa.cc.iitk.ac.in/Oa/Jsp/Photo/160757_0.jpg")
            .into(picassoImageTarget("studentPics", "160757_0.jpg"));
  }

  private Target picassoImageTarget(final String imageDir, final String imageName) {
    Log.d("picassoImageTarget", " picassoImageTarget");

    final File directory = new File(this.context.getExternalFilesDir(
            Environment.DIRECTORY_PICTURES), imageDir);
    if (!directory.mkdirs()) {
      Log.e("imageDownloader", "Directory not created");
    }


    return new Target() {
      @Override
      public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
        new Thread(new Runnable() {
          @Override
          public void run() {
            final File myImageFile = new File(directory, imageName); // Create image file
            FileOutputStream fos = null;
            try {
              fos = new FileOutputStream(myImageFile);
              bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (IOException e) {
              e.printStackTrace();
            } finally {
              try {
                if (fos != null) {
                  fos.close();
                }
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
            Log.i("image", "image saved to >>>" + myImageFile.getAbsolutePath());
          }
        }).start();
      }

      @Override
      public void onBitmapFailed(Drawable errorDrawable) {
      }

      @Override
      public void onPrepareLoad(Drawable placeHolderDrawable) {
        if (placeHolderDrawable != null) {
        }
      }
    };
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

