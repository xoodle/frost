package com.example.kaushal.studentsearch;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import static android.support.v4.view.GravityCompat.START;
import java.util.HashMap;
import java.util.Map;

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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.meu_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }


}
