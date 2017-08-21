package com.studentsearch.xoodle.studentsearch.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.studentsearch.xoodle.studentsearch.R;
import com.studentsearch.xoodle.studentsearch.StudentData;
import com.studentsearch.xoodle.studentsearch.utils.DecodeBitmapTask;
import java.io.File;

/**
 * Created by kaushal on 7/7/17.
 */

public class SliderCard extends RecyclerView.ViewHolder {

  private static int viewWidth = 0;
  private static int viewHeight = 0;
  private ImageView imageView;
  private Context context;
  private DecodeBitmapTask task;

  public SliderCard(View itemView, Context context) {
    super(itemView);
    imageView = (ImageView) itemView.findViewById(R.id.image);
    this.context = context;
  }

  void setContent(final int position, final StudentData studentData) {
      if (viewWidth == 0) {
        itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override
          public void onGlobalLayout() {
            itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            viewWidth = itemView.getWidth();
            viewHeight = itemView.getHeight();
            loadImage(studentData);
          }
        });
      } else {
        loadImage(studentData);
      }
  }

  private void loadImage(StudentData studentData) {
    int errID;
    Resources res = context.getResources();
    if (studentData.getGender().equals("M")) {
      errID = res.getIdentifier("boy", "drawable", context.getPackageName());
    } else {
      errID = res.getIdentifier("girl", "drawable", context.getPackageName());
    }

    File directory = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "studentPics");
    File image = new File(directory, studentData.getRollNo() + "_0");
    Picasso.with(context)
            .load("http://home.iitk.ac.in/~" + studentData.getUserName() + "/dp")
            .resize(150,200)
            .centerCrop()
            .into(this.imageView);
    if(this.imageView.getDrawable() == null) {
      if (image.exists()) {
        Picasso.with(context)
                .load(image)
                .placeholder(errID)
                .error(errID)
                .into(this.imageView);
      } else {
        Picasso.with(context)
                .load("https://oa.cc.iitk.ac.in/Oa/Jsp/Photo/" + studentData.getRollNo() + "_0.jpg")
                .placeholder(errID)
                .error(errID)
                .into(this.imageView);
      }
    }
  }

  void clearContent() {
    if (task != null) {
      task.cancel(true);
    }
  }
}