package com.studentsearch.xoodle.studentsearch;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.studentsearch.xoodle.studentsearch.adapter.SearchResultAdapter;
import com.studentsearch.xoodle.studentsearch.database.DbHelper;
import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {

  public DbHelper dbHelper;
  RecyclerView.LayoutManager mLinearLayoutManager;
  private RecyclerView mResultRecyclerView;
  private SearchResultAdapter mSearchResultAdapter;
  public static ArrayList<StudentData> studentDataArrayList;
  private Cursor cursor;

  public static Intent getNewIntent(Context c) {
    return new Intent(c, SearchResultActivity.class);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_result);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    mResultRecyclerView = (RecyclerView) findViewById(R.id.rv_main);
    mLinearLayoutManager = new LinearLayoutManager(this);
    mResultRecyclerView.setLayoutManager(mLinearLayoutManager);
    studentDataArrayList = new ArrayList<>();
    String[] filter = getFilterArray();
    dbHelper = DbHelper.getDbHelperInstance(getApplicationContext(), DbHelper.TABLE_NAME, 1);
    performQuery(filter);
  }

  private String[] getFilterArray() {
    Intent i = getIntent();
    String[] filter = {
            i.getExtras().getString(DbHelper.COLUMN_NAME, ""),
            i.getExtras().getString(DbHelper.COLUMN_ROLL_NO, ""),
            i.getExtras().getString(DbHelper.COLUMN_HALL, ""),
            i.getExtras().getString(DbHelper.COLUMN_BLOOD_GROUP, ""),
            i.getExtras().getString(DbHelper.COLUMN_DEPT, ""),
            i.getExtras().getString(DbHelper.COLUMN_PROGRAMME, ""),
            i.getExtras().getString(DbHelper.COLUMN_GENDER, ""),
            i.getExtras().getString(DbHelper.COLUMN_YEAR, ""),
            i.getExtras().getString(DbHelper.COLUMN_USER_NAME, "")
    };
    return filter;
  }

  private String getEmailIds() {
    String emails = "";
    int counter = studentDataArrayList.size();
    for(StudentData s : studentDataArrayList) {
      if(!s.equals("")) {
        emails = emails + s.getUserName() + "@iitk.ac.in";
        if (counter != 1)
          emails = emails + ", ";
      }
      counter--;
    }
    return emails;
  }

  private void copyEmailIdsToClipboard() {
    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    ClipData emailClip = ClipData.newPlainText("email ids", getEmailIds());
    clipboard.setPrimaryClip(emailClip);
    Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_search_activity, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()) {
      case R.id.action_copy_email:
        copyEmailIdsToClipboard();
        break;
      case android.R.id.home:
        onBackPressed();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void performQuery(String filter[]) throws Resources.NotFoundException, NullPointerException {
    new AsyncStudentSearch().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, filter);
  }

  public class AsyncStudentSearch extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... filter) {
      try {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DbHelper.TABLE_NAME
                + " WHERE (" + DbHelper.COLUMN_NAME + " LIKE \"%"
                + filter[0] + "%\""
                + " OR " + DbHelper.COLUMN_ROLL_NO + " LIKE \"%"
                + filter[0] + "%\""
                + " OR " + DbHelper.COLUMN_USER_NAME + " LIKE \"%"
                + filter[0] + "%\")";
        if(!filter[2].equals("")) {
          query = query + " AND " + DbHelper.COLUMN_HALL + " = \"" + filter[2] + "\"";
        }
        if(!filter[3].equals("")) {
          query = query + " AND " + DbHelper.COLUMN_BLOOD_GROUP + " = \"" + filter[3] + "\"";
        }
        if(!filter[4].equals("")) {
          query = query + " AND " + DbHelper.COLUMN_DEPT + " = \"" + filter[4] + "\"";
        }
        if(!filter[5].equals("")) {
          query = query + " AND " + DbHelper.COLUMN_PROGRAMME + " = \"" + filter[5] + "\"";
        }
        if(!filter[6].equals("")) {
          query = query + " AND " + DbHelper.COLUMN_GENDER + " = \"" + filter[6] + "\"";
        }
        if(!filter[7].equals("Others")) {
          query = query + " AND " + DbHelper.COLUMN_YEAR + " LIKE \"%"
                  + filter[7] + "%\"";
        }
        else {
          query = query + " AND ("
                  + DbHelper.COLUMN_YEAR + "!=\"Y10\" " + "AND "
                  + DbHelper.COLUMN_YEAR + "!=\"Y11\" " + "AND "
                  + DbHelper.COLUMN_YEAR + "!=\"Y12\" " + "AND "
                  + DbHelper.COLUMN_YEAR + "!=\"Y13\" " + "AND "
                  + DbHelper.COLUMN_YEAR + "!=\"Y14\" " + "AND "
                  + DbHelper.COLUMN_YEAR + "!=\"Y15\" " + "AND "
                  + DbHelper.COLUMN_YEAR + "!=\"Y16\" " + "AND "
                  + DbHelper.COLUMN_YEAR + "!=\"Y17\" " + "AND "
                  + DbHelper.COLUMN_YEAR + "!=\"Y18\")";
        }
        cursor = db.rawQuery(query, null);
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
      int count = studentDataArrayList.size();
      if(count > 0) {
        getSupportActionBar().setSubtitle("Displaying " + count + (count == 1 ? " result" : " results"));

        mSearchResultAdapter = new SearchResultAdapter(SearchResultActivity.this, studentDataArrayList);
        mResultRecyclerView.setAdapter(mSearchResultAdapter);
        mResultRecyclerView.setVisibility(View.VISIBLE);
      } else {
        findViewById(R.id.tv_not_found).setVisibility(View.VISIBLE);
      }
    }
  }
}