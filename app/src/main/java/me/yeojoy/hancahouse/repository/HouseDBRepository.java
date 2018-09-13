package me.yeojoy.hancahouse.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import me.yeojoy.hancahouse.db.HancaDatabase;
import me.yeojoy.hancahouse.db.HouseDao;
import me.yeojoy.hancahouse.model.House;

public class HouseDBRepository {
    private static final String TAG = HouseDBRepository.class.getSimpleName();

    private HouseDao mHouseDao;
    private LiveData<List<House>> mAllHouses;

    public HouseDBRepository(Context context) {
        HancaDatabase db = HancaDatabase.getDatabase(context);
        mHouseDao = db.houseDao();
        mAllHouses = mHouseDao.getAllHouses();
    }

    public LiveData<List<House>> getAllHouses() {
        return mAllHouses;
    }

    public void saveHouses(List<House> houses) {
        House[] houseArray = new House[houses.size()];
        houses.toArray(houseArray);
        new InsertAsyncTask(mHouseDao).execute(houseArray);
    }

    public void updateHouses(List<House> houses) {
        House[] houseArray = new House[houses.size()];
        houses.toArray(houseArray);
        new UpdateAsyncTask(mHouseDao).execute(houseArray);
    }

    private static class InsertAsyncTask extends AsyncTask<House, Void, Void> {

        private HouseDao mHouseDao;

        InsertAsyncTask(HouseDao houseDao) {
            mHouseDao = houseDao;
        }

        @Override
        protected Void doInBackground(House... houses) {
            mHouseDao.insert(houses);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<House, Void, Void> {
        private HouseDao mHouseDao;

        UpdateAsyncTask(HouseDao houseDao) {
            mHouseDao = houseDao;
        }

        @Override
        protected Void doInBackground(House... houses) {
            mHouseDao.update(houses);
            return null;
        }
    }
}
