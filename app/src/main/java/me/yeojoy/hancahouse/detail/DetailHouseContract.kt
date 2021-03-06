package me.yeojoy.hancahouse.detail;

import me.yeojoy.hancahouse.app.BasePresenter
import me.yeojoy.hancahouse.app.BaseView;
import me.yeojoy.hancahouse.model.House

interface DetailHouseContract {
    interface Presenter : BasePresenter {
        fun getHouse()
        fun emailClicked()
    }

    interface View : BaseView<Presenter> {
        fun onGetHouse(house: House)
    }
}