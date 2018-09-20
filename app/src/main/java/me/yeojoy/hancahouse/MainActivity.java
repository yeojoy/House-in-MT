package me.yeojoy.hancahouse;

import android.app.ActivityOptions;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import me.yeojoy.hancahouse.app.adapter.HouseAdapter;
import me.yeojoy.hancahouse.model.House;
import me.yeojoy.hancahouse.view.MainView;
import me.yeojoy.hancahouse.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity implements MainView {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MainViewModel mMainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.toolbar));

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(new HouseAdapter(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton floatingActionButton = findViewById(R.id.floating_action_button_refresh);
        floatingActionButton.setOnClickListener(view -> mMainViewModel.loadData(1));

        // mModel = ViewModelProviders.of(this).get(NameViewModel.class);
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainViewModel.loadData(1);

        mMainViewModel.getHouses().observe(this, houses -> {
            Log.d(TAG, "=================================================================");
            for (House house : houses) {
                Log.d(TAG, house.toString());
            }

            ((HouseAdapter) recyclerView.getAdapter()).setHouses(houses);
            Log.d(TAG, "=================================================================");
        });
    }

    @Override
    public void onItemClick(ImageView imageView, House house) {
        ActivityOptions activityOptions =
                ActivityOptions.makeSceneTransitionAnimation(
                        this, imageView, "logo");
        Intent intent = new Intent(this, DetailHouseActivity.class);
        intent.putExtra(House.class.getSimpleName(), house);
        startActivity(intent, activityOptions.toBundle());
    }
}
