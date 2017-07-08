package com.studentsearch.xoodle.studentsearch.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.studentsearch.xoodle.studentsearch.StudentData;

import java.io.Serializable;

import static android.R.attr.data;

@SuppressWarnings("serial")
public class DbHelper extends SQLiteOpenHelper implements Serializable {
  private static DbHelper dbHelper;
  public static final String TABLE_NAME = "students";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_NAME = "name";
  public static final String COLUMN_ROLL_NO = "roll_no";
  public static final String COLUMN_USER_NAME = "user_name";
  public static final String COLUMN_DEPT = "dept";
  public static final String COLUMN_BLOOD_GROUP = "blood_group";
  public static final String COLUMN_HALL = "hall";
  public static final String COLUMN_GENDER = "gender";
  public static final String COLUMN_ROOM_NO = "room_no";
  public static final String COLUMN_ADDRESS = "address";
  public static final String COLUMN_PROGRAMME = "programme";
  public static final String COLUMN_YEAR = "year";
  private static final String CREATE_TABLE_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( "
          + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
          + COLUMN_NAME + " TEXT, "
          + COLUMN_ROLL_NO + " TEXT, "
          + COLUMN_HALL + " TEXT, "
          + COLUMN_GENDER + " TEXT, "
          + COLUMN_DEPT + " TEXT, "
          + COLUMN_ROOM_NO + " TEXT, "
          + COLUMN_ADDRESS + " TEXT, "
          + COLUMN_BLOOD_GROUP + " TEXT, "
          + COLUMN_PROGRAMME + " TEXT, "
          + COLUMN_USER_NAME + " TEXT, "
          + COLUMN_YEAR + " TEXT);";
  private static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS " + TABLE_NAME;

  private DbHelper(Context context, String dbName, Integer version) {
    super(context, dbName, null, version);
  }

  public static DbHelper getDbHelperInstance(Context context, String dbName, Integer version) {
    if (dbHelper == null)
      dbHelper = new DbHelper(context, dbName, version);
    return dbHelper;
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    try {
      db.execSQL(CREATE_TABLE_IF_NOT_EXISTS);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL(DROP_TABLE_IF_EXISTS);
  }


  public void insertStudents(StudentData[] students) {
    try {
      SQLiteDatabase db = this.getWritableDatabase();
      db.beginTransaction();
      db.delete(TABLE_NAME, null, null);
      for (StudentData student : students) {
        //reinitialize student to initalize "year"
        student = new StudentData(
                student.getAddress(),
                student.getBloodGroup(),
                student.getDept(),
                student.getGender(),
                student.getHall(),
                student.getRollNo(),
                student.getName(),
                student.getProgramme(),
                student.getRoomNo(),
                student.getUserName()
        );
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ADDRESS, student.getAddress());
        contentValues.put(COLUMN_BLOOD_GROUP, student.getBloodGroup());
        contentValues.put(COLUMN_DEPT, student.getDept());
        contentValues.put(COLUMN_GENDER, student.getGender());
        contentValues.put(COLUMN_HALL, student.getHall());
        contentValues.put(COLUMN_NAME, student.getName());
        contentValues.put(COLUMN_PROGRAMME, student.getProgramme());
        contentValues.put(COLUMN_ROLL_NO, student.getRollNo());
        contentValues.put(COLUMN_ROOM_NO, student.getRoomNo());
        contentValues.put(COLUMN_USER_NAME, student.getUserName());
        contentValues.put(COLUMN_YEAR, student.getYear());
        db.insert(TABLE_NAME, null, contentValues);
      }
      db.setTransactionSuccessful();
      db.endTransaction();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
}