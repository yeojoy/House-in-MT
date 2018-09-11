package me.yeojoy.hancahouse;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import me.yeojoy.hancahouse.model.House;
import me.yeojoy.hancahouse.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MainViewModel mMainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        FloatingActionButton floatingActionButton = findViewById(R.id.floating_action_button_refresh);
        floatingActionButton.setOnClickListener(view -> {

        });

        // mModel = ViewModelProviders.of(this).get(NameViewModel.class);
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainViewModel.loadData(1);

        mMainViewModel.getHouses().observe(this, data -> {
            Log.d(TAG, "=================================================================");
            for (House house : data) {
                Log.d(TAG, house.toString());
            }
            Log.d(TAG, "=================================================================");
        });
    }
}
