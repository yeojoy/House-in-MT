package me.yeojoy.hancahouse.app;

import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Iterator;
import java.util.List;

import me.yeojoy.hancahouse.MainActivity;
import me.yeojoy.hancahouse.R;
import me.yeojoy.hancahouse.model.House;
import me.yeojoy.hancahouse.repository.HouseDBRepository;
import me.yeojoy.hancahouse.repository.HouseNetworkRepository;

public class CrawlerService extends Service {

    private static final String TAG = CrawlerService.class.getSimpleName();

    private static final String CHANNEL_ID = "hanca";
    private HouseDBRepository mHouseDBRepository;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHouseDBRepository = new HouseDBRepository(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startCrawling();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startCrawling() {
        Log.d(TAG, "===========================================================================");
        Log.d(TAG, "===========================================================================");
        Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>> startCrawling() <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        Log.d(TAG, "===========================================================================");
        Log.d(TAG, "===========================================================================");
        HouseNetworkRepository houseNetworkRepository = HouseNetworkRepository.getInstance();
        houseNetworkRepository.loadPage(1, 1, this::saveHousesToDatabase);
    }

    private void saveHousesToDatabase(List<House> houses) {
        Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>> saveHousesToDatabase() <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        List<House> allHouses = mHouseDBRepository.getAllHouses().getValue();

        if (allHouses != null && allHouses.size() > 0) {
            Iterator<House> iterator = houses.iterator();
            while (iterator.hasNext()) {
                House house = iterator.next();
                if (allHouses.contains(house)) {
                    Log.d(TAG, "UID, " + house.getUid() + ", is deleted.");
                    iterator.remove();
                }
            }
        }

        Log.d(TAG, "===========================================================================");
        for (House h : houses) {
            Log.d(TAG, h.toString());
        }
        Log.d(TAG, "===========================================================================");

        mHouseDBRepository.saveHouses(houses);
        notifyNewHouse(houses.size());

        stopSelf();
    }

    private void notifyNewHouse(int count) {
        Log.d(TAG, "notifyNewHouse(), count : " + count);
        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(this, MainActivity.class);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_home_default);
        NotificationCompat.Builder newMessageNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_home_default)
                .setContentTitle(getText(R.string.notification_title))
                .setContentText(getString(R.string.notification_content, count))
                .setContentIntent(resultPendingIntent)
                .setLargeIcon(bitmap)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // TODO add Notification CHANNEL ID
            newMessageNotificationBuilder.setChannelId(CHANNEL_ID);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, newMessageNotificationBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
