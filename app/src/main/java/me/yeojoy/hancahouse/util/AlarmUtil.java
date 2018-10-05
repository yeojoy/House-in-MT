package me.yeojoy.hancahouse.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;

import me.yeojoy.hancahouse.BuildConfig;
import me.yeojoy.hancahouse.app.AlarmBroadcaseReceiver;
import me.yeojoy.hancahouse.app.Constants;

public class AlarmUtil implements Constants {
    private static final String TAG = AlarmUtil.class.getSimpleName();

    public static void startCrawlerWithTime(Context context) {
        Log.i(TAG, "startCrawlerWithTime()");

        PendingIntent pendingIntent = getBroadcastPendingIntent(context);

        final int TIME = PreferenceHelper.getCrawlerDurationTime(context);
        Log.d(TAG, "TIME : " + TIME);

        long duration = TIME * (BuildConfig.DEBUG ? DateUtils.MINUTE_IN_MILLIS : DateUtils.HOUR_IN_MILLIS);

        // AlarmManager 호출
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (manager != null) {
            manager.cancel(pendingIntent);
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0,
                    duration, pendingIntent);
        }
    }

    public static void stopCrawler(Context context) {
        Log.i(TAG, "stopCrawler()");
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (manager != null) {
            PendingIntent pendingIntent = getBroadcastPendingIntent(context);
            manager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    public static boolean isAlarmManagerRunning(Context context) {
        Log.i(TAG, "isAlarmManagerRunning()");
        Intent intent = new Intent(context, AlarmBroadcaseReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_NO_CREATE);

        return pendingIntent != null;
    }

    private static PendingIntent getBroadcastPendingIntent(Context context) {
        Intent intent = new Intent(context, AlarmBroadcaseReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        return pendingIntent;
    }
}
