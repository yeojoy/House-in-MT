package me.yeojoy.hancahouse.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import me.yeojoy.hancahouse.model.House;
import me.yeojoy.hancahouse.model.Sublet;

@Database(entities = { House.class, Sublet.class }, version = 2)
public abstract class HancaDatabase extends RoomDatabase implements DBConstants {

    public abstract HouseDao houseDao();

    public abstract SubletDao subletDao();

    private static HancaDatabase sInstance;

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE " + TABLE_NAME
                    + " ADD COLUMN author TEXT");
        }
    };

    public static HancaDatabase getDatabase(final Context context) {
        if (sInstance == null) {
            synchronized (HancaDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            HancaDatabase.class, DATABASE_NAME)
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }

        return sInstance;
    }
}
