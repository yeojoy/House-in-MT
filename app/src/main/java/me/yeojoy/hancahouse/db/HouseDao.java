package me.yeojoy.hancahouse.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import me.yeojoy.hancahouse.model.House;

@Dao
public interface HouseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(House... house);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(House... house);

    @Query("SELECT * FROM " + DBConstants.TABLE_NAME + " WHERE board_type = 1 ORDER BY uid DESC")
    LiveData<List<House>> getAllHouses();

    @Query("SELECT * FROM " + DBConstants.TABLE_NAME + " WHERE board_type = 2 ORDER BY uid DESC")
    LiveData<List<House>> getAllSublets();

    @Query("SELECT * FROM " + DBConstants.TABLE_NAME + " ORDER BY uid DESC")
    LiveData<List<House>> getAllRents();

    @Query("SELECT * FROM " + DBConstants.TABLE_NAME + " ORDER BY uid DESC")
    List<House> getAllRawRents();

    @Query("DELETE FROM " + DBConstants.TABLE_NAME)
    void deleteAll();

}
