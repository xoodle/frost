package com.studentsearch.xoodle.studentsearch;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.studentsearch.xoodle.studentsearch.database.DbHelper;

import java.util.ArrayList;

public class SearchResultActivity extends MainActivity {

  private RecyclerView mResultRecyclerView;
  private DataAdapter mDataAdapter;
  private ArrayList<StudentData> studentDataArrayList;
  RecyclerView.LayoutManager mLinearLayoutManager;
  public DbHelper dbHelper;
  private Cursor cursor;

  public static Intent getNewIntent(Context c) {
    return new Intent(c, SearchResultActivity.class);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_result);
    ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    mResultRecyclerView = (RecyclerView) findViewById(R.id.rv_main);
    mLinearLayoutManager = new LinearLayoutManager(this);
    mResultRecyclerView.setLayoutManager(mLinearLayoutManager);
    studentDataArrayList = new ArrayList<>();
    String name = getIntent().getExtras().getString("name", "Student Search");
    String hall = getIntent().getExtras().getString("hall", "HALL5");
    String[] filter = {name, hall};
    dbHelper = DbHelper.getDbHelperInstance(getApplicationContext(), "students", 1);
    performQuery(filter);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.meu_search_activity, menu);
    return super.onCreateOptionsMenu(menu);
  }

  private void performQuery(String filter[]) throws Resources.NotFoundException, NullPointerException {
    new AsyncStudentSearch().execute(filter);
  }

  public class AsyncStudentSearch extends AsyncTask<String, Void, Void> {
    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... filter) {
      try {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery(
                "SELECT * FROM students WHERE name LIKE \"%"
                + filter[0]
                + "%\" AND hall = \""
                + filter[1]
                + "\"",
                null
        );
        if (cursor.getCount() > 0) {
          cursor.moveToFirst();
          for(int x=1;x<=cursor.getCount();x++,cursor.moveToNext()) {
            StudentData student = new StudentData(
                    cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_ADDRESS)),
                    cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_BLOOD_GROUP)),
                    cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_DEPT)),
                    cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_GENDER)),
                    cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_HALL)),
                    cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_ROLL_NO)),
                    cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_PROGRAMME)),
                    cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_ROOM_NO)),
                    cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_USER_NAME))
            );
            studentDataArrayList.add(student);
          }
          cursor.close();
        }
      } catch (NullPointerException e) {
        e.printStackTrace();
        throw e;
      } finally {
        if (cursor != null)
          cursor.close();
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      if(studentDataArrayList.size()>0) {
        mDataAdapter = new DataAdapter(getApplicationContext(), studentDataArrayList);
        mResultRecyclerView.setAdapter(mDataAdapter);
        mResultRecyclerView.setVisibility(View.VISIBLE);
      } else {
        findViewById(R.id.tv_not_found).setVisibility(View.VISIBLE);
      }
    }
  }
}