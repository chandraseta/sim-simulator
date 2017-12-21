package com.thesim.simsimulator;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.thesim.simsimulator.model.User;
import com.thesim.simsimulator.utils.Helper;

import java.net.HttpURLConnection;
import java.util.Locale;

/**
 * ResultActivity handles user result after a test.
 */
public class ResultActivity extends Activity {

  private NotificationReceiver notifyReceiver;
  private NotificationManager notifyManager;

  static final int NOTIFICATION_ID = 0;
  private static final String ACTION_UPDATE_NOTIFICATION = "com.rionaldichandraseta.notifyme.ACTION_UPDATE_NOTIFICATION";
  private static final String ACTION_CANCEL_NOTIFICATION = "com.rionaldichandraseta.notifyme.ACTION_CANCEL_NOTIFICATION";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_result);

    Log.d("PostScore", "onCreate");

    Intent intent = getIntent();
    String percentageString = intent.getStringExtra("correctPercentage");
    double percentage = Double.parseDouble(percentageString);

    notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    notifyReceiver = new NotificationReceiver();
    registerReceiver(notifyReceiver, new IntentFilter(ACTION_UPDATE_NOTIFICATION));

    if (percentage >= 0.7) {
      Intent notificationIntent = new Intent(this, MainActivity.class);
      PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

      String successMessage = String.format(Locale.ENGLISH, "You answered %.2f percent of all questions correctly.", percentage * 100);
      NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this)
          .setContentTitle("You passed your last test")
          .setContentText(successMessage)
          .setContentIntent(notificationPendingIntent)
          .setPriority(NotificationCompat.PRIORITY_HIGH)
          .setDefaults(NotificationCompat.DEFAULT_ALL)
          .setSmallIcon(R.drawable.app_logo_simple);

      notifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());

    } else {
      Intent notificationIntent = new Intent(this, MainActivity.class);
      PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

      String failureMessage = String.format(Locale.ENGLISH, "You answered %.2f percent of all questions correctly.", percentage * 100);
      NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this)
          .setContentTitle("You failed your last test")
          .setContentText(failureMessage)
          .setContentIntent(notificationPendingIntent)
          .setPriority(NotificationCompat.PRIORITY_HIGH)
          .setDefaults(NotificationCompat.DEFAULT_ALL)
          .setSmallIcon(R.drawable.app_logo_simple);

      notifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

    final String email = User.getInstance().getEmail();
    int score = (int) (percentage * 100);
    Log.d("PostScore", Integer.toString(score));

    PostToServer post = new PostToServer(email, score);
    post.execute();

    Intent mainIntent = new Intent(this, MainActivity.class);
    startActivity(mainIntent);
  }

  @Override
  protected void onDestroy() {
    unregisterReceiver(notifyReceiver);
    super.onDestroy();
  }

  public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();
      switch (action) {
        case ACTION_CANCEL_NOTIFICATION:
          break;
        case ACTION_UPDATE_NOTIFICATION:
          break;
      }
    }
  }

  private class PostToServer extends AsyncTask<String, HttpURLConnection, String> {

    String email;
    int score;
    String result;

    PostToServer(String email, int score) {
      this.email = email;
      this.score = score;
    }

    @Override
    protected String doInBackground(String... strings) {
      result =  Helper.postScore(email, score);
      return result;
    }

    @Override
    protected void onPostExecute(String s) {
      super.onPostExecute(s);
      if (result != null) {
        Log.d("PostScore", result);
      }
      else {
        Log.d("PostScore", "PostScore returns null");
      }
    }
  }
}
