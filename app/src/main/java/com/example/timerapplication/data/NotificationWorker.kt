package com.example.timerapplication.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.timerapplication.R

class NotificationWorker(  context: Context,
                           workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val timerName = inputData.getString("TIMER_NAME") ?: "Timer"
        showNotification(timerName)
        return Result.success()
    }

    private fun showNotification(timerName: String) {
        val channelId = "timer_channel"
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Timer Alerts", NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Timer Reminder")
            .setContentText("Your timer '$timerName' will finish in 5 minutes!")
            .setSmallIcon(R.drawable.notification_bell)
            .setAutoCancel(true)
            .build()

        manager.notify(timerName.hashCode(), notification)
    }
}