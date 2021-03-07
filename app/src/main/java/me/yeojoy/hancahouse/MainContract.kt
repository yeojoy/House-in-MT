package me.yeojoy.hancahouse

import me.yeojoy.hancahouse.app.BasePresenter
import me.yeojoy.hancahouse.app.BaseView
import me.yeojoy.hancahouse.model.House

interface MainContract {
    interface Presenter : BasePresenter, HouseClickAction {
        fun retrieveHouses()
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
        fun onGetHouses(hasError: Boolean)
        fun turnedOnAlarmManager()
        fun turnedOffAlarmManager()

        fun onItemClicked(house: House)
        fun refreshResult(houses: List<House>?)
    }
}