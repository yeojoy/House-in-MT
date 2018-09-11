package me.yeojoy.hancahouse.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import java.util.List;

import me.yeojoy.hancahouse.model.House;
import me.yeojoy.hancahouse.repository.HouseNetworkRepository;

public class MainViewModel extends ViewModel {
    private static final String TAG = MainViewModel.class.getSimpleName();

    private MutableLiveData<List<House>> mHouses;

    public void loadData(@Nullable Integer pageNumber) {
        int page = pageNumber == null ? 1 : pageNumber.intValue();
        HouseNetworkRepository networkRepository = new HouseNetworkRepository();
        networkRepository.loadPage(page, data -> mHouses.postValue(data));
    }

    public MutableLiveData<List<House>> getHouses() {
        if (mHouses == null) {
            mHouses = new MutableLiveData<>();
        }
        return mHouses;
    }
}
