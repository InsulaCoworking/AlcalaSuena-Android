package com.triskelapps.alcalasuena;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import androidx.room.Room;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.triskelapps.alcalasuena.database.AppDatabase;
import com.triskelapps.alcalasuena.interactor.BandInteractor;
import com.triskelapps.alcalasuena.interactor.VenueInteractor;
import com.triskelapps.alcalasuena.util.NotificationHelper;
import com.triskelapps.alcalasuena.util.update_app.UpdateAppManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by julio on 17/06/16.
 */

public class App extends MultiDexApplication {

    private static final String TAG = "App";

    public static final int FEST_YEAR = 2022;
    public static List<String> festDates = new ArrayList<>();

    static {
        festDates.add("2022-06-03");
        festDates.add("2022-06-04");
        festDates.add("2022-06-05");
    }

    public static final int CACHED_DATA_VERSION = 1;

    public static final String PREFIX = BuildConfig.APPLICATION_ID + ".";

//    public static final String SHARED_FIRST_TIME = PREFIX + "first_time_7";
    public static final String SHARED_CURRENT_DATA_VERSION = PREFIX + "shared_current_data_version_" + FEST_YEAR;
    public static final String SHARED_FIRST_TIME_APP_LAUNCHING = PREFIX + "extra_first_time_app_lauching_" + FEST_YEAR;
    private static final String SHARED_CACHED_DATA_STORED = PREFIX + "shared_cached_data_stored_" + FEST_YEAR;
    public static final String SHARED_PIN_SEND_NEWS_ENCRIPT = PREFIX + "shared_pin_send_news_encript";

    public static final String ACTION_REFRESH_DATA = PREFIX + "action_refresh_data";
    public static final String ACTION_SHOW_NOTIFICATION = PREFIX + "action_show_notification";

    public static final String URL_GOOGLE_PLAY_APP = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
    public static final String URL_APPLE_STORE_APP = "https://itunes.apple.com/es/app/Alcala-suena/id1458551516";
    public static final String URL_DIRECT_GOOGLE_PLAY_APP = "market://details?id=" + BuildConfig.APPLICATION_ID;
    public static final String TOPIC_NEWS = "news";
    public static final String TOPIC_NEWS_TEST = "test_news3";


    private static AppDatabase db;
    private static final String DB_NAME = "app_db.sqlite";

    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DB_NAME)
//                .addMigrations(DBMigrationBase.MIGRATION_5_6)
                .fallbackToDestructiveMigration()
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

        initializeDataFirstTime();

        UpdateAppManager.scheduleAppUpdateCheckWork(this);

//        updateDataFromApi();

//        String token = FirebaseInstanceId.getInstance().getToken();
//        Log.d(TAG, "Refreshed token: " + token);

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_NEWS);

        if (BuildConfig.DEBUG) {
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_NEWS_TEST);
        }

//        if (true) {
//            throw new RuntimeException("esprueba");
//        }


    }

    private void initializeDataFirstTime() {

        if (!getPrefs(this).getBoolean(SHARED_CACHED_DATA_STORED, false)) {
            new BandInteractor(this, null).initializeBandsFirstTime();
            new VenueInteractor(this,null).initializeVenuesFirstTime();
            getPrefs(this).edit().putBoolean(SHARED_CACHED_DATA_STORED, true).commit();
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
