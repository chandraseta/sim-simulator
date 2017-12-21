package com.thesim.simsimulator;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * NotificationService handles messaging including notification from Firebase
 */
public class NotificationService extends FirebaseMessagingService {
  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    notify(remoteMessage.getNotification().getBody());
  }

  public void notify(String message) {
    Intent resultIntent = new Intent(this, MainActivity.class);

    TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
    taskStackBuilder.addParentStack(MainActivity.class);
    taskStackBuilder.addNextIntent(resultIntent);

    PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

    NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder)
        new NotificationCompat.Builder(this)
            .setContentTitle("SIM Simulator")
            .setContentText(message)
            .setSmallIcon(R.drawable.app_logo_simple)
            .setContentIntent(resultPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true);

    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.notify(0, notificationBuilder.build());
  }
}
