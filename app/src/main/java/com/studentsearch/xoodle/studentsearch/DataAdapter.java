package com.studentsearch.xoodle.studentsearch;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

  private ArrayList<StudentData> mStudentData;
  private Context context;
  private String packageName;

  public DataAdapter(Context context, ArrayList<StudentData> studentData) {
    this.context = context;
    this.packageName = context.getPackageName();
    this.mStudentData = studentData;
  }

  @Override
  public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_entries, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.bind(context, packageName, mStudentData.get(position));
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
    private TextView mNameView, mRollView, mDeptView, mHallView, mAddressView, mUserBloodView;
    private ImageView mImageView;

    public ViewHolder(View v) {
      super(v);
      mNameView = (TextView) v.findViewById(R.id.tv_name);
      mRollView = (TextView) v.findViewById(R.id.tv_roll);
      mDeptView = (TextView) v.findViewById(R.id.tv_dept);
      mHallView = (TextView) v.findViewById(R.id.tv_hall);
      mAddressView = (TextView) v.findViewById(R.id.tv_address);
      mUserBloodView = (TextView) v.findViewById(R.id.tv_user_blood);
      mImageView = (ImageView) v.findViewById(R.id.user_image);
    }

    @Override
    public void onClick(View v) {
      //clicked
    }

    public void bind(Context context, String packageName, StudentData studentData) {
      mNameView.setText("Name: "+studentData.getName());
      mRollView.setText("Roll No: "+studentData.getRollNo());
      mDeptView.setText("Programme: "+studentData.getProgramme()+"    Dept:"+studentData.getDept());
      mHallView.setText("IITK Address: "+studentData.getRoomNo()+ ", "+studentData.getHall());
      mAddressView.setText("Home Address: "+studentData.getAddress());
      mUserBloodView.setText("Username: "+studentData.getUserName()+"    Blood Group: "+studentData.getBloodGroup());

      int resID;
      Resources res = context.getResources();
      if(studentData.getGender().equals("M")) {
        resID = res.getIdentifier("boy", "drawable", packageName);
      } else {
        resID = res.getIdentifier("girl", "drawable", packageName);
      }
      Picasso.with(context)
              .load("http://oa.cc.iitk.ac.in/Oa/Jsp/Photo/"+studentData.getRollNo()+"_0.jpg")
              .placeholder(resID)
              .error(resID)
              .into(this.mImageView);
    }
  }
}