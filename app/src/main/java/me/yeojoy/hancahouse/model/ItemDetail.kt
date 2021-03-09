package me.yeojoy.hancahouse.model;

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemDetail(
        var title: String = "",
        var contents: String? = "",
        var url: String? = "",
        var imageUrlString: String? = "",
        var imageUrls: MutableList<String> = mutableListOf()
) : Parcelable