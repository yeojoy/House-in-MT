package me.yeojoy.hancahouse.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_tab_page.*
import me.yeojoy.hancahouse.R
import me.yeojoy.hancahouse.app.Constants
import me.yeojoy.hancahouse.app.adapter.ItemAdapter
import me.yeojoy.hancahouse.detail.HouseDetailActivity
import me.yeojoy.hancahouse.model.House

class RentTabPageFragment: Fragment(), TabPageContract.View<House> {

    private lateinit var presenter: TabPageContract.Presenter<House>

    companion object {
        fun newInstance(): RentTabPageFragment {
            return RentTabPageFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setPresenter(RentTabPagePresenter(this))
        return inflater.inflate(R.layout.fragment_tab_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerview.adapter = ItemAdapter(presenter)
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        presenter.retrieveItems()
    }

    override fun onSuccessGetItems() {
        recyclerview.visibility = View.VISIBLE
        textViewEmptyList.visibility = View.GONE
        recyclerview.adapter?.notifyDataSetChanged()
    }

    override fun onFailGetItems() {
        textViewEmptyList.visibility = View.VISIBLE
        recyclerview.visibility = View.GONE
    }

    override fun onClickItem(item: House) {
        val intent = Intent(context, HouseDetailActivity::class.java)
        intent.putExtra(Constants.KEY_ITEM, item)
        startActivity(intent)
    }

    override fun setPresenter(presenter: TabPageContract.Presenter<House>) {
        this.presenter = presenter
    }
}