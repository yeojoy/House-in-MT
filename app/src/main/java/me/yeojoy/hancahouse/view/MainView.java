package me.yeojoy.hancahouse.view;

import android.widget.ImageView;

import me.yeojoy.hancahouse.model.House;

public interface MainView {
    void onItemClick(ImageView transitionView, House house);
}