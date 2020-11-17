package me.yeojoy.hancahouse.app

interface BaseView<T> {
    fun setPresenter(presenter: T)
}