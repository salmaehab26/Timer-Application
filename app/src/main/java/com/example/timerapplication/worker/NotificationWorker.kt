package com.example.timerapplication.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.timerapplication.R

class NotificationWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val title = inputData.getString(KEY_TITLE) ?: "Timer Reminder"
        val message = inputData.getString(KEY_MESSAGE) ?: "5 minutes remaining"
        showNotification(title, message)
        return Result.success()
    }

     fun showNotification(title: String, message: String) {
        val channelId = "timer_channel_id"
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "Timer Alerts", NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        }

        val notif = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.notification_bell)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        manager.notify(title.hashCode() + (System.currentTimeMillis() % 1000).toInt(), notif)
    }

    companion object {
        const val KEY_TITLE = "title"
        const val KEY_MESSAGE = "message"
    }
}