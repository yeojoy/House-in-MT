package me.yeojoy.hancahouse.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Iterator;
import java.util.List;

import me.yeojoy.hancahouse.model.House;
import me.yeojoy.hancahouse.repository.HouseDBRepository;
import me.yeojoy.hancahouse.repository.HouseNetworkRepository;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<House>> mHouses;
    private LiveData<List<House>> mSublets;

    private HouseDBRepository mHouseDBRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mHouseDBRepository = new HouseDBRepository(application);
        mHouses = mHouseDBRepository.getAllHouses();
        mSublets = mHouseDBRepository.getAllSublets();
    }

    public void loadHouses(@Nullable Integer pageNumber) {
        int page = pageNumber == null ? 1 : pageNumber;
        HouseNetworkRepository networkRepository = HouseNetworkRepository.getInstance();
        networkRepository.loadPage(HouseNetworkRepository.TYPE_RENT, page, this::saveHousesToDatabase);
    }

    private void saveHousesToDatabase(List<House> houses) {
        Iterator<House> iterator = houses.iterator();
        List<House> sources = mHouses.getValue();
        while (iterator.hasNext()) {
            House house = iterator.next();
            if (sources.contains(house)) {
                Log.d(TAG, "UID, " + house.getUid() + ", is deleted.");
                iterator.remove();
            }
        }

        if (houses.size() > 0) {
            mHouseDBRepository.saveHouses(houses);
        }
    }

    public LiveData<List<House>> getHouses() {
        if (mHouses == null) {
            mHouses = new MutableLiveData<>();
        }
        return mHouses;
    }

    public void loadSublets(@Nullable Integer pageNumber) {
        int page = pageNumber == null ? 1 : pageNumber;
        HouseNetworkRepository networkRepository = HouseNetworkRepository.getInstance();
        networkRepository.loadPage(HouseNetworkRepository.TYPE_SUBLET, page, this::saveSubletsToDatabase);
    }

    private void saveSubletsToDatabase(List<House> sublets) {
        Iterator<House> iterator = sublets.iterator();
        List<House> sources = mSublets.getValue();
        while (iterator.hasNext()) {
            House house = iterator.next();
            if (sources.contains(house)) {
                Log.d(TAG, "UID, " + house.getUid() + ", is deleted.");
                iterator.remove();
            }
        }

        if (sublets.size() > 0) {
            mHouseDBRepository.saveHouses(sublets);
        }
    }

    public LiveData<List<House>> getSublets() {
        if (mSublets == null) {
            mSublets = new MutableLiveData<>();
        }
        return mSublets;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
