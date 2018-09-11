package me.yeojoy.hancahouse.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Objects;

import me.yeojoy.hancahouse.db.DBConstants;

@Entity(tableName = DBConstants.TABLE_NAME)
public class House implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId = -1;

    @NonNull
    @ColumnInfo(name = "title")
    private String mTitle;

    @NonNull
    @ColumnInfo(name = "thumbnail_url")
    private String mThumbnailUrl;

    /**
     * detail of house information webpage url
     */
    @NonNull
    @ColumnInfo(name = "url")
    private String mUrl;

    @ColumnInfo(name = "date")
    private long mDate;

    /**
     * unique id in board
     */
    @ColumnInfo(name = "uid")
    private long mUid;

    public House(@NonNull String title, @NonNull String thumbnailUrl, @NonNull String url, long date, long uid) {
        mTitle = title;
        mThumbnailUrl = thumbnailUrl;
        mUrl = url;
        mDate = date;
        mUid = uid;
    }

    private House(Parcel in) {
        mId = in.readInt();
        mTitle = in.readString();
        mThumbnailUrl = in.readString();
        mUrl = in.readString();
        mDate = in.readLong();
        mUid = in.readLong();
    }

    public static final Creator<House> CREATOR = new Creator<House>() {
        @Override
        public House createFromParcel(Parcel in) {
            return new House(in);
        }

        @Override
        public House[] newArray(int size) {
            return new House[size];
        }
    };

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public String getUrl() {
        return mUrl;
    }

    public long getDate() {
        return mDate;
    }

    public long getUid() {
        return mUid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mThumbnailUrl);
        parcel.writeString(mUrl);
        parcel.writeLong(mDate);
        parcel.writeLong(mUid);
    }

    @Override
    public String toString() {
        return "House{" +
                "mId=" + mId +
                ", mTitle='" + mTitle + '\'' +
                ", mThumbnailUrl='" + mThumbnailUrl + '\'' +
                ", mUrl='" + mUrl + '\'' +
                ", mDate=" + mDate +
                ", mUid=" + mUid +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof House)) return false;
        House house = (House) o;
        return getUid() == house.getUid();
    }

    @Override
    public int hashCode() {

        return Objects.hash(this);
    }

    public void setId(int id) {
        mId = id;
    }
}
