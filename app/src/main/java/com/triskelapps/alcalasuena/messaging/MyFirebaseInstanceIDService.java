package com.triskelapps.alcalasuena.messaging;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.triskelapps.alcalasuena.App;

/**
 * Created by julio on 31/05/17.
 *
 * QUICK START MESSAGING: https://github.com/firebase/quickstart-android/tree/master/messaging
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "FirebaseTokenService";

    @Override
    public void onTokenRefresh() {
//        super.onTokenRefresh();

        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

//        sendRegistrationToServer(refreshedToken);

        if (FirebaseInstanceId.getInstance().getToken() != null
                && !App.getPrefs(this).getBoolean(App.SHARED_SUBSCRIBED_NEWS_NOTIFS, false)) {
            FirebaseMessaging.getInstance().subscribeToTopic("news");
            App.getPrefs(this).edit().putBoolean(App.SHARED_SUBSCRIBED_NEWS_NOTIFS, true).commit();
        }

    }
}
