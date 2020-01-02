package com.example.SchoolManagement;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseInstanceIdService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NewTokenGenerated: ", s);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("TokenGenerated", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("Token", s); // Storing string

        editor.commit(); // commit changes
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            NotificationHelper.DisplayNotification(MyFirebaseInstanceIdService.this, title, body);

        } else {

            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("message");

            NotificationHelper.DisplayNotification(MyFirebaseInstanceIdService.this, title, body);
        }
    }
}
