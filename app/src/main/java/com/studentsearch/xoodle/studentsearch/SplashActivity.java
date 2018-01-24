package com.studentsearch.xoodle.studentsearch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by aryan on 7/6/17.
 */

public class SplashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.splashscreen);

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {

        startActivity(new Intent(SplashActivity.this,MainActivity.class));
        finish();

      }
    },300);
  }
}