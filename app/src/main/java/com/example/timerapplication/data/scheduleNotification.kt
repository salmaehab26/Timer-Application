import android.app.Application
import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.timerapplication.data.NotificationWorker
import com.example.timerapplication.data.TimerModel
import java.util.concurrent.TimeUnit


fun scheduleNotification(context: Context, timer: TimerModel) {
    val delay = timer.endTimeMillis - System.currentTimeMillis() - 5 * 60 * 1000

    if (delay <= 0) return // no need to schedule past timers

    val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setInputData(
            workDataOf(
                "title" to "Timer Reminder",
                "message" to "Your timer '${timer.name}' ends in 5 minutes!"
            )
        )
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}