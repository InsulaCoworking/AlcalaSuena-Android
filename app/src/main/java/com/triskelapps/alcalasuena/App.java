package com.triskelapps.alcalasuena;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.triskelapps.alcalasuena.interactor.BandInteractor;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by julio on 17/06/16.
 */

public class App extends Application {

    private static final String TAG = "App";

    public static final String PREFIX = "com.halcash.halcashapp.";

    public static final String SHARED_FIRST_TIME = PREFIX + "first_time_7";


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
                .setDefaultFontPath("fonts/Aller.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

//        initializeData();

    }

    private void initializeData() {
        BandInteractor.initializeBands();

    }

    private void configureRealm() {

        Realm.init(this);

        RealmConfiguration.Builder configBuilder = new RealmConfiguration.Builder()
//                .name("myrealm.realm")
//                .encryptionKey(getKey())
                .schemaVersion(MyRealmMigration.VERSION)
//                .modules(new MySchemaModule())
                .migration(new MyRealmMigration());

        if (BuildConfig.DEBUG) {
            configBuilder.deleteRealmIfMigrationNeeded();
        }

        RealmConfiguration config = configBuilder.build();

        Realm.setDefaultConfiguration(config);
    }


    public static SharedPreferences getPrefs(Context context) {
//        return new SecurePreferences(context);
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}
