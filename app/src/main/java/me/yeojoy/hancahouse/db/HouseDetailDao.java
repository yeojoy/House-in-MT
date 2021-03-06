package me.yeojoy.hancahouse.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import me.yeojoy.hancahouse.model.HouseDetail;

@Dao
public interface HouseDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HouseDetail houseDetail);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(HouseDetail houseDetail);

    @Query("SELECT * FROM " + DBConstants.TABLE_NAME_DETAIL + " WHERE house_id = :houseId")
    LiveData<HouseDetail> getHouseDetail(int houseId);

    @Query("SELECT * FROM " + DBConstants.TABLE_NAME_DETAIL)
    LiveData<List<HouseDetail>> getAllHouseDetails();

    @Query("DELETE FROM " + DBConstants.TABLE_NAME_DETAIL)
    void deleteAll();
}
