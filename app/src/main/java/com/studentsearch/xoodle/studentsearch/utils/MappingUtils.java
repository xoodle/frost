package com.studentsearch.xoodle.studentsearch.utils;

import java.util.HashMap;
import java.util.Map;

/** Mapping from displayed to actual value associated with the filter spinners */

public class MappingUtils {

  private static  MappingUtils  THE_INSTANCE = null;

  private Map<String, String> hallMap;
  private Map<String, String> deptMap;
  private Map<String, String> deptAbbrevMap;
  private Map<String, String> bloodGroupMap;
  private Map<String, String> genderMap;
  private Map<String, String> programmeMap;
  private Map<String, String> yearMap;

  private MappingUtils() {
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
    deptMap.put("Physics", "Physics");
    deptMap.put("Materials Science & Engg.", "Material Science");
    deptMap.put("Computer Science & Engg.", "Computer Science");
    deptMap.put("Civil Engg.", "Civil");
    deptMap.put("Electrical Engg.", "Electrical");
    deptMap.put("Aerospace Engg.", "Aerospace");
    deptMap.put("Chemical Engg.", "Chemical");
    deptMap.put("Economics", "Economics");
    deptMap.put("Mechanical Engineering", "Mechanical");
    deptMap.put("Chemistry", "Chemistry");
    deptMap.put("Biol.Sci. And Bio.Engg.", "BSBE");
    deptMap.put("Earth Sciences","Earth Sciences");
    deptMap.put("Mathematics", "Mathematics");

      deptAbbrevMap = new HashMap<>();
      deptAbbrevMap.put("", "Departments");
      deptAbbrevMap.put("Physics", "PHY");
      deptAbbrevMap.put("Materials Science & Engg.", "MSE");
      deptAbbrevMap.put("Computer Science & Engg.", "CSE");
      deptAbbrevMap.put("Civil Engg.", "CE");
      deptAbbrevMap.put("Electrical Engg.", "EE");
      deptAbbrevMap.put("Aerospace Engg.", "AE");
      deptAbbrevMap.put("Chemical Engg.", "CHE");
      deptAbbrevMap.put("Economics", "ECO");
      deptAbbrevMap.put("Mechanical Engineering", "ME");
      deptAbbrevMap.put("Chemistry", "CHM");
      deptAbbrevMap.put("Biol.Sci. And Bio.Engg.", "BSBE");
      deptAbbrevMap.put("Earth Sciences","ES");
      deptAbbrevMap.put("Mathematics", "MTH");

      bloodGroupMap = new HashMap<>();
    bloodGroupMap.put("", "Blood Group");

    genderMap = new HashMap<>();
    genderMap.put("", "Gender");

    programmeMap = new HashMap<>();
    programmeMap.put("", "Programme");

    yearMap = new HashMap<>();
    yearMap.put("", "Year");
    yearMap.put("Y10", "Y10");
    yearMap.put("Y11", "Y11");
    yearMap.put("Y12", "Y12");
    yearMap.put("Y13", "Y13");
    yearMap.put("Y14", "Y14");
    yearMap.put("Y15", "Y15");
    yearMap.put("Y16", "Y16");
    yearMap.put("Y17", "Y17");
  }

  public Map<String, String> getHallMap() {
    return hallMap;
  }

  public Map<String, String> getDeptMap() {
    return deptMap;
  }

    public Map<String, String> getDeptAbbrevMap() {
        return deptAbbrevMap;
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

  public static MappingUtils getInstance() {
    if(THE_INSTANCE == null) THE_INSTANCE = new MappingUtils();
    return THE_INSTANCE;
  }
}
