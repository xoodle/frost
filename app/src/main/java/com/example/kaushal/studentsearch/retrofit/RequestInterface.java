package com.example.kaushal.studentsearch.retrofit;

import com.example.kaushal.studentsearch.StudentData;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by kaushal on 15/6/17.
 */

public interface RequestInterface {
  @GET("api")
  Call<JsonArray> getData();
}
