package com.example.kaushal.studentsearch;

import android.graphics.Movie;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by aryan on 10/6/17.
 */

public class StudentData {

  @SerializedName("a")
  public String a;

  @SerializedName("b")
  public String b;

  @SerializedName("d")
  public String d;

  @SerializedName("g")
  public String g;

  @SerializedName("h")
  public String h;

  @SerializedName("i")
  public String i;

  @SerializedName("n")
  public String n;

  @SerializedName("p")
  public String p;

  @SerializedName("r")
  public String r;

  @SerializedName("u")
  public String u;

//  public StudentData(String place, String blood, String branch, String gender, String hall, String rollno, String name, String programme, String room, String mail) {
//    this.a = place;
//    this.b = blood;
//    this.d = branch;
//    this.g = gender;
//    this.h = hall;
//    this.i = rollno;
//    this.n = name;
//    this.p = programme;
//    this.r = room;
//    this.u = mail;
//  }
  public String geta() {
    return a;
  }

  public String getb() {
    return b;
  }

  public String getd() {
    return d;
  }

  public String getg() {
    return g;
  }

  public String geth() {
    return h;
  }

  public String geti() {
    return i;
  }

  public String getn() {
    return n;
  }

  public String getp() {
    return p;
  }

  public String getr() {
    return r;
  }

  public String getu() {
    return u;
  }
}
