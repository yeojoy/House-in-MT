package me.yeojoy.hancahouse

import me.yeojoy.hancahouse.app.BasePresenter
import me.yeojoy.hancahouse.app.BaseView

interface MainContract {
    interface Presenter : BasePresenter {
        fun optionSettingClicked()

    }

    interface View : BaseView<Presenter> {
    }
}