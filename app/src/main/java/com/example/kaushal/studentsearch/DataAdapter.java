package com.example.kaushal.studentsearch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kaushal on 15/6/17.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

  private ArrayList<StudentData> mStudentData;

  public DataAdapter(ArrayList<StudentData> studentData) {
    this.mStudentData = studentData;
  }

  @Override
  public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_entries, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
    holder.bind(mStudentData.get(position));
  }

  @Override
  public int getItemCount() {
    return mStudentData.size();
  }

  @Override
  public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
  }

  public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
private TextView mNameView;


    public ViewHolder(View v) {
      super(v);
      mNameView = (TextView) v.findViewById(R.id.tv_name);
    }

    @Override
    public void onClick(View v) {
      //clicked
    }

    public void bind(StudentData studentData) {
      mNameView.setText(studentData.getName());
    }
  }
}