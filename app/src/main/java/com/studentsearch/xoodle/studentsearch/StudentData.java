package com.studentsearch.xoodle.studentsearch;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by aryan on 10/6/17.
 */

public class StudentData implements Parcelable {

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

  public static final Parcelable.Creator<StudentData> CREATOR = new Parcelable.Creator<StudentData>() {
    public StudentData createFromParcel(Parcel in) {
      return new StudentData(in);
    }

    public StudentData[] newArray(int size) {
      return new StudentData[size];
    }
  };

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

  private StudentData(Parcel in) {
    // This order must match the order in writeToParcel()
    address = in.readString();
    bloodGroup = in.readString();
    dept = in.readString();
    gender = in.readString();
    hall = in.readString();
    rollNo = in.readString();
    name = in.readString();
    programme = in.readString();
    roomNo = in.readString();
    userName = in.readString();
  }

  public void writeToParcel(Parcel out, int flags) {
    // Again this order must match the Question(Parcel) constructor
    out.writeString(address);
    out.writeString(bloodGroup);
    out.writeString(dept);
    out.writeString(gender);
    out.writeString(hall);
    out.writeString(rollNo);
    out.writeString(name);
    out.writeString(programme);
    out.writeString(roomNo);
    out.writeString(userName);
  }

  public int describeContents() {
    return 0;
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