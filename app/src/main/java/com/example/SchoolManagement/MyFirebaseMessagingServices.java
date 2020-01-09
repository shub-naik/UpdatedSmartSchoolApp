package com.example.SchoolManagement;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingServices extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            NotificationHelper.DisplayNotification(MyFirebaseMessagingServices.this, title, body);

        } else {
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("message");

            NotificationHelper.DisplayNotification(MyFirebaseMessagingServices.this, title, body);
        }
    }
}
