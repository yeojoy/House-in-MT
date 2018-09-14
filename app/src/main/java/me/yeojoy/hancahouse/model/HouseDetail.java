package me.yeojoy.hancahouse.model;

import java.util.ArrayList;
import java.util.List;

public class HouseDetail {
    private String mTitle;
    private String mContents;
    private String mUrl;
    private List<String> mImageUrls;

    public HouseDetail(String title, String url) {
        mTitle = title;
        mUrl = url;

        mContents = "";
        mImageUrls = new ArrayList<>();
    }

    public String getTitle() {
        return mTitle;
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

    public void setImageUrls(List<String> imageUrls) {
        mImageUrls.clear();
        mImageUrls.addAll(imageUrls);
    }

    public List<String> getImageUrls() {
        return mImageUrls;
    }

    @Override
    public String toString() {
        return "HouseDetail{" +
                "mTitle='" + mTitle + '\'' +
                ", mContents='" + mContents + '\'' +
                ", mUrl='" + mUrl + '\'' +
                ", mImageUrls=" + mImageUrls +
                '}';
    }
}