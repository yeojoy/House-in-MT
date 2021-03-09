package me.yeojoy.hancahouse.detail;

import android.text.SpannableStringBuilder
import me.yeojoy.hancahouse.app.BasePresenter
import me.yeojoy.hancahouse.app.BaseView;
import me.yeojoy.hancahouse.model.House
import me.yeojoy.hancahouse.model.ItemDetail

interface DetailItemContract {
    interface Presenter : BasePresenter {
        fun setHouse(house: House)
        fun emailClicked(email: String)
        fun telephoneNumberClicked(telephoneNumber: String)
    }

    interface View : BaseView<Presenter> {
        fun onGetHouseDetail(itemDetail: ItemDetail)
        fun onGetWholeContents(contents: SpannableStringBuilder)
        fun onGetEmailAddress(emailAddress: String)
        fun onGetTelephoneNumber(telephoneNumber: String)
    }
}