package com.sportskonnect.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.RemoteMessage;
import com.sportskonnect.Activity_Badminton;
import com.sportskonnect.R;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();
            Log.e("Trying lol",  message);

            Intent intent = new Intent(this, Activity_Badminton.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Notification.Builder notification = new Notification.Builder(this);
            notification.setContentTitle(title);
            notification.setContentText(message);
            notification.setSmallIcon(R.mipmap.ic_launcher);
            notification.setContentIntent(pendingIntent);
            notification.setStyle(new Notification.BigTextStyle().bigText(message));
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification.build());
        } else {
            Log.e("Error", "Error");
        }

    }

}
