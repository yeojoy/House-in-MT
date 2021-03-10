package me.yeojoy.hancahouse.detail;

import android.text.SpannableStringBuilder
import me.yeojoy.hancahouse.app.BasePresenter
import me.yeojoy.hancahouse.app.BaseView;
import me.yeojoy.hancahouse.model.House
import me.yeojoy.hancahouse.model.ItemDetail

interface ItemDetailContract {
    interface Presenter<T> : BasePresenter {
        fun setItem(item: T)
        fun emailClicked(email: String)
        fun telephoneNumberClicked(telephoneNumber: String)

        fun goWebClicked()
    }

    interface View<T> : BaseView<Presenter<T>> {
        fun onGetHouseDetail(itemDetail: ItemDetail)
        fun onGetWholeContents(contents: SpannableStringBuilder)
        fun onGetEmailAddress(emailAddress: String)
        fun onGetTelephoneNumber(telephoneNumber: String)

        fun onGetWebUrl(url: String)
    }
}