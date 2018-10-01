package me.yeojoy.hancahouse.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import me.yeojoy.hancahouse.db.HancaDatabase;
import me.yeojoy.hancahouse.db.HouseDao;
import me.yeojoy.hancahouse.model.House;

public class HouseDBRepository {
    private static final String TAG = HouseDBRepository.class.getSimpleName();

    private HouseDao mHouseDao;
    private LiveData<List<House>> mAllRents;

    public HouseDBRepository(Context context) {
        HancaDatabase db = HancaDatabase.getDatabase(context);
        mHouseDao = db.houseDao();
        mAllRents = mHouseDao.getAllRents();
    }

    public LiveData<List<House>> getAllRents() {
        return mAllRents;
    }

    ///////////////////////////////////////////////////////////
    // House
    ///////////////////////////////////////////////////////////

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

    public void deleteAllFromTable() {
        new DeleteAsyncTask(mHouseDao).execute();
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

    private static class DeleteAsyncTask extends AsyncTask<Void, Void, Void> {
        private HouseDao mHouseDao;

        DeleteAsyncTask(HouseDao houseDao) {
            mHouseDao = houseDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mHouseDao.deleteAll();
            return null;
        }
    }

}
