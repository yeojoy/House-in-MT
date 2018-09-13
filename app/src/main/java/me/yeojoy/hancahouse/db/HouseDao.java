package me.yeojoy.hancahouse.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import me.yeojoy.hancahouse.model.House;

@Dao
public interface HouseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(House... house);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(House... house);

    @Query("SELECT * FROM " + DBConstants.TABLE_NAME + " ORDER BY uid DESC")
    LiveData<List<House>> getAllHouses();
}
