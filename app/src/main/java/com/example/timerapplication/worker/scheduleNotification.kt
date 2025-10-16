import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.timerapplication.worker.NotificationWorker
import com.example.timerapplication.data.TimerModel
import java.util.concurrent.TimeUnit


fun scheduleNotification(context: Context, timer: TimerModel) {
    val delayMillis = (timer.endTimeMillis - System.currentTimeMillis()) - (5 * 60 * 1000)

    if (delayMillis > 0) {
        val data = Data.Builder()
            .putString("TIMER_NAME", timer.name)
            .build()

        val request = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(data)
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            timer.id.toString(),
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}
