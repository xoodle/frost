package com.studentsearch.xoodle.studentsearch.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.studentsearch.xoodle.studentsearch.R;
import com.studentsearch.xoodle.studentsearch.StudentData;
import com.studentsearch.xoodle.studentsearch.utils.DecodeBitmapTask;
import com.studentsearch.xoodle.studentsearch.utils.GlobalDrawables;

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
  void setContent(final int position) {
      if (viewWidth == 0) {
      itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
          itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

          viewWidth = itemView.getWidth();
          viewHeight = itemView.getHeight();
//          loadBitmap(resId);
          loadBitmap(position);
        }
      });
    } else {
//      loadBitmap(resId);
        loadBitmap(position);
    }
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