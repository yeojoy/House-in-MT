package me.yeojoy.hancahouse.util

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {

    private const val NAME = "houses_in_mtl"
    private const val MODE = Context.MODE_PRIVATE

    private lateinit var prefereces: SharedPreferences

    private val CRAWLER_STATUS = Pair("crawler_status", "off")
    private val IS_FIRST_LAUNCH = Pair("is_first_launch", true)

    fun init(context: Context) {
        prefereces = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var crawlerStatus: String?
        get() = prefereces.getString(CRAWLER_STATUS.first, CRAWLER_STATUS.second)
        set(value) = prefereces.edit {
            it.putString(CRAWLER_STATUS.first, value)
        }

    var isFirstLaunch: Boolean?
        get() = prefereces.getBoolean(IS_FIRST_LAUNCH.first, IS_FIRST_LAUNCH.second)
        set(value) = prefereces.edit {
            it.putBoolean(IS_FIRST_LAUNCH.first, value ?: false)
        }
}