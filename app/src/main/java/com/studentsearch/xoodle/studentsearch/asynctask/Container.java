package com.studentsearch.xoodle.studentsearch.asynctask;

/** Classes that use FetchData asynctask should implement this interface */
public interface Container {
  void finish();

  void setFilterSpinnerEntries();
}
