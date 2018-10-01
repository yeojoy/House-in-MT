package me.yeojoy.hancahouse;

import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import me.yeojoy.hancahouse.app.AlarmBroadcaseReceiver;
import me.yeojoy.hancahouse.app.Constants;
import me.yeojoy.hancahouse.app.adapter.HouseAdapter;
import me.yeojoy.hancahouse.model.House;
import me.yeojoy.hancahouse.util.PreferenceUtil;
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
        floatingActionButton.setOnClickListener(view -> loadHouses());

        // mModel = ViewModelProviders.of(this).get(NameViewModel.class);
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mMainViewModel.getRents().observe(this, houses -> {
            Log.d(TAG, "=================================================================");
            for (House house : houses) {
                Log.d(TAG, house.toString());
            }

            ((HouseAdapter) recyclerView.getAdapter()).setHouses(houses);
            Log.d(TAG, "=================================================================");
        });

        loadHouses();
    }

    /**
     *
     */
    private void loadHouses() {
        Log.d(TAG, "loadHouses()");
        mMainViewModel.loadHouses(1);

        if (PreferenceUtil.getInstance(this).getInt(Constants.KEY_INITIALIZE_PAGE, -1) != 1) {
            mMainViewModel.loadHouses(2);
            mMainViewModel.loadHouses(3);

            PreferenceUtil.getInstance(this).putInt(Constants.KEY_INITIALIZE_PAGE, 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        if (BuildConfig.DEBUG) {
            menu.add(0, R.id.delete_all_from_table, 0, R.string.delete_all_from_table);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.start_crawler:
                startCrawler();
                return true;

            case R.id.delete_all_from_table:
                mMainViewModel.deleteAllFromTable();
                PreferenceUtil.getInstance(this).putInt(Constants.KEY_INITIALIZE_PAGE, -1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startCrawler() {
        Log.d(TAG, "startCrawler()");
        Intent intent = new Intent(this, AlarmBroadcaseReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // AlarmManager 호출
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (manager != null) {
            manager.cancel(pendingIntent);
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0,
                    DateUtils.HOUR_IN_MILLIS * 2, pendingIntent);
        }
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
