package me.yeojoy.hancahouse.app.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import androidx.viewpager2.adapter.FragmentStateAdapter
import me.yeojoy.hancahouse.main.RentTabPageFragment
import me.yeojoy.hancahouse.main.SellingTabPageFragment
import me.yeojoy.hancahouse.main.SubletTabPageFragment

class TabPageAdapter(activity: FragmentActivity):
        FragmentStateAdapter(activity) {
    private val fragments: List<Fragment>
    init {
        fragments = listOf(
                SubletTabPageFragment.newInstance(),
                RentTabPageFragment.newInstance(),
                SellingTabPageFragment.newInstance()
        )
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}