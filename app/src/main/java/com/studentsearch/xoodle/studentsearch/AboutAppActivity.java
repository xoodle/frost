package com.studentsearch.xoodle.studentsearch;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by aryan on 15/7/17.
 */

public class AboutAppActivity extends AppCompatActivity {

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_about_app);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }
}
