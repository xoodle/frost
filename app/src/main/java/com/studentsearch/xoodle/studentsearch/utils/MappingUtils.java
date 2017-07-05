package com.studentsearch.xoodle.studentsearch.utils;

import java.util.HashMap;
import java.util.Map;

/** Mapping from displayed to actual value associated with the filter spinners */

public class MappingUtils {
  private Map<String, String> hallMap;
  private Map<String, String> deptMap;
  private Map<String, String> bloodGroupMap;
  private Map<String, String> genderMap;
  private Map<String, String> programmeMap;
  private Map<String, String> yearMap;

  public MappingUtils() {
    hallMap = new HashMap<>();
    hallMap.put("", "Halls");
    hallMap.put("HALL1", "Hall 1");
    hallMap.put("HALL2", "Hall 2");
    hallMap.put("HALL3", "Hall 3");
    hallMap.put("HALL4", "Hall 4");
    hallMap.put("HALL5", "Hall 5");
    hallMap.put("HALL6", "Hall 6");
    hallMap.put("HALL7", "Hall 7");
    hallMap.put("HALL8", "Hall 8");
    hallMap.put("HALL9", "Hall 9");
    hallMap.put("HALLX", "Hall 10");
    hallMap.put("HALLXI", "Hall 11");
    hallMap.put("HALL12", "Hall 12");
    hallMap.put("HALL13", "Hall 13");

    deptMap = new HashMap<>();
    deptMap.put("", "Departments");
    deptMap.put("Physics", "PHY");
    deptMap.put("Materials Science & Engg.", "MSE");
    deptMap.put("Computer Science & Engg.", "CSE");
    deptMap.put("Civil Engg.", "CE");
    deptMap.put("Electrical Engg.", "EE");
    deptMap.put("Humanities & Soc. Sciences", "HSS");
    deptMap.put("Aerospace Engg.", "AE");
    deptMap.put("Chemical Engg.", "CHE");
    deptMap.put("Economics", "ECO");
    deptMap.put("Mechanical Engineering", "ME");
    deptMap.put("Chemistry", "CHY");
    deptMap.put("Math For Pg Online", "MTH");
    deptMap.put("Biol.Sci. And Bio.Engg.", "BSBE");
    deptMap.put("Earth Sciences","ES");

    bloodGroupMap = new HashMap<>();
    bloodGroupMap.put("", "Blood Group");

    genderMap = new HashMap<>();
    genderMap.put("", "Gender");

    programmeMap = new HashMap<>();
    programmeMap.put("", "Programme");

    yearMap = new HashMap<>();
    yearMap.put("", "Year");
  }

  public Map<String, String> getHallMap() {
    return hallMap;
  }

  public Map<String, String> getDeptMap() {
    return deptMap;
  }

  public Map<String, String> getBloodGroupMap() {
    return bloodGroupMap;
  }

  public Map<String, String> getGenderMap() {
    return genderMap;
  }

  public Map<String, String> getProgrammeMap() {
    return programmeMap;
  }

  public Map<String, String> getYearMap() {
    return yearMap;
  }
}
