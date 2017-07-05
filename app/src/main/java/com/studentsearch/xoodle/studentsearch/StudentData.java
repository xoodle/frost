package com.studentsearch.xoodle.studentsearch;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by aryan on 10/6/17.
 */

public class StudentData {

  @SerializedName("a")
  public String address;

  @SerializedName("b")
  public String bloodGroup;

  @SerializedName("d")
  public String dept;

  @SerializedName("g")
  public String gender;

  @SerializedName("h")
  public String hall;

  @SerializedName("i")
  public String rollNo;

  @SerializedName("n")
  public String name;

  @SerializedName("p")
  public String programme;

  @SerializedName("r")
  public String roomNo;

  @SerializedName("u")
  public String userName;

  public String year;

  public static ArrayList<StudentData> studentDataArrayList;

  public StudentData(String address, String bloodGroup, String dept, String gender, String hall, String rollNo, String name, String programme, String roomNo, String userName) {
    this.address = address;
    this.bloodGroup = bloodGroup;
    this.dept = dept;
    this.gender = gender;
    this.hall = hall;
    this.rollNo = rollNo;
    this.name = name;
    this.programme = programme;
    this.roomNo = roomNo;
    this.userName = userName;
    setYear();
  }

  public String getAddress() {
    return address;
  }

  public String getBloodGroup() {
    return bloodGroup;
  }

  public String getDept() {
    return dept;
  }

  public String getGender() {
    return gender;
  }

  public String getHall() {
    return hall;
  }

  public String getRollNo() {
    return rollNo;
  }

  public String getName() {
    return name;
  }

  public String getProgramme() {
    return programme;
  }

  public String getRoomNo() {
    return roomNo;
  }

  public String getUserName() {
    return userName;
  }

  public String getYear() {
    return year;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setBloodGroup(String bloodGroup) {
    this.bloodGroup = bloodGroup;
  }

  public void setDept(String dept) {
    this.dept = dept;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public void setHall(String hall) {
    this.hall = hall;
  }

  public void setRollNo(String rollNo) {
    this.rollNo = rollNo;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setProgramme(String programme) {
    this.programme = programme;
  }

  public void setRoomNo(String roomNo) {
    this.roomNo = roomNo;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public void setYear() {
    year="Y" + getRollNo().substring(0,2);
  }
}