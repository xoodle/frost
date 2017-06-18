package com.example.kaushal.studentsearch;

import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.kaushal.studentsearch.database.DbHelper;

import java.util.ArrayList;

public class SearchResultActivity extends MainActivity {

  private RecyclerView mResultRecyclerView;
  private DataAdapter mDataAdapter;
  private ArrayList<StudentData> studentDataArrayList;
  LinearLayoutManager mLinearLayoutManager;
  private DbHelper dbHelper;
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
//    dbHelper = new DbHelper(this, "students", 1);
    String name = getIntent().getExtras().getString("name", "Anil Kumar").toString();
    DbHelper dbHelper = (DbHelper) getIntent().getSerializableExtra("DbHelper");
    Log.v("SearchResultActivity", name);
    performQuery(name);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.meu_search_activity, menu);
    return super.onCreateOptionsMenu(menu);
  }

  private void performQuery(String name) throws Resources.NotFoundException, NullPointerException {
    new AsyncStudentSearch().execute(name);
  }

  public class AsyncStudentSearch extends AsyncTask<String, Void, Void> {
    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... name) {
      try {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
//      cursor = db.rawQuery(
//              "SELECT * FROM "
//              + DbHelper.TABLE_NAME
//              + " WHERE "
//              + DbHelper.COLUMN_NAME
//              + " = \'"
//              + name[0].toString()
//              + "\'",
//              null
//      );
        cursor = db.rawQuery(
                "SELECT * FROM students WHERE name = \"Sunil Kumar\"",
                null
        );
//      cursor = db.query(true,
//              DbHelper.TABLE_NAME,
//              null,
//              DbHelper.COLUMN_NAME+" = ?",
//              new String[]{name+""},
//              null,
//              null,
//              null,
//              null,
//              null);
        if (cursor.getCount() > 0) {
          cursor.moveToFirst();
          do {
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
            cursor.moveToNext();
          } while (!cursor.isLast());
        } else {
          throw new Resources.NotFoundException("Student with name " + name[0] + " does not exist.");
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
      mDataAdapter = new DataAdapter(studentDataArrayList);
      mResultRecyclerView.setAdapter(mDataAdapter);
    }
  }
}