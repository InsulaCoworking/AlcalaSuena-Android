package com.triskelapps.alcalasuena.database;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class DBMigrationBase {


    public static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

//            database.execSQL("CREATE TABLE 'product' ('_id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , 'name' VARCHAR(50), 'name_normalized' VARCHAR(50), 'idCategory' INTEGER DEFAULT 0, 'months_season' VARCHAR(12) DEFAULT 222222222222, 'marked' INTEGER DEFAULT 0, 'added' INTEGER DEFAULT 0, 'completed' INTEGER DEFAULT 0, 'amount' FLOAT DEFAULT 0, 'g_kg_ud' INTEGER DEFAULT 1, 'order_weight' INTEGER DEFAULT 0, 'remains' INTEGER DEFAULT 0);");

        }
    };
}
