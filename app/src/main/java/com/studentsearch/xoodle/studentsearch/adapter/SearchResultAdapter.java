package com.studentsearch.xoodle.studentsearch.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityOptionsCompat;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

  private static ArrayList<StudentData> mStudentData;
  private Activity context;
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

  public SearchResultAdapter(Activity context, ArrayList<StudentData> studentData) {
    this.context = context;
    this.packageName = context.getPackageName();
    Collections.sort(studentData, comparator);
    mStudentData = studentData;
  }

  @Override
  public SearchResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_entries, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {
    holder.bind(context, packageName, position);
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

    public void bind(Activity context, String packageName, final int position) {
      final StudentData studentData = mStudentData.get(position);
      tvName.setText(studentData.getName());
      tvAddress.setText(studentData.getRoomNo().trim() + ", " + (MappingUtils.getInstance().getHallMap().get(studentData.getHall()) != null ? MappingUtils.getInstance().getHallMap().get(studentData.getHall()) : studentData.getHall()));
      tvProgramme.setText(studentData.getProgramme() + ", " + (MappingUtils.getInstance().getDeptAbbrevMap().containsKey(studentData.getDept())?MappingUtils.getInstance().getDeptAbbrevMap().get(studentData.getDept()):studentData.getDept()));
      new CheckUserImage(context, packageName, studentData, iv).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,studentData.getUserName());
      final Activity c = context;
      view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(c, iv, "to_more_detail");
          Intent intent = DetailsActivity.getNewIntent(c);
          intent.putExtra(ConstantUtils.CARD_SLIDER_POSITION, position);
          intent.putExtra("student", studentData);
          c.startActivity(intent, options.toBundle());
        }
      }); }

    public static class CheckUserImage extends AsyncTask<String, Void, Boolean> {
      Context context;
      String packageName;
      StudentData studentData;
      ImageView iv;

      public CheckUserImage(Context context, String packageName, final StudentData studentData, ImageView iv){
        this.context = context;
        this.packageName = packageName;
        this.studentData = studentData;
        this.iv = iv;
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
        Resources res = context.getResources();
        int errID = res.getIdentifier("ic_person_black_48dp", "drawable", packageName);
        File directory = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "studentPics");
        File nomedia = new File(directory, ".nomedia");
        if(!nomedia.exists()) {
          try {
            nomedia.createNewFile();
          }
          catch(Exception e) {}
        }
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
          if(iv.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable)iv.getDrawable()).getBitmap();
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, array);
            try {
              if(image.createNewFile()) {
                FileOutputStream stream = new FileOutputStream(image);
                stream.write(array.toByteArray());
                stream.close();
              }
            }
            catch(FileNotFoundException fnfe){}
            catch(IOException ioe) {}
          }
        } else {
          Picasso.with(context)
                  .load(ConstantUtils.ImageUrl + studentData.getRollNo() + "_0.jpg")
                  .placeholder(errID)
                  .error(errID)
                  .into(iv);
          if(iv.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable)iv.getDrawable()).getBitmap();
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, array);
            try {
              if(image.createNewFile()) {
                FileOutputStream stream = new FileOutputStream(image);
                stream.write(array.toByteArray());
                stream.close();
              }
            }
            catch(FileNotFoundException fnfe){}
            catch(IOException ioe) {}
          }
        }
      }
    }
    private View getView() {
      return view;
    }
  }
}