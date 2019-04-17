package com.triskelapps.alcalasuena.util;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.triskelapps.alcalasuena.R;


public class NotificationHelper {

    public static final String CHANNEL_NOVELTIES_PUSH = "channel_novelties_push";
    public static final int NOTIF_ID_MAIN = 0;

    private final Context context;
    private final NotificationManager notificationManager;

    private NotificationHelper(Context context) {
        this.context = context;
        notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static NotificationHelper with(Context context) {
        return new NotificationHelper(context);
    }


    public void initializeOreoChannelsNotification() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            createChannel(CHANNEL_NOVELTIES_PUSH,
                    R.string.channel_novelties_push_name,
                    R.string.channel_novelties_push_description);

        }

    }

    @SuppressLint("NewApi")
    private void createChannel(String channelId, int nameStringId, int descriptionStringId) {

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

        if (notificationManager.getNotificationChannel(channelId) == null) {
            CharSequence name = context.getString(nameStringId);
            String description = context.getString(descriptionStringId);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void cancel(int notifId) {
        notificationManager.cancel(notifId);
    }

}
