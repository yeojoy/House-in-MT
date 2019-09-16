package me.yeojoy.hancahouse.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import me.yeojoy.hancahouse.model.House;
import me.yeojoy.hancahouse.model.HouseDetail;
import me.yeojoy.hancahouse.repository.HouseDetailDBRepository;
import me.yeojoy.hancahouse.repository.HouseDetailNetworkRepository;

public class DetailViewModel extends AndroidViewModel {
    private static final String TAG = DetailViewModel.class.getSimpleName();

    private LiveData<HouseDetail> mHouseDetailLiveData;
    private LiveData<List<HouseDetail>> mAllHouseDetails;
    private LiveData<House> mHouseLiveData;

    private HouseDetailDBRepository mHouseDetailDBRepository;
    private HouseDetailNetworkRepository mHouseDetailNetworkRepository;

    public DetailViewModel(@NonNull Application application) {
        super(application);
        mHouseDetailDBRepository = new HouseDetailDBRepository(application);
        mHouseDetailNetworkRepository = HouseDetailNetworkRepository.getInstance();
        mAllHouseDetails = mHouseDetailDBRepository.getAllHouseDetails();
    }

    public LiveData<HouseDetail> getHouseDetailLiveData(int houseId) {
        mHouseDetailLiveData = mHouseDetailDBRepository.getHouseDetail(houseId);
        return mHouseDetailLiveData;
    }

    public LiveData<HouseDetail> getHouseDetailLiveData() {
        return mHouseDetailLiveData;
    }

    public LiveData<List<HouseDetail>> getAllHouseDetails() {
        return mAllHouseDetails;
    }

    public void loadPage(int houseId, String title, String url) {
        Log.i(TAG, "loadPage()");
        mHouseDetailNetworkRepository.loadPage(houseId, title, url, houseDetail -> {
            if (mHouseDetailLiveData.getValue() != null &&
                    TextUtils.isEmpty(mHouseDetailLiveData.getValue().getContents())) {
                mHouseDetailDBRepository.updateHouseDetail(houseDetail);
            } else {
                mHouseDetailDBRepository.saveHouseDetail(houseDetail);
            }
        });
    }

    public void deleteAllItems() {
        mHouseDetailDBRepository.deleteAll();
    }

    public LiveData<House> getHouseLiveData() {
        return mHouseLiveData;
    }

    public void setHouseLiveData(LiveData<House> houseLiveData) {
        mHouseLiveData = houseLiveData;
    }
}
