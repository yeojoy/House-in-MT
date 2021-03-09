package me.yeojoy.hancahouse.main

import me.yeojoy.hancahouse.app.BasePresenter
import me.yeojoy.hancahouse.app.BaseView
import me.yeojoy.hancahouse.model.House

interface TabPageContract {
    interface Presenter<T> : BasePresenter {
        fun retrieveItems()
        fun itemClicked(position: Int)

        fun numberOfItem() : Int
        fun itemAt(position: Int): T
    }

    interface View<T> : BaseView<Presenter<T>> {
        fun onSuccessGetItems()
        fun onFailGetItems()
        fun onClickItem(item: T)
    }
}