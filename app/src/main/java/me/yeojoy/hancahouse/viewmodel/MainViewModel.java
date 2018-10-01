package me.yeojoy.hancahouse.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
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

    private LiveData<List<House>> mRents;

    private HouseDBRepository mHouseDBRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mHouseDBRepository = new HouseDBRepository(application);
        mRents = mHouseDBRepository.getAllRents();
    }

    public void loadHouses(@Nullable Integer pageNumber) {
        int page = pageNumber == null ? 1 : pageNumber;
        HouseNetworkRepository networkRepository = HouseNetworkRepository.getInstance();
        networkRepository.loadPage(page, this::saveHousesToDatabase);
    }

    private void saveHousesToDatabase(List<House> houses) {
        Iterator<House> iterator = houses.iterator();
        List<House> sources = mRents.getValue();
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

    public LiveData<List<House>> getRents() {
        return mRents;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    /**
     * Table에서 모든 집 정보 삭제
     */
    public void deleteAllFromTable() {
        mHouseDBRepository.deleteAllFromTable();
    }
}
