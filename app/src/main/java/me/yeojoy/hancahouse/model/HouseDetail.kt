package me.yeojoy.hancahouse.model;

import android.os.Parcelable
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kotlinx.android.parcel.Parcelize

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.yeojoy.hancahouse.db.DBConstants;

@Parcelize
@Entity(tableName = DBConstants.TABLE_NAME_DETAIL,
        indices = [Index(value = [DBConstants.COLUMN_HOUSE_ID])],
        foreignKeys = [ForeignKey(entity = House::class,
                parentColumns = [DBConstants.COLUMN_ID],
                childColumns = [DBConstants.COLUMN_HOUSE_ID])])
data class HouseDetail(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = DBConstants.COLUMN_ID)
        var id: Int = 0,

        @NonNull
        @ColumnInfo(name = DBConstants.COLUMN_TITLE)
        var title: String = "",

        @ColumnInfo(name = DBConstants.COLUMN_CONTENTS)
        var contents: String = "",

        @ColumnInfo(name = DBConstants.COLUMN_DETAIL_URL)
        var url: String = "",

        @ColumnInfo(name = DBConstants.COLUMN_IMAGE_URL)
        var imageUrlString: String = "",

        @Ignore
        var imageUrls: String = "",

        @ColumnInfo(name = DBConstants.COLUMN_HOUSE_ID)
        var houseId: Int = 0
) : Parcelable