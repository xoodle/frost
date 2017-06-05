package com.example.kaushal.studentsearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by kaushal on 5/6/17.
 */

public class SearchResultActivity extends AppCompatActivity {
    private String mSearchQuery;
    private String mHallFilter;
    private String mGenderFilter;
    private TextView mTv;

    public static Intent getNewIntent(Context c) {
        return new Intent(c, SearchResultActivity.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        mSearchQuery = getIntent().getStringExtra("searchQuery");
        mHallFilter = getIntent().getStringExtra("hallFilter");
        mGenderFilter = getIntent().getStringExtra("genderFilter");
        mTv = (TextView)findViewById(R.id.text);
        mTv.setText(mSearchQuery + "\n" + mHallFilter + "\n" + mGenderFilter);
    }
}
