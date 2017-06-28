package com.studentsearch.xoodle.studentsearch.retrofit;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by xoodle on 15/6/17.
 */

public interface RequestInterface {
  @GET("api")
  Call<JsonArray> getData();
}
