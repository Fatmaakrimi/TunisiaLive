package com.example.tunisialive;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "FCM_CHANNEL";

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("FCM", "Token: " + token);
        // You can send this token to your server if needed
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String title = "";
        String message = "";

        // Check for data payload
        if (remoteMessage.getData().size() > 0) {
            title = remoteMessage.getData().get("title");
            message = remoteMessage.getData().get("message");
        }

        // Optional: check for notification payload (in case it's used)
        if (remoteMessage.getNotification() != null) {
            if (title == null || title.isEmpty()) {
                title = remoteMessage.getNotification().getTitle();
            }
            if (message == null || message.isEmpty()) {
                message = remoteMessage.getNotification().getBody();
            }
        }

        showNotification(title, message);
    }

    private void showNotification(String title, String message) {
        // Create a notification channel for Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "FCM Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification) // Make sure this icon exists
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(0, builder.build());
        }
    }
}
