package me.yeojoy.hancahouse.model;

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import me.yeojoy.hancahouse.db.DBConstants

@Parcelize
@Entity(tableName = DBConstants.TABLE_NAME)
data class House(
        @PrimaryKey(autoGenerate = true)
        @NonNull
        @ColumnInfo(name = DBConstants.COLUMN_ID)
        var id: Int = 0,
        @ColumnInfo(name = DBConstants.COLUMN_TITLE)
        var title: String = "",
        @ColumnInfo(name = DBConstants.COLUMN_THUMBNAIL_URL)
        var thumbnailUrl: String = "",
        @ColumnInfo(name = DBConstants.COLUMN_DETAIL_URL)
        var detailUrl: String = "",
        @ColumnInfo(name = DBConstants.COLUMN_AUTHOR)
        var author: String = "",
        @ColumnInfo(name = DBConstants.COLUMN_DATE)
        var date: Long = 0L,
        @ColumnInfo(name = DBConstants.COLUMN_PARSED_TIME)
        var partedDateTime: String = "",
        @ColumnInfo(name = DBConstants.COLUMN_UID)
        var uid: Long = 0,
        @ColumnInfo(name = DBConstants.COLUMN_BOARD_TYPE)
        var boardType: Int = 0
) : Parcelable
