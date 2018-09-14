package me.yeojoy.hancahouse;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.yeojoy.hancahouse.app.GlideApp;
import me.yeojoy.hancahouse.model.House;

public class DetailHouseActivity extends AppCompatActivity {
    private static final String TAG = DetailHouseActivity.class.getSimpleName();

    private ImageView mImageViewThumbnail;

    private TextView mTextViewAuthor, mTextViewTitle, mTextViewDescription;

    private LinearLayout mLinearLayoutImages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_house);

        Intent intent = getIntent();
        if (intent == null) finish();

        House house = getIntent().getParcelableExtra(House.class.getSimpleName());

        mImageViewThumbnail = findViewById(R.id.image_view_thumbnail);

        mTextViewAuthor = findViewById(R.id.text_view_author);
        mTextViewTitle = findViewById(R.id.text_view_title);
        mTextViewDescription = findViewById(R.id.text_view_description);

        mLinearLayoutImages = findViewById(R.id.linear_layout_images);

        GlideApp.with(this)
                .load(house.getThumbnailUrl())
                .into(mImageViewThumbnail);

        mTextViewAuthor.setText(house.getAuthor());
        mTextViewTitle.setText(house.getTitle());
    }
}
