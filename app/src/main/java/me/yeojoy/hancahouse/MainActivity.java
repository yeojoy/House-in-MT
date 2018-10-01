package me.yeojoy.hancahouse;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TimeUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import javax.xml.transform.OutputKeys;

import me.yeojoy.hancahouse.app.CrawlerService;
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
        floatingActionButton.setOnClickListener(view -> mMainViewModel.loadHouses(1));

        // mModel = ViewModelProviders.of(this).get(NameViewModel.class);
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainViewModel.loadHouses(1);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.start_crawler:
                startCrawler();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(android.os.Build.VERSION_CODES.M)
    private void startCrawler() {
        Log.d(TAG, "startCrawler()");
        Intent intent = new Intent(this, CrawlerService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // AlarmManager 호출
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, DateUtils.HOUR_IN_MILLIS * 3,
                DateUtils.HOUR_IN_MILLIS * 3, pendingIntent);
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
