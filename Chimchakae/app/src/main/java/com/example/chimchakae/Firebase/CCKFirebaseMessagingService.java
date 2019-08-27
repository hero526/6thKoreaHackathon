package com.example.chimchakae.Firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class CCKFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "CCK_FCM_Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO: Handling the FCM in here

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Messgae data payload: " + remoteMessage.getData());

        } else {
            handleNow();
        }
    }

    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }
}
