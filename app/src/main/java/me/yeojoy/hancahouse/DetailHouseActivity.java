package me.yeojoy.hancahouse;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.yeojoy.hancahouse.app.Constants;
import me.yeojoy.hancahouse.app.GlideApp;
import me.yeojoy.hancahouse.model.House;
import me.yeojoy.hancahouse.model.HouseDetail;
import me.yeojoy.hancahouse.viewmodel.DetailViewModel;

public class DetailHouseActivity extends AppCompatActivity implements Constants {
    private static final String TAG = DetailHouseActivity.class.getSimpleName();

    private ImageView mImageViewThumbnail;

    private TextView mTextViewAuthor, mTextViewTitle, mTextViewDescription;

    private LinearLayout mLinearLayoutImages;

    private DetailViewModel mDetailViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_house);

        Intent intent = getIntent();
        if (intent == null) finish();

        House house = getIntent().getParcelableExtra(House.class.getSimpleName());

        if (house == null) finish();

        mDetailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);

        HouseDetail houseDetail = new HouseDetail(house.getTitle(), house.getUrl());
        mDetailViewModel.getHouseDetailLiveData().setValue(houseDetail);
        mDetailViewModel.getHouseDetailLiveData().observe(this, this::bindDataToView);

        mImageViewThumbnail = findViewById(R.id.image_view_thumbnail);

        mTextViewAuthor = findViewById(R.id.text_view_author);
        mTextViewTitle = findViewById(R.id.text_view_title);
        mTextViewDescription = findViewById(R.id.text_view_description);

        mLinearLayoutImages = findViewById(R.id.linear_layout_images);

        if (!TextUtils.isEmpty(house.getThumbnailUrl()) &&
                !house.getThumbnailUrl().equals(NO_IMAGE)) {
            GlideApp.with(this)
                    .load(house.getThumbnailUrl())
                    .into(mImageViewThumbnail);
        }


        mTextViewAuthor.setText(house.getAuthor());
        mTextViewTitle.setText(house.getTitle());
    }

    private void bindDataToView(HouseDetail houseDetail) {
        mTextViewDescription.setText(houseDetail.getContents());

        mLinearLayoutImages.removeAllViews();

        for (String url : houseDetail.getImageUrls()) {

            String imageUrl = HOST + url;
            Log.d(TAG, "baindDataToView imageUrl >> " + imageUrl);
            ImageView imageView = new ImageView(this);
            GlideApp.with(this)
                    .load(imageUrl)
                    .into(imageView);

            mLinearLayoutImages.addView(imageView, getImageLayoutParams());
        }
    }

    private LinearLayout.LayoutParams getImageLayoutParams() {
        LinearLayout.LayoutParams params
                = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.margin_bottom_detail_image);

        return params;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDetailViewModel.loadPage(mDetailViewModel.getHouseDetailLiveData().getValue());
    }
}
