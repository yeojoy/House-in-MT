package me.yeojoy.hancahouse;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        mTextViewDescription.setMovementMethod(LinkMovementMethod.getInstance());

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
        if (TextUtils.isEmpty(houseDetail.getContents())) {
            return;
        }
        mTextViewDescription.setText(getDetailDescription(houseDetail.getContents()));

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

    private SpannableStringBuilder getDetailDescription(String contents) {
        Log.d(TAG, "contents : " + contents);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        int indexOfEmail = contents.indexOf(TITLE_EMAIL) + TITLE_EMAIL.length();
        int indexOfPhoneStart = contents.indexOf(TITLE_PHONE);
        int indexOfPhoneEnd = indexOfPhoneStart + TITLE_PHONE.length();
        int indexOfRegion = contents.indexOf(TITLE_REGION);

        spannableStringBuilder.append(contents.substring(0, indexOfEmail));
        spannableStringBuilder.append(" ");

        String email = contents.substring(indexOfEmail, indexOfPhoneStart);
        email = email.trim();

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            SpannableString emailSpannableString = new SpannableString(email);
            emailSpannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] { emailSpannableString.toString() });

//                intent.putExtra(Intent.EXTRA_SUBJECT, subject);

                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }

                    Toast.makeText(DetailHouseActivity.this, "email", Toast.LENGTH_SHORT).show();

                }
            }, 0, emailSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            spannableStringBuilder.append(emailSpannableString);
        } else {
            spannableStringBuilder.append(email);
        }

        spannableStringBuilder.append(System.lineSeparator());
        spannableStringBuilder.append(contents.substring(indexOfPhoneStart, indexOfPhoneEnd));
        spannableStringBuilder.append(" ");

        String phoneNumber = contents.substring(indexOfPhoneEnd, indexOfRegion);
        phoneNumber = phoneNumber.trim();
        if (Patterns.PHONE.matcher(phoneNumber).matches()) {
            SpannableString phoneSpannableString = new SpannableString(phoneNumber);
            phoneSpannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(DetailHouseActivity.this, "phone", Toast.LENGTH_SHORT).show();
                }
            }, 0, phoneSpannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.append(phoneSpannableString);
        } else {
            spannableStringBuilder.append(phoneNumber);
        }

        spannableStringBuilder.append(System.lineSeparator());
        spannableStringBuilder.append(contents.substring(indexOfRegion));


        return spannableStringBuilder;
    }
}
