package me.yeojoy.hancahouse.detail;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import me.yeojoy.hancahouse.BuildConfig;
import me.yeojoy.hancahouse.R;
import me.yeojoy.hancahouse.WebActivity;
import me.yeojoy.hancahouse.app.Constants;
import me.yeojoy.hancahouse.model.House;
import me.yeojoy.hancahouse.model.HouseDetail;

public class DetailHouseActivity extends AppCompatActivity {
    private static final String TAG = DetailHouseActivity.class.getSimpleName();

    private TextView textViewDescription;
    private ProgressBar progressBarLoading;

    private LinearLayout linearLayoutImages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_house);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        House house = getIntent().getParcelableExtra(House.class.getSimpleName());
        Log.d(TAG, "DETAIL ACTIVITY >>> " + house.toString());

        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        params.height = getResources().getDisplayMetrics().widthPixels;
        appBarLayout.setLayoutParams(params);

        ImageView imageViewThumbnail = findViewById(R.id.image_view_thumbnail);

        TextView textViewTitle = findViewById(R.id.text_view_title);
        textViewDescription = findViewById(R.id.text_view_description);
        textViewDescription.setMovementMethod(LinkMovementMethod.getInstance());

        progressBarLoading = findViewById(R.id.progress_bar_loading);

        linearLayoutImages = findViewById(R.id.linear_layout_images);

        if (!TextUtils.isEmpty(house.getThumbnailUrl()) &&
                !house.getThumbnailUrl().equals(Constants.NO_IMAGE)) {

            final String SIZE = "-120x90";
            String imageUrl = house.getThumbnailUrl().replace(SIZE, "");
            Glide.with(this)
                    .load(imageUrl)
                    .centerCrop()
                    .into(imageViewThumbnail);
        }
        textViewTitle.setText(house.getTitle());

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(house.getAuthor());

        /*
        mDetailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        mDetailViewModel.loadPage(house.getId(), house.getTitle(), house.getUrl());

        mDetailViewModel.getHouseDetailLiveData(house.getId())
                .observe(this, this::bindDataToView);

        mDetailViewModel.getAllHouseDetails().observe(this, allHouseDetails -> {
            if (allHouseDetails == null) return;

            Log.e(TAG, "########################################################################");
            for (HouseDetail houseDetail : allHouseDetails) {
                Log.e(TAG, houseDetail.getHouseId() + ", " + house.getTitle());
            }
            Log.e(TAG, "########################################################################");
        });

        MutableLiveData<House> houseMutableLiveData = new MutableLiveData<>();
        houseMutableLiveData.setValue(house);
        mDetailViewModel.setHouseLiveData(houseMutableLiveData);
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
//        HouseDetail houseDetail = mDetailViewModel.getHouseDetailLiveData().getValue();

//        if (houseDetail != null && !TextUtils.isEmpty(houseDetail.getContents())) {
//            mProgressBarLoading.setVisibility(View.GONE);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        if (BuildConfig.DEBUG) {
            menu.add(0, R.id.delete_all_from_table, 2, R.string.delete_all_from_table);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.go_web_page: {
//                String url = mDetailViewModel.getHouseLiveData().getValue().getUrl();
                String url = "https://google.com/images";
                Log.d(TAG, "url : " + url);

                if (TextUtils.isEmpty(url)) {
                    Toast.makeText(this, R.string.toast_no_detail_url, Toast.LENGTH_SHORT)
                            .show();
                    return false;
                }

                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra(Constants.KEY_INTENT_URL, url);
                startActivity(intent);
                return true;
            }
            case R.id.delete_all_from_table: {
//                mDetailViewModel.deleteAllItems();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindDataToView(HouseDetail houseDetail) {
        Log.i(TAG, "bindDataToView()");
        if (houseDetail == null) {
            Log.e(TAG, "houseDetail is null.");
            return;
        }

        if (TextUtils.isEmpty(houseDetail.getContents())) {
            Log.e(TAG, "houseDetail's contents is null.");
            return;
        }

        progressBarLoading.setVisibility(View.GONE);

        textViewDescription.setText(getDetailDescription(houseDetail.getContents()));

        linearLayoutImages.removeAllViews();
        /*
        for (String url : houseDetail.getImageUrls()) {

            String imageUrl = Constants.HOST + url;
            Log.d(TAG, "baindDataToView imageUrl >> " + imageUrl);
            ImageView imageView = new ImageView(this);
            Glide.with(this)
                    .load(imageUrl)
                    .into(imageView);

            mLinearLayoutImages.addView(imageView, getImageLayoutParams());
        }
        */
    }

    private LinearLayout.LayoutParams getImageLayoutParams() {
        LinearLayout.LayoutParams params
                = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.margin_bottom_detail_image);

        return params;
    }

    private SpannableStringBuilder getDetailDescription(String contents) {
        Log.d(TAG, "contents : " + contents);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        int indexOfEmail = contents.indexOf(Constants.TITLE_EMAIL) + Constants.TITLE_EMAIL.length();
        int indexOfPhoneStart = contents.indexOf(Constants.TITLE_PHONE);
        int indexOfPhoneEnd = indexOfPhoneStart + Constants.TITLE_PHONE.length();
        int indexOfRegion = contents.indexOf(Constants.TITLE_REGION);

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
        String finalPhoneNumber = phoneNumber.trim();
        if (Patterns.PHONE.matcher(finalPhoneNumber).matches()) {
            SpannableString phoneSpannableString = new SpannableString(finalPhoneNumber);
            phoneSpannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + finalPhoneNumber));

                    if (intent.resolveActivity(getPackageManager()) != null) {
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(DetailHouseActivity.this, R.string.warn_cannot_call, Toast.LENGTH_SHORT).show();
                        }
                    }

                    Toast.makeText(DetailHouseActivity.this, "phone", Toast.LENGTH_SHORT).show();
                }
            }, 0, phoneSpannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.append(phoneSpannableString);
        } else {
            spannableStringBuilder.append(finalPhoneNumber);
        }

        spannableStringBuilder.append(System.lineSeparator());
        spannableStringBuilder.append(contents.substring(indexOfRegion));


        return spannableStringBuilder;
    }
}
