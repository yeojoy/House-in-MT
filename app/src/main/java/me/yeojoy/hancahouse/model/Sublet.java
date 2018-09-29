package me.yeojoy.hancahouse.model;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import me.yeojoy.hancahouse.db.DBConstants;

@Entity(tableName = DBConstants.TABLE_NAME_FOR_SUBLET)
public class Sublet extends House {


    public Sublet(@NonNull String title, @NonNull String thumbnailUrl, @NonNull String url, String author, long date, long uid) {
        super(title, thumbnailUrl, url, author, date, uid);
    }
}
