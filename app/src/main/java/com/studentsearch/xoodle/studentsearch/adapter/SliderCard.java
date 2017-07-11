package com.studentsearch.xoodle.studentsearch.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.studentsearch.xoodle.studentsearch.R;
import com.studentsearch.xoodle.studentsearch.StudentData;
import com.studentsearch.xoodle.studentsearch.utils.DecodeBitmapTask;
import com.studentsearch.xoodle.studentsearch.utils.GlobalDrawables;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by kaushal on 7/7/17.
 */

public class SliderCard extends RecyclerView.ViewHolder {

  private static int viewWidth = 0;
  private static int viewHeight = 0;

//  private final ImageView imageView;
private ImageView imageView;

  private Context context;

  private DecodeBitmapTask task;

  public SliderCard(View itemView, Context context) {
    super(itemView);
    imageView = (ImageView) itemView.findViewById(R.id.image);
    this.context = context;
  }

//  void setContent(@DrawableRes final int resId) {
//    void setContent(final Bitmap image) {
  void setContent(final int position, final StudentData studentData) {
      if (viewWidth == 0) {
        itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override
          public void onGlobalLayout() {
            itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            viewWidth = itemView.getWidth();
            viewHeight = itemView.getHeight();

            int errID;
            Resources res = context.getResources();
            if (studentData.getGender().equals("M")) {
              errID = res.getIdentifier("boy", "drawable", context.getPackageName());
            } else {
              errID = res.getIdentifier("girl", "drawable", context.getPackageName());
            }

            File directory = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "studentPics");
            File image = new File(directory, studentData.getRollNo() + "_0");
//      File image = new File(directory, "160757_0.jpeg");
            if (image.exists()) {
              Log.i("ad", studentData.getRollNo());
              Picasso.with(context)
                      .load(image)
                      .placeholder(errID)
                      .error(errID)
                      .into(imageView);
            } else {
              Picasso.with(context)
                      .load("http://oa.cc.iitk.ac.in/Oa/Jsp/Photo/" + studentData.getRollNo() + "_0.jpg")
                      .placeholder(errID)
                      .error(errID)
                      .into(imageView);
            }


//          loadBitmap(resId);
//            loadBitmap(position);
          }
        });
      }
//    } else {
////      loadBitmap(resId);
//        loadBitmap(position);
//    }
  }

  void clearContent() {
    if (task != null) {
      task.cancel(true);
    }
  }

//  private void loadBitmap(@DrawableRes int resId) {
//  private void loadBitmap(Bitmap image) {
  private void loadBitmap(int position) {
//    task = new DecodeBitmapTask(itemView.getResources(), resId, viewWidth, viewHeight) {
//      @Override
//      protected void onPostExecute(Bitmap bitmap) {
//        super.onPostExecute(bitmap);
//        imageView.setImageBitmap(bitmap);
//      }
//    };
//    task.execute();
    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    imageView.setImageDrawable(GlobalDrawables.drawables.get(position));
//    imageView.setImageBitmap(GlobalDrawables.bitmaps.get(position));
//    imageView = GlobalDrawables.imageViews.get(position);

  }

}