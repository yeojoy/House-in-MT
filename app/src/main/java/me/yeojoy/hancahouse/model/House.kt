package me.yeojoy.hancahouse.model;

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class House(
        var title: String = "",
        var thumbnailUrl: String = "",
        var detailUrl: String = "",
        var author: String = "",
        var date: Long = 0L,
        var parsedDateTime: String = "",
        var uid: Long = 0
) : Parcelable
