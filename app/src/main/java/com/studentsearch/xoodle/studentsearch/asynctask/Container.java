package com.studentsearch.xoodle.studentsearch.asynctask;

import android.content.Intent;

/** Classes that use FetchData asynctask should implement this interface */
public interface Container {
  void finish();

  void setFilterSpinnerEntries();

  Intent getIntent();
}
