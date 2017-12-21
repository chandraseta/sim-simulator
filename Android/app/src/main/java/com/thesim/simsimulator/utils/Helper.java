package com.thesim.simsimulator.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.lang.String.valueOf;

/**
 * Class to initiate HTTP connection, check connection
 */
public class Helper {

  private static final String LOG_TAG = Helper.class.getSimpleName();
  private static final String QUESTIONS_URL =
      "http://sim-simulator-server.herokuapp.com/api/questions/random/";
  private static final String REGISTER_URL =
      "http://sim-simulator-server.herokuapp.com/api/users/register";
  private static final String SCORE_URL =
          "http://sim-simulator-server.herokuapp.com/api/users/post";

  public static String getQuestions(int size) {
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String bookJSONString = null;

    try {
      Uri uri = Uri.parse(QUESTIONS_URL + size).buildUpon().build();
      URL requestURL = new URL(uri.toString());

      urlConnection = (HttpURLConnection) requestURL.openConnection();
      urlConnection.setRequestMethod("GET");
      urlConnection.connect();

      InputStream inputStream = urlConnection.getInputStream();
      StringBuilder builder = new StringBuilder();
      if (inputStream == null) {
        // Nothing to do.
        return null;
      }
      reader = new BufferedReader(new InputStreamReader(inputStream));
      String line;
      while ((line = reader.readLine()) != null) {
            /* Since it's JSON, adding a newline isn't necessary (it won't affect
              parsing) but it does make debugging a *lot* easier if you print out the
              completed buffer for debugging. */
        builder.append(line).append("\n");
      }
      if (builder.length() == 0) {
        // Stream was empty.  No point in parsing.
        return null;
      }
      bookJSONString = builder.toString();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } finally {
      if (urlConnection != null) {
        urlConnection.disconnect();
      }
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return bookJSONString;
  }

  public static String register(String name, String email, String password) {
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String result = null;

    try {
      URL url = new URL(REGISTER_URL);
      urlConnection = (HttpURLConnection) url.openConnection();
      urlConnection.setRequestMethod("POST");
      urlConnection.setRequestProperty("Content-Type", "application/json");

      String query = new JSONObject()
              .put("name", name)
              .put("email", email)
              .put("password", password).toString();

      Log.d("SignUpFragment: QUERY", query);

      OutputStream os = urlConnection.getOutputStream();
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
      writer.write(query);
      writer.flush();
      writer.close();
      os.close();

      urlConnection.connect();

      int status = urlConnection.getResponseCode();
      result = valueOf(status);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      if (urlConnection != null) {
        urlConnection.disconnect();
      }
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return result;
  }

  public static boolean isNetworkAvailable(Context context) {
    ConnectivityManager connectivityManager
        = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }

  public static boolean checkLocationPermission(final Context context) {
    return ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
  }

  public static String postScore(String email, int score) {
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String result = null;

    try {
      URL url = new URL(SCORE_URL);
      urlConnection = (HttpURLConnection) url.openConnection();
      urlConnection.setRequestMethod("POST");
      urlConnection.setRequestProperty("Content-Type", "application/json");

      String query = new JSONObject()
              .put("email", email)
              .put("theory_score", score).toString();

      Log.d("PostScore: QUERY", query);

      OutputStream os = urlConnection.getOutputStream();
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
      writer.write(query);
      writer.flush();
      writer.close();
      os.close();

      urlConnection.connect();

      int status = urlConnection.getResponseCode();
      result = valueOf(status);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      if (urlConnection != null) {
        urlConnection.disconnect();
      }
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return result;
  }
}