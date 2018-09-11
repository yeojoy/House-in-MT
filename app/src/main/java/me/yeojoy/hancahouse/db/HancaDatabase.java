package me.yeojoy.hancahouse.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import me.yeojoy.hancahouse.model.House;

@Database(entities = { House.class }, version = 1)
public abstract class HancaDatabase extends RoomDatabase implements DBConstants {

    public abstract HouseDao houseDao();

    private static HancaDatabase sInstance;

    public static HancaDatabase getDatabase(final Context context) {
        if (sInstance == null) {
            synchronized (HancaDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            HancaDatabase.class, TABLE_NAME)
                            .build();
                }
            }
        }

        return sInstance;
    }
}
