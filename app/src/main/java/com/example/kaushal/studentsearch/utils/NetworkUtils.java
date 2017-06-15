package com.example.kaushal.studentsearch.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/** for network related work */

public class NetworkUtils {

    final static String BASE_URL = "https://yashsriv.org/api";
    final static String PARAM_QUERY = "q";
    final static String PARAM_SORT = "sort";

    public static URL buildUrl() {
        Uri builtUri = Uri.parse(BASE_URL)
                .buildUpon()
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            if(scanner.hasNext())
                return scanner.next();
            else
                return null;
        } finally {
            urlConnection.disconnect();
        }
    }
}
