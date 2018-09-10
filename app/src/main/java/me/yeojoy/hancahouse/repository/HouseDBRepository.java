package me.yeojoy.hancahouse.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;

import me.yeojoy.hancahouse.db.HancaDatabase;
import me.yeojoy.hancahouse.db.HouseDao;
import me.yeojoy.hancahouse.model.House;

public class HouseDBRepository {

    private HouseDao mHouseDao;
    private LiveData<List<House>> mAllHouses;

    public HouseDBRepository(Context context) {
        HancaDatabase db = HancaDatabase.getDatabase(context);
        mHouseDao = db.houseDao();
        mAllHouses = mHouseDao.getAllHouses();
    }
}
