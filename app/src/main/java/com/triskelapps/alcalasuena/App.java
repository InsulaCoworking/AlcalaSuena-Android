package com.triskelapps.alcalasuena;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import androidx.room.Room;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.triskelapps.alcalasuena.database.AppDatabase;
import com.triskelapps.alcalasuena.util.NotificationHelper;
import com.triskelapps.simpleappupdate.SimpleAppUpdate;
import com.triskelapps.simpleappupdate.config.NotificationStyle;
import com.triskelapps.simpleappupdate.config.WorkerConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by julio on 17/06/16.
 */

public class App extends MultiDexApplication {

    private static final String TAG = App.class.getSimpleName();

    public static final int FEST_YEAR = 2024;
    public static final int CACHED_DATA_VERSION = 1;
    public static final int INIT_DATA_VERSION = 1;

    public static List<String> festDates = new ArrayList<>();

    static {
        festDates.add("2024-06-07");
        festDates.add("2024-06-08");
        festDates.add("2024-06-09");
    }

    public static final String WEB_URL = "https://alcalasuena.es/";

    public static final String PREFIX = BuildConfig.APPLICATION_ID + ".";

    public static final String SHARED_CURRENT_DATA_VERSION = PREFIX + "shared_current_data_version_" + FEST_YEAR;
    public static final String SHARED_FIRST_TIME_APP_LAUNCHING = PREFIX + "extra_first_time_app_lauching_" + FEST_YEAR;
    public static final String SHARED_PIN_SEND_NEWS_ENCRIPT = PREFIX + "shared_pin_send_news_encript";

    public static final String ACTION_REFRESH_DATA = PREFIX + "action_refresh_data";
    public static final String ACTION_SHOW_NOTIFICATION = PREFIX + "action_show_notification";

    public static final String URL_GOOGLE_PLAY_APP = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
    public static final String URL_APPLE_STORE_APP = "https://itunes.apple.com/es/app/Alcala-suena/id1458551516";
    public static final String URL_DIRECT_GOOGLE_PLAY_APP = "market://details?id=" + BuildConfig.APPLICATION_ID;
    public static final String TOPIC_NEWS = "news";
    public static final String TOPIC_NEWS_TEST = "news_test";


    private static AppDatabase db;
    private static final String DB_NAME = "app_db.sqlite";

    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DB_NAME)
//                .addMigrations(DBMigrationBase.MIGRATION_5_6)
//                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(DebugHelper.SWITCH_CRASH_REPORT_ENABLED);

        NotificationHelper.with(this).initializeOreoChannelsNotification();

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

        // Periodic app update configuration
        new SimpleAppUpdate(this).cancelWork("appUpdateCheckWork");
        NotificationStyle notificationStyle = new NotificationStyle(R.mipmap.img_logo_alcalasuena_notif, R.color.red);
        WorkerConfig workerConfig = new WorkerConfig(2, TimeUnit.HOURS, 30, TimeUnit.MINUTES);
        SimpleAppUpdate.schedulePeriodicChecks(this, BuildConfig.VERSION_CODE, notificationStyle, workerConfig);


//        updateDataFromApi();

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Refreshed token: " + task.getResult());
            } else {
                Log.d(TAG, "Refreshed token: ERROR\n" + task.getException().getMessage());
            }
        });


        if (BuildConfig.DEBUG) {
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_NEWS_TEST);
            FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC_NEWS);
        } else {
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_NEWS);
        }

    }


    public static AppDatabase getDB() {
        return db;
    }


    public static void openAppInGooglePlay(Context context) {
        Intent directPlayIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_DIRECT_GOOGLE_PLAY_APP.replace(".debug", "")));
        if (directPlayIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(directPlayIntent);
        } else {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL_GOOGLE_PLAY_APP)));
        }
    }

    public static SharedPreferences getPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}
