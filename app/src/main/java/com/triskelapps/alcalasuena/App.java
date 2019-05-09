package com.triskelapps.alcalasuena;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.triskelapps.alcalasuena.interactor.BandInteractor;
import com.triskelapps.alcalasuena.interactor.VenueInteractor;
import com.triskelapps.alcalasuena.util.NotificationHelper;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by julio on 17/06/16.
 */

public class App extends Application {

    private static final String TAG = "App";

    public static final int CACHED_DATA_VERSION = 1;

    public static final String PREFIX = "com.triskelapps.alcalasuena.";

//    public static final String SHARED_FIRST_TIME = PREFIX + "first_time_7";
    public static final String SHARED_CURRENT_DATA_VERSION = PREFIX + "shared_current_data_version_2018";
    public static final String SHARED_FIRST_TIME_APP_LAUNCHING = PREFIX + "extra_first_time_app_lauching_2018";
    private static final String SHARED_CACHED_DATA_STORED = PREFIX + "shared_cached_data_stored_2018";
    public static final String SHARED_PIN_SEND_NEWS_ENCRIPT = PREFIX + "shared_pin_send_news_encript";
    private static final String SHARED_MIGRATION_3_4_DONE = "shared_migration_3_4_done";

    public static final String ACTION_REFRESH_DATA = PREFIX + "action_refresh_data";
    public static final String ACTION_SHOW_NOTIFICATION = PREFIX + "action_show_notification";


    public static final String URL_GOOGLE_PLAY_APP = "https://play.google.com/store/apps/details?id=com.triskelapps.alcalasuena";
    public static final String URL_APPLE_STORE_APP = "https://itunes.apple.com/es/app/Alcala-suena/id1458551516";
    public static final String URL_DIRECT_GOOGLE_PLAY_APP = "market://details?id=com.triskelapps.alcalasuena";


    @Override
    public void onCreate() {
        super.onCreate();


        CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(false).build();
        Fabric.with(this, new Crashlytics.Builder().core(core).build());

        NotificationHelper.with(this).initializeOreoChannelsNotification();

        configureRealm();

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
//        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Exo-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        initializeDataFirstTime();
//        updateDataFromApi();

//        String token = FirebaseInstanceId.getInstance().getToken();
//        Log.d(TAG, "Refreshed token: " + token);

        FirebaseMessaging.getInstance().subscribeToTopic("news");

        if (BuildConfig.DEBUG) {
            FirebaseMessaging.getInstance().subscribeToTopic("test_news");
        }

//        if (true) {
//            throw new RuntimeException("esprueba");
//        }

    }

    private void initializeDataFirstTime() {

        if (!getPrefs(this).getBoolean(SHARED_MIGRATION_3_4_DONE, false)) {
//        if(true) {
            getPrefs(this).edit().putBoolean(SHARED_CACHED_DATA_STORED, false).commit();
            getPrefs(this).edit().putBoolean(SHARED_MIGRATION_3_4_DONE, true).commit();
        }

        if (!getPrefs(this).getBoolean(SHARED_CACHED_DATA_STORED, false)) {
            new BandInteractor(this, null).initializeBandsFirstTime();
            new VenueInteractor(this,null).initializeVenuesFirstTime();
            getPrefs(this).edit().putBoolean(SHARED_CACHED_DATA_STORED, true).commit();
        }

    }

    private void configureRealm() {

        Realm.init(this);

        RealmConfiguration.Builder configBuilder = new RealmConfiguration.Builder()
//                .name("myrealm.realm")
//                .encryptionKey(getKey())
                .schemaVersion(MyRealmMigration.VERSION)
//                .modules(new MySchemaModule())
//                .migration(new MyRealmMigration())
                .deleteRealmIfMigrationNeeded()
                ;

        if (BuildConfig.DEBUG) {
//            configBuilder.deleteRealmIfMigrationNeeded();
        }

        RealmConfiguration config = configBuilder.build();

        Realm.setDefaultConfiguration(config);
    }

    public static void openAppInGooglePlay(Context context) {
//        Intent directPlayIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_DIRECT_GOOGLE_PLAY_APP));
//        if (directPlayIntent.resolveActivity(context.getPackageManager()) != null) {
//            context.startActivity(directPlayIntent);
//        } else {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL_GOOGLE_PLAY_APP)));
//        }
    }

    public static SharedPreferences getPrefs(Context context) {
//        return new SecurePreferences(context);
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}
