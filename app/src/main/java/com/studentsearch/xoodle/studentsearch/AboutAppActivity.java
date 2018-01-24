package com.studentsearch.xoodle.studentsearch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by aryan on 15/7/17.
 */

public class AboutAppActivity extends AppCompatActivity implements View.OnClickListener{

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_about_app);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    findViewById(R.id.about_app_btn_app_repo).setOnClickListener(this);
    findViewById(R.id.about_app_btn_aryan_email).setOnClickListener(this);
    findViewById(R.id.about_app_btn_aryan_github).setOnClickListener(this);
    findViewById(R.id.about_app_btn_kaushal_email).setOnClickListener(this);
    findViewById(R.id.about_app_btn_kaushal_github).setOnClickListener(this);
    findViewById(R.id.about_app_btn_utkarsh_email).setOnClickListener(this);
    findViewById(R.id.about_app_btn_utkarsh_github).setOnClickListener(this);
    findViewById(R.id.about_app_btn_siddharth_email).setOnClickListener(this);
    findViewById(R.id.about_app_btn_siddharth_github).setOnClickListener(this);
    findViewById(R.id.about_app_btn_yash_email).setOnClickListener(this);
    findViewById(R.id.about_app_btn_yash_github).setOnClickListener(this);
  }
  
  @Override
  public void onClick(View v) {
    Intent intent = null;
    switch (v.getId()) {
      case R.id.about_app_btn_app_repo: intent = new Intent("android.intent.action.VIEW", Uri.parse("https://github.com/xoodle/frost"));
                                        break;
      case R.id.about_app_btn_aryan_email: intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
              "mailto","saryan@iitk.ac.in", null));
                                          break;
      case R.id.about_app_btn_aryan_github: intent =  new Intent("android.intent.action.VIEW", Uri.parse("https://github.com/aryanshar"));
                                          break;
      case R.id.about_app_btn_kaushal_email: intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
              "mailto","kaushalk@iitk.ac.in", null));
                                          break;
      case R.id.about_app_btn_kaushal_github: intent =  new Intent("android.intent.action.VIEW", Uri.parse("https://github.com/kaushal-k"));
                                          break;
      case R.id.about_app_btn_utkarsh_email: intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
              "mailto","butkarsh@iitk.ac.in", null));
        break;
      case R.id.about_app_btn_utkarsh_github: intent =  new Intent("android.intent.action.VIEW", Uri.parse("https://github.com/ubarsaiyan"));
        break;
      case R.id.about_app_btn_siddharth_email: intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
              "mailto","sidgane@iitk.ac.in", null));
        break;
      case R.id.about_app_btn_siddharth_github: intent =  new Intent("android.intent.action.VIEW", Uri.parse("https://github.com/siddharth2610"));
        break;
      case R.id.about_app_btn_yash_email: intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
              "mailto","yashsriv@iitk.ac.in", null));
        break;
      case R.id.about_app_btn_yash_github: intent =  new Intent("android.intent.action.VIEW", Uri.parse("https://github.com/yashsriv"));
        break;
    }
    startActivity(intent);
  }
}
