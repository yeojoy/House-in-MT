package me.yeojoy.hancahouse

import me.yeojoy.hancahouse.app.BasePresenter
import me.yeojoy.hancahouse.app.BaseView
import me.yeojoy.hancahouse.model.House
import me.yeojoy.hancahouse.repository.HouseDBRepository

interface MainContract {
    interface Presenter : BasePresenter, HouseClickAction {
        fun retrieveHouses(reponsitory: HouseDBRepository)
        fun retrieveHouses()
        fun saveHouses(houses: List<House>)
        fun houseItemClicked(position: Int)

        fun optionStartCrawlerClicked()
        fun optionStopCrawlerClicked()
        fun optionSettingClicked()
        fun optionManageAlarmClicked()
        fun optionDeleteAllClicked()

        fun numberOfHouses() : Int
        fun houseAt(position: Int): House
    }

    interface View : BaseView<Presenter> {
        fun onGetHouses(houses: List<House>)
        fun turnedOnAlarmManager()
        fun turnedOffAlarmManager()

        fun onItemClicked(house: House)
        fun refreshResult(houses: List<House>?)
    }
}