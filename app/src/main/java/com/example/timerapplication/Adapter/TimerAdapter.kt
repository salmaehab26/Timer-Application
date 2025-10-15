import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.timerapplication.Adapter.DIffUtils
import com.example.timerapplication.data.TimerModel
import com.example.timerapplication.databinding.TimerItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class TimerAdapter : RecyclerView.Adapter<TimerAdapter.ViewHolder>() {

    private val timerList = mutableListOf<TimerModel>()
    private var countdownJob: Job? = null

    inner class ViewHolder(val binding: TimerItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TimerModel) {
            binding.textName.text = item.name
            binding.textDescr.text = item.description
            binding.textTime.text = formatMillis(item.remainingTime)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TimerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(timerList[position])
    }

    override fun getItemCount(): Int = timerList.size

    fun setTimers(newList: List<TimerModel>) {
        val diff = DIffUtils(timerList, newList)
        val result = DiffUtil.calculateDiff(diff)
        timerList.clear()
        timerList.addAll(newList)
        result.dispatchUpdatesTo(this)

        startCountdowns()
    }

    private fun startCountdowns() {
        countdownJob?.cancel()
        countdownJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                val now = System.currentTimeMillis()
                for ((index, timer) in timerList.withIndex()) {
                    val newRemaining = (timer.endTimeMillis - now).coerceAtLeast(0)
                    if (newRemaining != timer.remainingTime) {
                        timer.remainingTime = newRemaining
                        notifyItemChanged(index, "timeOnly") // partial update
                    }
                }
                delay(1000)
            }
        }
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.contains("timeOnly")) {
            holder.binding.textTime.text = formatMillis(timerList[position].remainingTime)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    private fun formatMillis(ms: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(ms)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(ms) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
