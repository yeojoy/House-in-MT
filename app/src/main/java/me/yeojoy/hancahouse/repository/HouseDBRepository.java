package me.yeojoy.hancahouse.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import me.yeojoy.hancahouse.db.HancaDatabase;
import me.yeojoy.hancahouse.db.HouseDao;
import me.yeojoy.hancahouse.db.SubletDao;
import me.yeojoy.hancahouse.model.House;
import me.yeojoy.hancahouse.model.Sublet;

public class HouseDBRepository {
    private static final String TAG = HouseDBRepository.class.getSimpleName();

    private HouseDao mHouseDao;
    private SubletDao mSubletDao;
    private LiveData<List<House>> mAllHouses;
    private LiveData<List<Sublet>> mAllSublets;

    public HouseDBRepository(Context context) {
        HancaDatabase db = HancaDatabase.getDatabase(context);
        mHouseDao = db.houseDao();
        mSubletDao = db.subletDao();
        mAllHouses = mHouseDao.getAllHouses();
    }

    ///////////////////////////////////////////////////////////
    // House
    ///////////////////////////////////////////////////////////
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

    ///////////////////////////////////////////////////////////
    // Sublet
    ///////////////////////////////////////////////////////////

    public LiveData<List<Sublet>> getAllSublets() {
        return mAllSublets;
    }

    public void saveSublets(List<Sublet> sublets) {
        Sublet[] subletArray = new Sublet[sublets.size()];
        sublets.toArray(subletArray);
        new InsertSubletAsyncTask(mSubletDao).execute(subletArray);
    }

    public void updateSublets(List<Sublet> sublets) {
        Sublet[] subletArray = new Sublet[sublets.size()];
        sublets.toArray(subletArray);
        new UpdateSubletAsyncTask(mSubletDao).execute(subletArray);
    }

    private static class InsertSubletAsyncTask extends AsyncTask<Sublet, Void, Void> {

        private SubletDao mSublectDao;

        InsertSubletAsyncTask(SubletDao houseDao) {
            mSublectDao = houseDao;
        }

        @Override
        protected Void doInBackground(Sublet... sublets) {
            mSublectDao.insert(sublets);
            return null;
        }
    }

    private static class UpdateSubletAsyncTask extends AsyncTask<Sublet, Void, Void> {
        private SubletDao mSublectDao;

        UpdateSubletAsyncTask(SubletDao houseDao) {
            mSublectDao = houseDao;
        }

        @Override
        protected Void doInBackground(Sublet... sublets) {
            mSublectDao.update(sublets);
            return null;
        }
    }
}
