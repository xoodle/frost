package com.example.kaushal.studentsearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
public class MainActivity extends AppCompatActivity {

    private EditText mEditText;
    private Button mButton;
    private Spinner mHallSpinner;
    private Spinner mGenderSpinner;

   //    private Map<String, Spinner> mSpinners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = (EditText)findViewById(R.id.edit_text);
        mButton = (Button)findViewById(R.id.button_go);
        mHallSpinner = (Spinner)findViewById(R.id.spinner_hall);
        mGenderSpinner = (Spinner)findViewById(R.id.spinner_gender);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SearchResultActivity.getNewIntent(MainActivity.this);
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.meu_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Take appropriate action for each action item click
    switch (item.getItemId()) {
      case R.id.action_ref:
        // search action
        return true;
      case R.id.menu_settings:
        // refresh
        return true;
      case R.id.menu_main:
        // help action
        return true;
      case R.id.menu_search:
        // check for updates action
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }


}
