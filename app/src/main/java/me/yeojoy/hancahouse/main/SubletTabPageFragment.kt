package me.yeojoy.hancahouse.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.yeojoy.hancahouse.R
import me.yeojoy.hancahouse.model.House

class SubletTabPageFragment: Fragment(), TabPageContract.View<House> {

    private lateinit var presenter: TabPageContract.Presenter<House>

    companion object {
        fun newInstance(): SubletTabPageFragment {
            return SubletTabPageFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setPresenter(SubletTabPagePresenter(this))
        return inflater.inflate(R.layout.fragment_tab_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onSuccessGetItems() {

    }

    override fun onFailGetItems() {

    }

    override fun onClickItem(item: House) {

    }

    override fun setPresenter(presenter: TabPageContract.Presenter<House>) {
        this.presenter = presenter
    }
}