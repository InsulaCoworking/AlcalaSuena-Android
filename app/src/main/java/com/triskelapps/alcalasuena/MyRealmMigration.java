package com.triskelapps.alcalasuena;

import com.triskelapps.alcalasuena.model.News;
import com.triskelapps.alcalasuena.model.NewsState;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by julio on 21/04/17.
 */

public class MyRealmMigration implements RealmMigration {

    public final static int VERSION = 3;

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        RealmSchema schema = realm.getSchema();

        if (oldVersion == 1) {
            oldVersion++;
        }

        if (oldVersion == 2) {

            schema.create(News.class.getSimpleName())
                    .addField(News.ID, int.class, FieldAttribute.PRIMARY_KEY)
                    .addField(News.NATIVE_SCREEN_CODE, int.class)
                    .addField(News.TITLE, String.class)
                    .addField(News.TEXT, String.class)
                    .addField(News.IMAGE, String.class)
                    .addField(News.BTN_TEXT, String.class)
                    .addField(News.BTN_LINK, String.class)
                    .addField(News.START_DATE_POPUP, String.class)
                    .addField(News.END_DATE_POPUP, String.class)
                    .addField(News.CADUCITY_DATE, String.class)
                    .addField(News.START_DATE_POPUP_TIME, Long.class)
                    .addField(News.END_DATE_POPUP_TIME, Long.class)
                    .addField(News.CADUCITY_DATE_TIME, Long.class);

            schema.create(NewsState.class.getSimpleName())
                    .addField(NewsState.ID_NEW, int.class)
                    .addField(NewsState.SEEN, Boolean.class)
                    .addField(NewsState.READ, Boolean.class) ;

            oldVersion++;
        }


        if (oldVersion < newVersion) {
            throw new IllegalStateException("Something wrong with Realm Migration. Version does not match. " +
                    "oldVersion=" + oldVersion + ", newVersion=" + newVersion);
        }
    }



    // EXAMPLES

//            RealmObjectSchema switchBoatSchema = schema.get(SwitchBoat.class.getSimpleName());
//            switchBoatSchema.addField(SwitchBoat.WARNING, boolean.class, FieldAttribute.REQUIRED);


//            RealmObjectSchema switchBoatSchema = schema.get(SwitchBoat.class.getSimpleName());
//            switchBoatSchema.removeField(SwitchBoat.WARNING);

//            schema.create(Alarm.class.getSimpleName())
//                    .addField(Alarm.ID, int.class, FieldAttribute.PRIMARY_KEY)
//                    .addField(Alarm.ID_BOAT, int.class)
//                    .addField(Alarm.DESCRIPTION, String.class)
//                    .addField(Alarm.TYPE, int.class)
//                    .addField(Alarm.TIMESTAMP, long.class);


//            schema.get(SMSIncome.class.getSimpleName()).renameField("info", SMSIncome.MESSAGE);

}
