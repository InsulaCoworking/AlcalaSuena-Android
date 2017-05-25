package com.triskelapps.alcalasuena;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by julio on 21/04/17.
 */

public class MyRealmMigration implements RealmMigration {

    public final static int VERSION = 2;

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        RealmSchema schema = realm.getSchema();

        if (oldVersion == 1) {
//            RealmObjectSchema switchBoatSchema = schema.get(SwitchBoat.class.getSimpleName());
//            switchBoatSchema.addField(SwitchBoat.WARNING, boolean.class, FieldAttribute.REQUIRED);
            oldVersion++;
        }

        if (oldVersion == 2) {

//            RealmObjectSchema switchBoatSchema = schema.get(SwitchBoat.class.getSimpleName());
//            switchBoatSchema.removeField(SwitchBoat.WARNING);

//            schema.create(Alarm.class.getSimpleName())
//                    .addField(Alarm.ID, int.class)
//                    .addField(Alarm.ID_BOAT, int.class)
//                    .addField(Alarm.DESCRIPTION, String.class)
//                    .addField(Alarm.TYPE, int.class)
//                    .addField(Alarm.TIMESTAMP, long.class);

            oldVersion++;
        }

        if (oldVersion == 3) {

//            schema.create(SMSIncome.class.getSimpleName())
//                    .addField(SMSIncome.ID, int.class)
//                    .addField(SMSIncome.PHONE_NUMBER_FULL, String.class)
//                    .addField(SMSIncome.MESSAGE, String.class)
//                    .addField(SMSIncome.TYPE, int.class)
//                    .addField(SMSIncome.TIMESTAMP, long.class);

            oldVersion++;
        }

        if (oldVersion == 4) {

//            schema.get(SMSIncome.class.getSimpleName()).renameField("info", SMSIncome.MESSAGE);

            oldVersion++;
        }


            if (oldVersion < newVersion) {
                throw new IllegalStateException("Something wrong with Realm Migration. Version does not match. " +
                        "oldVersion=" + oldVersion + ", newVersion=" + newVersion);
            }
        }
}
