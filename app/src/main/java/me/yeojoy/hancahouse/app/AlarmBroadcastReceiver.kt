package me.yeojoy.hancahouse.app

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
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
import me.yeojoy.hancahouse.db.HancaDatabase
import me.yeojoy.hancahouse.model.House
import me.yeojoy.hancahouse.repository.HouseDBRepository
import java.text.SimpleDateFormat
import java.util.*

class AlarmBroadcastReceiver : BroadcastReceiver() {
    private val TAG = AlarmBroadcastReceiver::class.java.simpleName
    val CHANNEL_ID = "hanca"

    private var houseDBRepository: HouseDBRepository? = null

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d(TAG, "***************************************************************************")
        Log.d(TAG, "***************************************************************************")
        Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>> onReceive() <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
        Log.d(TAG, "***************************************************************************")
        Log.d(TAG, "***************************************************************************")
        houseDBRepository = HouseDBRepository(context)

        startCrawling(context)
    }

    private fun startCrawling(context: Context?) {
        Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>> startCrawling() <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
//        HouseNetworkRepository houseNetworkRepository = HouseNetworkRepository.getInstance()
//        houseNetworkRepository.loadPage(1, houses -> saveHousesToDatabase(context, houses))
    }

    private fun saveHousesToDatabase(context: Context, houses: MutableList<House>) {
        Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>> saveHousesToDatabase() <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")

        val hancaDatabase = HancaDatabase.getDatabase(context)
        val houseDao = hancaDatabase.houseDao()
        val allHouses = houseDao.getAllRawRents()

        if (allHouses == null || allHouses.isEmpty()) {
            Log.e(TAG, "There is no house.")
            return
        }

        val iterator = houses.iterator()
        while (iterator.hasNext()) {
            val house = iterator.next()
            if (allHouses.contains(house)) {
                Log.d(TAG, "UID, " + house.uid + ", is deleted.")
                iterator.remove()
            }
        }

        if (houses.isEmpty()) {
            Log.e(TAG, "There is no new house.")
            return
        }

        Log.d(TAG, "***************************************************************************")
        for (h in houses) {
            Log.d(TAG, h.toString())
        }
        Log.d(TAG, "***************************************************************************")

        houseDBRepository?.saveHouses(houses)
        notifyNewHouse(context, houses.size)

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