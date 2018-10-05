package me.yeojoy.hancahouse.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import me.yeojoy.hancahouse.model.HouseDetail;
import me.yeojoy.hancahouse.repository.HouseDetailNetworkRepository;

public class DetailViewModel extends ViewModel {

    private MutableLiveData<HouseDetail> mHouseDetailLiveData;

    public MutableLiveData<HouseDetail> getHouseDetailLiveData() {
        if (mHouseDetailLiveData == null) {
            mHouseDetailLiveData = new MutableLiveData<>();
        }

        return mHouseDetailLiveData;
    }

    public void loadPage(HouseDetail detail) {
        HouseDetailNetworkRepository.getInstance().loadPage(detail,
                houseDetail -> mHouseDetailLiveData.postValue(houseDetail));
    }
}
