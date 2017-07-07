package com.studentsearch.xoodle.studentsearch.adapter;

/**
 * Created by kaushal on 7/7/17.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.studentsearch.xoodle.studentsearch.R;
import com.studentsearch.xoodle.studentsearch.StudentData;
import com.studentsearch.xoodle.studentsearch.utils.GlobalDrawables;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderCard> {

  private final int count;
  private final ArrayList<Drawable> drawables;
  private final View.OnClickListener listener;
  private Context context;

  public SliderAdapter(Context context, View.OnClickListener listener) {
    this.drawables = GlobalDrawables.drawables;
    this.count = (this.drawables).size();
    this.context = context;
    this.listener = listener;
  }

  @Override
  public SliderCard onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.layout_slider_card, parent, false);

    if (listener != null) {
      view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          listener.onClick(view);
        }
      });
    }

    return new SliderCard(view, context);
  }

  @Override
  public void onBindViewHolder(SliderCard holder, int position) {
//    holder.setContent(imageViewIds.get(position));
    holder.setContent(position);
  }

  @Override
  public void onViewRecycled(SliderCard holder) {
    holder.clearContent();
  }

  @Override
  public int getItemCount() {
    return count;
  }

}