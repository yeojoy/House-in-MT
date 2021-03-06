package me.yeojoy.hancahouse.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import me.yeojoy.hancahouse.db.HancaDatabase;
import me.yeojoy.hancahouse.db.HouseDetailDao;
import me.yeojoy.hancahouse.model.HouseDetail;

public class HouseDetailDBRepository {
    private static final String TAG = HouseDetailDBRepository.class.getSimpleName();

    private HouseDetailDao mHouseDetailDao;
    private LiveData<HouseDetail> mHouseDetail;
    private LiveData<List<HouseDetail>> mAllHouseDetails;

    public HouseDetailDBRepository(Context context) {
        HancaDatabase db = HancaDatabase.getDatabase(context);
        mHouseDetailDao = db.houseDetailDao();
        mAllHouseDetails = mHouseDetailDao.getAllHouseDetails();
    }

    public LiveData<List<HouseDetail>> getAllHouseDetails() {
        return mAllHouseDetails;
    }

    public LiveData<HouseDetail> getHouseDetail(int houseId) {
        mHouseDetail = mHouseDetailDao.getHouseDetail(houseId);
        return mHouseDetail;
    }

    public void saveHouseDetail(HouseDetail houseDetail) {
        new InsertAsyncTask(mHouseDetailDao).execute(houseDetail);
    }

    public void updateHouseDetail(HouseDetail houseDetail) {
        new UpdateAsyncTask(mHouseDetailDao).execute(houseDetail);
    }

    public void deleteAll() {
        new DeleteAsyncTask(mHouseDetailDao).execute();
    }

    private static class InsertAsyncTask extends AsyncTask<HouseDetail, Void, Void> {

        private HouseDetailDao mHouseDetailDao;

        InsertAsyncTask(HouseDetailDao houseDao) {
            mHouseDetailDao = houseDao;
        }

        @Override
        protected Void doInBackground(HouseDetail... houseDetails) {
            mHouseDetailDao.insert(houseDetails[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<HouseDetail, Void, Void> {
        private HouseDetailDao mHouseDetailDao;

        UpdateAsyncTask(HouseDetailDao houseDao) {
            mHouseDetailDao = houseDao;
        }

        @Override
        protected Void doInBackground(HouseDetail... houseDetails) {
            mHouseDetailDao.update(houseDetails[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Void, Void, Void> {
        private HouseDetailDao mHouseDetailDao;

        DeleteAsyncTask(HouseDetailDao houseDao) {
            mHouseDetailDao = houseDao;
        }

        @Override
        protected Void doInBackground(Void... args) {
            mHouseDetailDao.deleteAll();
            return null;
        }
    }

}
