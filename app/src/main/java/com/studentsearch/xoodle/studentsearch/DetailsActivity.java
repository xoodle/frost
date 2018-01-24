package com.studentsearch.xoodle.studentsearch;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.studentsearch.xoodle.studentsearch.adapter.SearchResultAdapter;
import com.studentsearch.xoodle.studentsearch.utils.ConstantUtils;
import com.studentsearch.xoodle.studentsearch.utils.MappingUtils;

import org.w3c.dom.Text;

import java.io.File;

/**
 * Created by aryan on 15/7/17.
 */

public class DetailsActivity extends AppCompatActivity {

  private StudentData thisStudent = null;
  private ImageView iv = null;
  private TextView tvName = null;
  private TextView tvEmail = null;
  private TextView tvRollNo = null;
  private TextView tvAddr = null;
  private TextView tvProgramme = null;
  private TextView tvHometown = null;
  private TextView tvBloodGroup = null;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    thisStudent = getIntent().getParcelableExtra("student");
    iv  = (ImageView)findViewById(R.id.details_iv);
    new SearchResultAdapter.ViewHolder.CheckUserImage(this, getPackageName(), thisStudent, iv).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,thisStudent.getUserName());
    tvName  = (TextView)findViewById(R.id.details_tv_name);
    tvEmail  = (TextView)findViewById(R.id.details_tv_email);
    tvRollNo  = (TextView)findViewById(R.id.details_tv_rollno);
    tvAddr  = (TextView)findViewById(R.id.details_tv_address);
    tvProgramme  = (TextView)findViewById(R.id.details_tv_programme);
    tvHometown  = (TextView)findViewById(R.id.details_tv_hometown);
    tvBloodGroup  = (TextView)findViewById(R.id.details_tv_bloodgroup);
    tvName.setText(thisStudent.getName().trim());
    tvEmail.setText(thisStudent.getUserName().trim() + "@iitk.ac.in");
    tvRollNo.setText(thisStudent.getRollNo().trim());
    tvAddr.setText(thisStudent.getRoomNo().trim() + ", " + (MappingUtils.getInstance().getHallMap().containsKey(thisStudent.getHall()) ? MappingUtils.getInstance().getHallMap().get(thisStudent.getHall()) : thisStudent.getHall().trim()));
    tvProgramme.setText(thisStudent.getProgramme().trim() + ", " + thisStudent.getDept().trim());
    String hometown = thisStudent.getAddress().trim();
    if(hometown.charAt(0)>=97 && hometown.charAt(0)<=122) hometown = ((char)(hometown.charAt(0)-32)) + hometown.substring(1);
    tvHometown.setText(hometown);
    tvBloodGroup.setText(thisStudent.getBloodGroup().trim());
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if(item.getItemId() == android.R.id.home) onBackPressed();
    else if(item.getItemId() == R.id.menu_detail_email) {
      Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
              "mailto",thisStudent.getUserName() + "@iitk.ac.in", null));
      startActivity(intent);
    }
    return true;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_detail_activity, menu);
    return super.onCreateOptionsMenu(menu);
  }

  public static Intent getNewIntent(Activity c) {
    return new Intent(c, DetailsActivity.class);
  }
}
