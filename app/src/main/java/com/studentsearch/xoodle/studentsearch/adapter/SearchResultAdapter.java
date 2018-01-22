package com.studentsearch.xoodle.studentsearch.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.studentsearch.xoodle.studentsearch.DetailsActivity;
import com.studentsearch.xoodle.studentsearch.R;
import com.studentsearch.xoodle.studentsearch.StudentData;
import com.studentsearch.xoodle.studentsearch.utils.ConstantUtils;
import com.studentsearch.xoodle.studentsearch.utils.MappingUtils;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

  private ArrayList<StudentData> mStudentData;
  private Context context;
  private String packageName;
  private Comparator<StudentData> comparator = new Comparator<StudentData>() {
    @Override
    public int compare(StudentData s1, StudentData s2) {
      MappingUtils mappingUtils = MappingUtils.getInstance();
      int result;
      Map<String, String> yearMap = mappingUtils.getYearMap();
      if (yearMap.get(s1.getYear()) == null && yearMap.get(s2.getYear()) == null) {
        result = 0;
      } else if (yearMap.get(s1.getYear()) == null && yearMap.get(s2.getYear()) != null) {
        result = 1;
      } else if(yearMap.get(s1.getYear()) != null && yearMap.get(s2.getYear()) == null) {
        result = -1;
      } else {
        result = (yearMap.get(s2.getYear()))
                .compareTo(yearMap.get(s1.getYear()));
        if (result == 0) {
          String r1 = s1.getRollNo();
          String r2 = s2.getRollNo();
          try {
            result = Integer.parseInt(r1) - Integer.parseInt(r2);
          } catch (Exception e) {
            result = r1.compareTo(r2);
          }
        }
      }
      return result;
    }
  };

  public SearchResultAdapter(Context context, ArrayList<StudentData> studentData) {
    this.context = context;
    this.packageName = context.getPackageName();
    Collections.sort(studentData, comparator);
    this.mStudentData = studentData;
  }

  @Override
  public SearchResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_entries, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {
    holder.bind(context, packageName, mStudentData.get(position));
    holder.getView().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = DetailsActivity.getNewIntent(context);
        intent.putExtra(ConstantUtils.CARD_SLIDER_POSITION, position);
        v.getContext().startActivity(intent);
      }
    });
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
    private TextView tvName, tvAddress, tvProgramme;
    private View view;
    public ImageView iv;

    public ViewHolder(View v) {
      super(v);
      view = v;
      tvName = (TextView) v.findViewById(R.id.card_entries_tv_name);
      tvAddress = (TextView) v.findViewById(R.id.card_entries_tv_address);
      tvProgramme = (TextView) v.findViewById(R.id.card_entries_tv_programme);
      iv = (ImageView) v.findViewById(R.id.card_entries_iv);
    }

    @Override
    public void onClick(View v) {
      // some action
    }

    public void bind(Context context, String packageName, final StudentData studentData) {
      tvName.setText(studentData.getName());
      tvAddress.setText(studentData.getRoomNo().trim() + ", " + (MappingUtils.getInstance().getHallMap().get(studentData.getHall()) != null ? MappingUtils.getInstance().getHallMap().get(studentData.getHall()) : studentData.getHall()));
      tvProgramme.setText(studentData.getProgramme() + ", " + MappingUtils.getInstance().getDeptAbbrevMap().get(studentData.getDept()));
      new CheckUserImage(context, packageName, studentData).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,studentData.getUserName());
    }

    class CheckUserImage extends AsyncTask<String, Void, Boolean> {
      Context context;
      String packageName;
      StudentData studentData;

      public CheckUserImage(Context context, String packageName, final StudentData studentData){
        this.context = context;
        this.packageName = packageName;
        this.studentData = studentData;
      }

      protected Boolean doInBackground(String... params) {
        try {
          HttpURLConnection connection = (HttpURLConnection) (new URL("http://home.iitk.ac.in/~"+params[0]+"/dp").openConnection());
          connection.setRequestProperty("User-Agent", "Test");
          connection.setRequestProperty("Connection", "close");
          connection.setConnectTimeout(500);
          connection.connect();
          if (connection.getResponseCode() == 200) {
            String contentType = connection.getHeaderField("Content-Type");
            boolean image = contentType.startsWith("image/");
            if(image) {
              return true;
            }
          }
        } catch (IOException e) {
          Log.e("test","not able to connect");
        }
        return false;
      }

      protected void onPostExecute(Boolean imageURLAvailable){
        int errID;
        Resources res = context.getResources();
        errID = res.getIdentifier("ic_person_black_48dp", "drawable", packageName);
        File directory = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "studentPics");
        File image = new File(directory, studentData.getRollNo() + "_0");

        if (image.exists()) {
          Picasso.with(context)
                  .load(image)
                  .placeholder(errID)
                  .error(errID)
                  .into(iv);
        } else if (imageURLAvailable) {
          Picasso.with(context)
                  .load("http://home.iitk.ac.in/~" + studentData.getUserName() + "/dp")
                  .resize(150,200)
                  .centerCrop()
                  .into(iv);
        } else {
          Picasso.with(context)
                  .load(ConstantUtils.ImageUrl + studentData.getRollNo() + "_0.jpg")
                  .placeholder(errID)
                  .error(errID)
                  .into(iv);
        }
      }
    }
    private View getView() {
      return view;
    }
  }
}