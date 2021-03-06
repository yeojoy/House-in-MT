package me.yeojoy.hancahouse.app;

import android.app.Application;

import me.yeojoy.hancahouse.util.AppPreferences;

class HancaHouseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
    }

}
