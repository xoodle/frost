package com.example.kaushal.studentsearch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.example.kaushal.studentsearch.retrofit.JSONResponse;
import com.example.kaushal.studentsearch.retrofit.RequestInterface;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SearchResultActivity extends MainActivity {

  public TextView JsonText;
  public ProgressDialog pd;
  public String jsonString;
  private RecyclerView mResultRecyclerView;
  private DataAdapter mDataAdapter;
  private ArrayList<StudentData> data;
  LinearLayoutManager mLinearLayoutManager;

  public static Intent getNewIntent(Context c) {
    return new Intent(c, SearchResultActivity.class);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_result);
    ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    //JsonText = (TextView) findViewById(R.id.tvJsontext);
    mResultRecyclerView = (RecyclerView) findViewById(R.id.rv_main);
    mLinearLayoutManager = new LinearLayoutManager(this);
    mResultRecyclerView.setLayoutManager(mLinearLayoutManager);
    data = new ArrayList<>();
    //loadJson();
    new JsonTask().execute("https://yashsriv.org/api");

  }


  // get action bar
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.meu_search_activity, menu);
    return super.onCreateOptionsMenu(menu);
  }

//  public void loadJson() {
//    Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl("https://yashsriv.org/")
//            .addConverterFactory(GsonConverterFactory.create())
//            //.client(getUnsafeOkHttpClient())
//            .build();
//    RequestInterface requestInterface = retrofit.create(RequestInterface.class);
//    Call<String> call = requestInterface.getData();
//    call.enqueue(new Callback<String>() {
//      @Override
//      public void onResponse(Call<String> call, Response<String> response) {
//        Gson gson = new Gson();
//        //JsonArray jsonResponse = response.body();
//        //JsonText.setText(jsonResponse.toString());
//        //data = new ArrayList<>(Arrays.asList(jsonResponse));
//        Matcher m = Pattern.compile("\\{(.*?)\\}").matcher(response.toString());
//        //data.add(gson.fromJson(response.toString(), StudentData.class));
//        while(m.find()){
//          data.add(gson.fromJson("{"+m.group(1)+"}", StudentData.class));
//        }
//        mDataAdapter = new DataAdapter(data);
//        mDataAdapter.notifyItemInserted(data.size());
//        mResultRecyclerView.setAdapter(mDataAdapter);
//
////        JsonArray jsonArray = null;
////        try {
////          jsonArray = jsonResponse.getAsJsonArray();
////        } catch (Exception e) {
////          e.printStackTrace();
////        }
////        JsonObject jsonObject;
////        try {
////            for(int i=0; i<jsonArray.size(); i++) {
////              jsonObject = jsonArray.getAsJsonObject();
////              data.add(gson.fromJson(jsonObject.toString(), StudentData.class));
////            }
////
////          } catch(Exception e) {
////            e.printStackTrace();
////          }
//
//      }
//
//      @Override
//      public void onFailure(Call<String> call, Throwable t) {
//        //JsonText.setText(t.toString());
//        t.printStackTrace();
//      }
//    });
//  }

  private static OkHttpClient getUnsafeOkHttpClient() {
    try {
      // Create a trust manager that does not validate certificate chains
      final TrustManager[] trustAllCerts = new TrustManager[] {
              new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                  return new java.security.cert.X509Certificate[]{};
                }
              }
      };

      // Install the all-trusting trust manager
      final SSLContext sslContext = SSLContext.getInstance("SSL");
      sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
      // Create an ssl socket factory with our all-trusting manager
      final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

      OkHttpClient.Builder builder = new OkHttpClient.Builder();
      builder.sslSocketFactory(sslSocketFactory);
      builder.hostnameVerifier(new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
          return true;
        }
      });

      OkHttpClient okHttpClient = builder.build();
      return okHttpClient;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public class JsonTask extends AsyncTask<String, String, String> {
    protected void onPreExecute() {
      super.onPreExecute();
      pd = new ProgressDialog(SearchResultActivity.this);
      pd.setMessage("Please wait");
      pd.setCancelable(false);
      pd.show();
    }

    protected String doInBackground(String... params) {


      HttpURLConnection connection = null;
      BufferedReader reader = null;

      try {
        URL url = new URL(params[0]);
        connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        InputStream stream = connection.getInputStream();
        reader = new BufferedReader(new InputStreamReader(stream));

        StringBuffer buffer = new StringBuffer();
        String line = "";

        while ((line = reader.readLine()) != null) {
          buffer.append(line + "\n");
          Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
        }
        return buffer.toString();
      } catch (MalformedURLException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        if (connection != null) {
          connection.disconnect();
        }
        try {
          if (reader != null) {
            reader.close();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      return null;
    }

    @Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);
      if (pd.isShowing()) {
        pd.dismiss();
      }
      Gson gson = new Gson();

      Matcher m = Pattern.compile("\\{(.*?)\\}").matcher(result);
      //if(m.find())
        //JsonText.setText(m.group(2));
      while(m.find()){
//      for(int i=0;i<10;i++){
        data.add(gson.fromJson("{"+m.group(1)+"}", StudentData.class));
        //JsonText.setText(m.group(1));
      }
      //JsonText.setText(data.get(5).getn());
      jsonString = result;
      mDataAdapter = new DataAdapter(data);
      mDataAdapter.notifyItemInserted(data.size());
      mResultRecyclerView.setAdapter(mDataAdapter);
    }
  }
}

