package me.yeojoy.hancahouse.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import me.yeojoy.hancahouse.model.House;
import me.yeojoy.hancahouse.model.HouseDetail;

@Database(entities = { House.class, HouseDetail.class }, version = 5, exportSchema = false)
public abstract class HancaDatabase extends RoomDatabase implements DBConstants {

    public abstract HouseDao houseDao();
    public abstract HouseDetailDao houseDetailDao();

    private static HancaDatabase sInstance;

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE " + TABLE_NAME
                    + " ADD COLUMN " + COLUMN_AUTHOR + " TEXT");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE " + TABLE_NAME
                    + " ADD COLUMN " + COLUMN_BOARD_TYPE + " INTEGER DEFAULT 1 NOT NULL");
            database.execSQL("ALTER TABLE " + TABLE_NAME
                    + " ADD COLUMN " + COLUMN_PARSED_TIME + " TEXT");
        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DETAIL);
            database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_DETAIL
                    + " (" + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_HOUSE_ID + " INTEGER NOT NULL, "
                    + COLUMN_TITLE + " TEXT NOT NULL, "
                    + COLUMN_CONTENTS + " TEXT, "
                    + COLUMN_IMAGE_URL + " TEXT, "
                    + "FOREIGN KEY (" + COLUMN_HOUSE_ID + ") REFERENCES "
                    + TABLE_NAME + " (" + COLUMN_ID + ") "
                    + "ON DELETE NO ACTION ON UPDATE NO ACTION"
                    + ")"
            );
        }
    };

    private static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("DELETE FROM " + TABLE_NAME_DETAIL);
            database.execSQL("ALTER TABLE " + TABLE_NAME_DETAIL
                    + " ADD COLUMN " + COLUMN_DETAIL_URL + " TEXT"
            );
            // UPDATE tablename SET filedA='456', fieldB='ABC' WHERE test='123' LIMIT 10;
        }
    };

    public static HancaDatabase getDatabase(final Context context) {
        if (sInstance == null) {
            synchronized (HancaDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            HancaDatabase.class, DATABASE_NAME)
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4,
                                    MIGRATION_4_5)
                            .build();
                }
            }
        }

        return sInstance;
    }
}
