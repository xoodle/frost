package com.example.kaushal.studentsearch;

import android.content.Intent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private BottomSheetBehavior mBottomSheetBehaviour;
    private EditText mEditText;
    private Button mButton;
    private Spinner mHallSpinner;
    private Spinner mGenderSpinner;
//    private Map<String, Spinner> mSpinners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomSheetBehaviour = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));
        mEditText = (EditText)findViewById(R.id.edit_text);
        mButton = (Button)findViewById(R.id.button_go);
        mHallSpinner = (Spinner)findViewById(R.id.spinner_hall);
        mGenderSpinner = (Spinner)findViewById(R.id.spinner_gender);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SearchResultActivity.getNewIntent(MainActivity.this);
                intent.putExtra("searchQuery", mEditText.getText().toString());
                intent.putExtra("hallFilter", mHallSpinner.getSelectedItem().toString());
                intent.putExtra("genderFilter", mGenderSpinner.getSelectedItem().toString());
                startActivity(intent);
            }
        });
//        mSpinners = new HashMap<String, Spinner>();
//        mSpinners.put("hall", (Spinner)findViewById(R.id.spinner_hall));
//        mSpinners.put("gender", (Spinner)findViewById(R.id.spinner_gender));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.meu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.action_filter) {
            searchFilter();
        }
        return super.onOptionsItemSelected(item);
    }



    private void searchFilter() {
        mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}
