package me.yeojoy.hancahouse

import me.yeojoy.hancahouse.app.BasePresenter
import me.yeojoy.hancahouse.app.BaseView
import me.yeojoy.hancahouse.model.House

interface MainContract {
    interface Presenter : BasePresenter, HouseClickAction {
        fun retrieveHouses()
        fun houseItemClicked(position: Int)

        fun optionSettingClicked()

        fun numberOfHouses() : Int
        fun houseAt(position: Int): House
    }

    interface View : BaseView<Presenter> {
        fun onGetHouses(hasError: Boolean)

        fun onItemClicked(house: House)
        fun refreshResult(houses: List<House>?)
    }
}