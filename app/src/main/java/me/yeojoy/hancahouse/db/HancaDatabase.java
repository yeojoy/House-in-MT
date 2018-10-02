package me.yeojoy.hancahouse.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import me.yeojoy.hancahouse.model.House;

@Database(entities = { House.class }, version = 3)
public abstract class HancaDatabase extends RoomDatabase implements DBConstants {

    public abstract HouseDao houseDao();

    private static HancaDatabase sInstance;

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE " + TABLE_NAME
                    + " ADD COLUMN author TEXT");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE " + TABLE_NAME
                    + " ADD COLUMN board_type INTEGER DEFAULT 1 NOT NULL");
            database.execSQL("ALTER TABLE " + TABLE_NAME
                    + " ADD COLUMN parsed_time TEXT");
        }
    };

    public static HancaDatabase getDatabase(final Context context) {
        if (sInstance == null) {
            synchronized (HancaDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            HancaDatabase.class, DATABASE_NAME)
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                            .build();
                }
            }
        }

        return sInstance;
    }
}
