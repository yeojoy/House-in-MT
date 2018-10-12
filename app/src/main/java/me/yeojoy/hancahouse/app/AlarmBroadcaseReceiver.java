package me.yeojoy.hancahouse.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import me.yeojoy.hancahouse.BuildConfig;
import me.yeojoy.hancahouse.MainActivity;
import me.yeojoy.hancahouse.R;
import me.yeojoy.hancahouse.db.HancaDatabase;
import me.yeojoy.hancahouse.db.HouseDao;
import me.yeojoy.hancahouse.model.House;
import me.yeojoy.hancahouse.repository.HouseDBRepository;
import me.yeojoy.hancahouse.repository.HouseNetworkRepository;

public class AlarmBroadcaseReceiver extends BroadcastReceiver {
    private static final String TAG = AlarmBroadcaseReceiver.class.getSimpleName();

    private static final String CHANNEL_ID = "hanca";
    private HouseDBRepository mHouseDBRepository;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "***************************************************************************");
        Log.d(TAG, "***************************************************************************");
        Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>> onReceive() <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        Log.d(TAG, "***************************************************************************");
        Log.d(TAG, "***************************************************************************");
        mHouseDBRepository = new HouseDBRepository(context.getApplicationContext());

        startCrawling(context);
    }

    private void startCrawling(Context context) {
        Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>> startCrawling() <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        HouseNetworkRepository houseNetworkRepository = HouseNetworkRepository.getInstance();
        houseNetworkRepository.loadPage(1, houses -> saveHousesToDatabase(context, houses));
    }

    private void saveHousesToDatabase(Context context, List<House> houses) {
        Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>> saveHousesToDatabase() <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        HancaDatabase hancaDatabase = HancaDatabase.getDatabase(context);
        HouseDao houseDao = hancaDatabase.houseDao();
        List<House> mAllHouses = houseDao.getAllRawRents();

        if (mAllHouses == null || mAllHouses.size() < 1) {
            Log.e(TAG, "There is no house.");
            return;
        }

        Iterator<House> iterator = houses.iterator();
        while (iterator.hasNext()) {
            House house = iterator.next();
            if (mAllHouses.contains(house)) {
                Log.d(TAG, "UID, " + house.getUid() + ", is deleted.");
                iterator.remove();
            }
        }

        if (houses.size() < 1) {
            Log.e(TAG, "There is no new house.");
            return;
        }

        Log.d(TAG, "***************************************************************************");
        for (House h : houses) {
            Log.d(TAG, h.toString());
        }
        Log.d(TAG, "***************************************************************************");

        mHouseDBRepository.saveHouses(houses);
        notifyNewHouse(context, houses.size());

    }

    private void notifyNewHouse(final Context context, final int count) {
        Log.d(TAG, "notifyNewHouse(), count : " + count);
        // Create an Intent for the activity you want to start

        Intent resultIntent = new Intent(context, MainActivity.class);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_home_white_40dp);
        String contentText = context.getString(R.string.notification_content_formatter, count);
        if (BuildConfig.DEBUG) {
            Date date = new Date();

            contentText += " " + new SimpleDateFormat(Constants.PARSED_TIME_FORMATTER, Locale.getDefault()).format(date);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // add Notification CHANNEL ID
            Notification.Builder newMessageNotificationBuilder = new Notification.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_home_white_40dp)
                    .setContentTitle(context.getText(R.string.notification_title))
                    .setContentText(contentText)
                    .setContentIntent(resultPendingIntent)
                    .setLargeIcon(bitmap)
                    .setAutoCancel(true);

            showNotificationMoreThanOreo(context, newMessageNotificationBuilder);
        } else {
            NotificationCompat.Builder newMessageNotificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_home_white_40dp)
                    .setContentTitle(context.getText(R.string.notification_title))
                    .setContentText(contentText)
                    .setContentIntent(resultPendingIntent)
                    .setLargeIcon(bitmap)
                    .setAutoCancel(true);
            showNotification(context, newMessageNotificationBuilder);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void showNotificationMoreThanOreo(Context context, Notification.Builder newMessageNotificationBuilder) {

        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "New", NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);

        notificationManager.notify(1, newMessageNotificationBuilder.build());
    }

    private void showNotification(Context context, NotificationCompat.Builder newMessageNotificationBuilder) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, newMessageNotificationBuilder.build());
    }
}
