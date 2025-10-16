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

class TimerAdapter(private val onDelete: (Long) -> Unit) : RecyclerView.Adapter<TimerAdapter.ViewHolder>() {

    private val timerList = mutableListOf<TimerModel>()
    private var countdownJob: Job? = null

    inner class ViewHolder(val binding: TimerItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TimerModel) {
            binding.timer = item
            binding.delete.setOnClickListener {
                onDelete(item.id)
            }

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

    fun updateTimers(newList: List<TimerModel>) {
        val diff = DIffUtils(timerList, newList)
        val result = DiffUtil.calculateDiff(diff)
        timerList.clear()
        timerList.addAll(newList)
        result.dispatchUpdatesTo(this)

        startCountdowns()
    }

    fun startCountdowns() {
        countdownJob?.cancel()
        countdownJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                val now = System.currentTimeMillis()
                for ((index, timer) in timerList.withIndex()) {
                    val newRemaining = (timer.endTimeMillis - now).coerceAtLeast(0)
                        timer.remainingTime = newRemaining
                        notifyItemChanged(index, "timeOnly")

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
            holder.binding.invalidateAll()
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        countdownJob?.cancel()
    }
}
