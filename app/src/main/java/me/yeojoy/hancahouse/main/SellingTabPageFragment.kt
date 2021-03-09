package me.yeojoy.hancahouse.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.yeojoy.hancahouse.R
import me.yeojoy.hancahouse.model.Item

class SellingTabPageFragment: Fragment(), TabPageContract.View<Item> {

    private lateinit var presenter: TabPageContract.Presenter<Item>

    companion object {
        fun newInstance(): SellingTabPageFragment {
            return SellingTabPageFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setPresenter(SellingTabPagePresenter(this))
        return inflater.inflate(R.layout.fragment_tab_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onSuccessGetItems() {

    }

    override fun onFailGetItems() {

    }

    override fun onClickItem(item: Item) {

    }

    override fun setPresenter(presenter: TabPageContract.Presenter<Item>) {
        this.presenter = presenter
    }
}