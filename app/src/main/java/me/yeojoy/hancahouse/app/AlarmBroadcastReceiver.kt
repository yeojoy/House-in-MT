package me.yeojoy.hancahouse.app

import android.annotation.TargetApi
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import me.yeojoy.hancahouse.BuildConfig
import me.yeojoy.hancahouse.MainActivity
import me.yeojoy.hancahouse.R
import java.text.SimpleDateFormat
import java.util.*

class AlarmBroadcastReceiver : BroadcastReceiver() {
    private val TAG = AlarmBroadcastReceiver::class.java.simpleName
    val CHANNEL_ID = "hanca"

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d(TAG, "***************************************************************************")
        Log.d(TAG, "***************************************************************************")
        Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>> onReceive() <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
        Log.d(TAG, "***************************************************************************")
        Log.d(TAG, "***************************************************************************")
    }

    @SuppressWarnings("deprecation")
    private fun notifyNewHouse(context: Context, count: Int) {
        Log.d(TAG, "notifyNewHouse(), count : " + count)
        // Create an Intent for the activity you want to start

        val resultIntent = Intent(context, MainActivity::class.java)
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addNextIntentWithParentStack(resultIntent)
        // Get the PendingIntent containing the entire back stack
        val resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_home_white_40dp)
        val contentText = if (BuildConfig.DEBUG) {
            val date = Date()
            "${context.getString(R.string.notification_content_formatter, count)} ${SimpleDateFormat(Constants.PARSED_TIME_FORMATTER, Locale.getDefault()).format(date)}"
        } else {
            context.getString(R.string.notification_content_formatter, count)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // add Notification CHANNEL ID
            val newMessageNotificationBuilder = Notification.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_home_white_40dp)
                    .setContentTitle(context.getText(R.string.notification_title))
                    .setContentText(contentText)
                    .setContentIntent(resultPendingIntent)
                    .setLargeIcon(bitmap)
                    .setAutoCancel(true)

            showNotificationMoreThanOreo(context, newMessageNotificationBuilder)
        } else {
            val newMessageNotificationBuilder = NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_home_white_40dp)
                    .setContentTitle(context.getText(R.string.notification_title))
                    .setContentText(contentText)
                    .setContentIntent(resultPendingIntent)
                    .setLargeIcon(bitmap)
                    .setAutoCancel(true)
            showNotification(context, newMessageNotificationBuilder)
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun showNotificationMoreThanOreo(context: Context, newMessageNotificationBuilder: Notification.Builder) {

        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(CHANNEL_ID, "New", NotificationManager.IMPORTANCE_DEFAULT).apply {
            // description = descriptionText
        }

        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(1, newMessageNotificationBuilder.build())
    }

    private fun showNotification(context: Context, newMessageNotificationBuilder: NotificationCompat.Builder) {
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1, newMessageNotificationBuilder.build())
    }
}
