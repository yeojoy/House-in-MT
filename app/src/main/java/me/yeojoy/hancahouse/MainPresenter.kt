package me.yeojoy.hancahouse

import me.yeojoy.hancahouse.model.House
import me.yeojoy.hancahouse.repository.HouseDBRepository

class MainPresenter(view: MainContract.View) : MainContract.Presenter {

    private var houses: MutableList<House> = mutableListOf()

    override fun retrieveHouses(reponsitory: HouseDBRepository) {

    }

    override fun retrieveHouses() {

    }

    override fun saveHouses(houses: List<House>) {

    }

    override fun houseItemClicked(position: Int) {

    }

    override fun optionStartCrawlerClicked() {

    }

    override fun optionStopCrawlerClicked() {

    }

    override fun optionSettingClicked() {

    }

    override fun optionManageAlarmClicked() {

    }

    override fun optionDeleteAllClicked() {

    }

    override fun numberOfHouses() : Int {
        return houses.size
    }

    override fun houseAt(position: Int): House {
        return houseAt(position)
    }

    override fun onViewCreated() {

    }

    override fun onViewDestroyed() {

    }

    override fun listItemClicked() {

    }

}

