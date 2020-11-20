package me.yeojoy.hancahouse.app.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.yeojoy.hancahouse.BuildConfig;
import me.yeojoy.hancahouse.MainContract;
import me.yeojoy.hancahouse.MainPresenter;
import me.yeojoy.hancahouse.R;
import me.yeojoy.hancahouse.app.Constants;
import me.yeojoy.hancahouse.app.GlideApp;
import me.yeojoy.hancahouse.model.House;
import me.yeojoy.hancahouse.view.MainView;

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.HouseViewHolder> {

    private List<House> mHouses;
    private MainView mMainView;

    public HouseAdapter(MainContract.Presenter presenter) {
        mHouses = new ArrayList<>();
//        mMainView = mainView;
    }

    @NonNull
    @Override
    public HouseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_house, viewGroup, false);
        return new HouseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HouseViewHolder holder, int i) {
        House house = mHouses.get(i);

        if (!TextUtils.isEmpty(house.getThumbnailUrl()) &&
                !Constants.NO_IMAGE.equals(house.getThumbnailUrl())) {
            GlideApp.with(holder.mImageViewThumbnail)
                    .load(house.getThumbnailUrl())
                    .fitCenter()
                    // icon from https://www.brandeps.com/icon/H/Home-01-thanks?filetype=zip
                    .placeholder(R.drawable.ic_home_black_24dp)
                    .into(holder.mImageViewThumbnail);
        } else {
            holder.mImageViewThumbnail.setImageResource(R.drawable.ic_home_black_24dp);
        }

        SimpleDateFormat simpleDateFormat
                = new SimpleDateFormat(Constants.APP_DATE_FORMAT, Locale.getDefault());
        holder.mTextViewTitle.setText(house.getTitle());
        holder.mTextViewDate.setText(simpleDateFormat.format(new Date(house.getDate())));

        String author = house.getAuthor() == null ? "" : house.getAuthor();
        if (BuildConfig.DEBUG) {
            author = author + " >>> uid : " + String.valueOf(house.getUid());
        }

        holder.mTextViewAuthor.setText(author);

        holder.itemView.setOnClickListener(view -> mMainView.onItemClick(holder.mImageViewThumbnail, house));
    }

    @Override
    public int getItemCount() {
        return mHouses.size();
    }

    public void setHouses(List<House> houses) {
        mHouses.clear();
        mHouses.addAll(houses);
        notifyDataSetChanged();
    }

    static class HouseViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageViewThumbnail;
        TextView mTextViewTitle, mTextViewDate, mTextViewAuthor;

        HouseViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageViewThumbnail = itemView.findViewById(R.id.image_view_thumbnail);
            mTextViewTitle = itemView.findViewById(R.id.text_view_title);
            mTextViewDate = itemView.findViewById(R.id.text_view_date);
            mTextViewAuthor = itemView.findViewById(R.id.text_view_author);
        }
    }
}
