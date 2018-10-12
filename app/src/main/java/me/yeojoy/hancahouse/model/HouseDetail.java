package me.yeojoy.hancahouse.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.yeojoy.hancahouse.db.DBConstants;

@Entity(tableName = DBConstants.TABLE_NAME_DETAIL,
        foreignKeys = @ForeignKey(entity = House.class,
                parentColumns = DBConstants.COLUMN_ID,
                childColumns = DBConstants.COLUMN_HOUSE_ID))
public class HouseDetail implements DBConstants {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    private int mId;

    @NonNull
    @ColumnInfo(name = COLUMN_TITLE)
    private String mTitle;

    @ColumnInfo(name = COLUMN_CONTENTS)
    private String mContents;

    @ColumnInfo(name = COLUMN_DETAIL_URL)
    private String mUrl;

    @ColumnInfo(name = COLUMN_IMAGE_URL)
    private String mImageUrlString;

    @Ignore
    private List<String> mImageUrls;

    @ColumnInfo(name = COLUMN_HOUSE_ID)
    private int mHouseId;

    public HouseDetail(@NonNull String title, @NonNull String contents, String imageUrlString,
                       String detailUrl, int houseId) {
        mTitle = title;
        mContents = contents;
        mImageUrlString = imageUrlString;
        mUrl = detailUrl;
        mHouseId = houseId;
        mImageUrls = new ArrayList<>();
    }

    public HouseDetail() {
        mImageUrls = new ArrayList<>();
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(@NonNull String title) {
        mTitle = title;
    }

    public String getContents() {
        return mContents;
    }

    public void setContents(String contents) {
        mContents = contents;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public void setImageUrlString(String imageUrlString) {
        mImageUrlString = imageUrlString;
    }

    public void setImageUrls(List<String> imageUrls) {
        mImageUrls.clear();
        mImageUrls.addAll(imageUrls);
        mImageUrlString = new Gson().toJson(mImageUrls);
    }

    public List<String> getImageUrls() {
        if (mImageUrls != null && mImageUrls.size() < 1 &&
                !TextUtils.isEmpty(mImageUrlString)) {
            Type listType = new TypeToken<ArrayList<String>>(){}.getType();
            List<String> urls = new Gson().fromJson(mImageUrlString, listType);
            if (urls != null && urls.size() > 0) {
                mImageUrls.addAll(urls);
            }
        }

        return mImageUrls;
    }

    public int getId() {
        return mId;
    }

    public String getImageUrlString() {
        return mImageUrlString;
    }

    public int getHouseId() {
        return mHouseId;
    }

    public void setHouseId(int houseId) {
        mHouseId = houseId;
    }

    @Override
    public String toString() {
        return "HouseDetail{" +
                "mId=" + mId +
                ", mTitle='" + mTitle + '\'' +
                ", mContents='" + mContents + '\'' +
                ", mUrl='" + mUrl + '\'' +
                ", mImageUrls=" + mImageUrls +
                ", mImageUrlString='" + mImageUrlString + '\'' +
                ", mHouseId=" + mHouseId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HouseDetail)) return false;
        HouseDetail that = (HouseDetail) o;
        return getId() == that.getId() && getHouseId() == that.getHouseId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getContents(), getUrl(), getImageUrlString(), getImageUrls(), getHouseId());
    }
}