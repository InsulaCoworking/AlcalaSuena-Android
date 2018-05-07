package com.triskelapps.alcalasuena;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.triskelapps.alcalasuena.interactor.BandInteractor;
import com.triskelapps.alcalasuena.interactor.VenueInteractor;

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
    public static final String SHARED_SUBSCRIBED_NEWS_NOTIFS = PREFIX + "shared_subscribed_news_notifs";

    public static final String ACTION_REFRESH_DATA = PREFIX + "action_refresh_data";
    public static final String ACTION_SHOW_NOTIFICATION = PREFIX + "action_show_notification";


    @Override
    public void onCreate() {
        super.onCreate();

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

        if (FirebaseInstanceId.getInstance().getToken() != null
                && !getPrefs(this).getBoolean(SHARED_SUBSCRIBED_NEWS_NOTIFS, false)) {
            FirebaseMessaging.getInstance().subscribeToTopic("news");
            getPrefs(this).edit().putBoolean(SHARED_SUBSCRIBED_NEWS_NOTIFS, true).commit();
        }

    }

    private void initializeDataFirstTime() {

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
                .migration(new MyRealmMigration())
                ;

        if (BuildConfig.DEBUG) {
//            configBuilder.deleteRealmIfMigrationNeeded();
        }

        RealmConfiguration config = configBuilder.build();

        Realm.setDefaultConfiguration(config);
    }


    public static SharedPreferences getPrefs(Context context) {
//        return new SecurePreferences(context);
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}
