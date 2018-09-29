package me.yeojoy.hancahouse.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import me.yeojoy.hancahouse.model.Sublet;

@Dao
public interface SubletDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Sublet... sublets);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Sublet... sublets);

    @Query("SELECT * FROM " + DBConstants.TABLE_NAME_FOR_SUBLET + " ORDER BY uid DESC")
    LiveData<List<Sublet>> getAllSublets();
}
