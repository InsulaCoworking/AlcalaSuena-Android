package com.triskelapps.alcalasuena.messaging;

/**
 * Created by julio on 31/05/17.
 */


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.util.Patterns;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.triskelapps.alcalasuena.App;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.model.News;
import com.triskelapps.alcalasuena.model.notification.FirebasePush;
import com.triskelapps.alcalasuena.ui.MainActivity;
import com.triskelapps.alcalasuena.util.NotificationHelper;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String KEY_CUSTOM_BUTTON_TEXT = "btn-text";
    private static final String KEY_CUSTOM_BUTTON_LINK = "btn-link";


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        handleNow(remoteMessage);

        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
        // [START dispatch_job]
//        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
//        Job myJob = dispatcher.newJobBuilder()
//                .setService(MyJobService.class)
//                .setTag("my-job-tag")
//                .build();
//        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     *
     * @param remoteMessage
     */
    private void handleNow(RemoteMessage remoteMessage) {

        String title;
        String message;
        String idNews;

        Map<String, String> notifData = remoteMessage.getData();

        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            message = remoteMessage.getNotification().getBody();
            idNews = null;
        } else {
            title = notifData.get(FirebasePush.NOTIFICATION_TITLE);
            message = notifData.get(FirebasePush.NOTIFICATION_MESSAGE);
            idNews = notifData.get(FirebasePush.NOTIFICATION_ID_NEWS);
        }

        Bundle extras = new Bundle();

        for (Map.Entry<String, String> entry : notifData.entrySet()) {
            extras.putString(entry.getKey(), entry.getValue());
        }

//        Intent intent = new Intent(App.ACTION_SHOW_NOTIFICATION);
//        intent.putExtras(extras);
//        getApplicationContext().sendBroadcast(intent);


        if (extras.containsKey(FirebasePush.NOTIFICATION_NEWS)) {
            saveNews(extras);
        }

        sendNotification(title, message, idNews, extras);

    }

    private void saveNews(Bundle extras) {
        if (extras.containsKey(FirebasePush.NOTIFICATION_NEWS)) {
            String newsJson = extras.getString(FirebasePush.NOTIFICATION_NEWS);
            final News news = new Gson().fromJson(newsJson, News.class);
            if (news != null) {
                news.configureDatesTime();
                App.getDB().newsDao().insert(news);
            }
        }
    }

    private void sendNotification(String title, String text, String idNews, Bundle extras) {

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!notificationManager.areNotificationsEnabled()) {
                return;
            }
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(App.ACTION_SHOW_NOTIFICATION);
        intent.putExtras(extras);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NotificationHelper.CHANNEL_NOVELTIES_PUSH)
                .setSmallIcon(R.mipmap.img_logo_alcalasuena_notif)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        idNews != null ? R.mipmap.ic_notif_news : R.mipmap.ic_notif_announcement))
                .setContentTitle(title != null ? title : getString(R.string.app_name))
                .setContentText(text)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text))
                .setContentIntent(pendingIntent);

        String btnText = extras.getString(FirebasePush.NOTIFICATION_CUSTOM_BUTTON_TEXT);
        String btnLink = extras.getString(FirebasePush.NOTIFICATION_CUSTOM_BUTTON_LINK);
        if (btnLink != null && Patterns.WEB_URL.matcher(btnLink).matches()) {
            PendingIntent pIntentLink = getLinkPendingIntent(extras);
            notificationBuilder.addAction(R.mipmap.ic_link_web, btnText, pIntentLink);
        } else {
            extras.remove(FirebasePush.NOTIFICATION_CUSTOM_BUTTON_LINK);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private PendingIntent getLinkPendingIntent(Bundle extras) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(extras);
        intent.putExtra(FirebasePush.EXTRA_OPEN_URL_LINK, true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        return pendingIntent;
    }

}
