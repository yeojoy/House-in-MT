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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import me.yeojoy.hancahouse.app.Constants;
import me.yeojoy.hancahouse.app.adapter.HouseAdapter;
import me.yeojoy.hancahouse.model.House;
import me.yeojoy.hancahouse.util.AlarmUtil;
import me.yeojoy.hancahouse.util.PreferenceHelper;
import me.yeojoy.hancahouse.view.MainView;
import me.yeojoy.hancahouse.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity implements MainView, Constants {

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
            ((HouseAdapter) recyclerView.getAdapter()).setHouses(houses);
        });

        loadHouses();

        if (PreferenceHelper.isCrawlerStatusOn(this) &&
                !AlarmUtil.isAlarmManagerRunning(this)) {
            // Preference에는 돌아간다는 flag가 저정됐지만 실제 돌아가고 있지 않을 때에 실행해 준다.
            startCrawler();
        }
    }

    /**
     *
     */
    private void loadHouses() {
        Log.d(TAG, "loadHouses()");
        mMainViewModel.loadHouses(1);

        if (PreferenceHelper.isFirstLaunch(this)) {
            mMainViewModel.loadHouses(2);
            mMainViewModel.loadHouses(3);

            PreferenceHelper.setFirstLaunchFlag(this, true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu()");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        if (BuildConfig.DEBUG) {
            menu.add(0, R.id.check_alarm_manager, 2, R.string.check_crawler);
            menu.add(0, R.id.delete_all_from_table, 3, R.string.delete_all_from_table);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, "onPrepareOptionsMenu()");
        boolean isRunning = AlarmUtil.isAlarmManagerRunning(this);
        MenuItem startItem = menu.findItem(R.id.start_crawler);
        MenuItem stopItem = menu.findItem(R.id.stop_crawler);
        startItem.setVisible(!isRunning);
        stopItem.setVisible(isRunning);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected()");
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.go_setting:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;

            case R.id.start_crawler:
                startCrawler();
                Toast.makeText(this, R.string.toast_alarm_start, Toast.LENGTH_SHORT).show();
                return true;

            case R.id.stop_crawler:
                stopCrawler();
                Toast.makeText(this, R.string.toast_alarm_stop, Toast.LENGTH_SHORT).show();
                return true;

            case R.id.check_alarm_manager:
                checkCrawler();
                return true;

            case R.id.delete_all_from_table:
                mMainViewModel.deleteAllFromTable();
                PreferenceHelper.setFirstLaunchFlag(this, false);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startCrawler() {
        AlarmUtil.startCrawlerWithTime(this);
        PreferenceHelper.setCrawlerStatusOn(this);
    }

    private void stopCrawler() {
        AlarmUtil.stopCrawler(this);
        PreferenceHelper.setCrawlerStatusOff(this);
    }

    private void checkCrawler() {
        Toast.makeText(this, AlarmUtil.isAlarmManagerRunning(this) ?
                        R.string.toast_alarm_running : R.string.toast_alarm_stopped,
                Toast.LENGTH_SHORT).show();
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
