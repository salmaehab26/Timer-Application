
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.timerapplication.Adapter.DIffUtils
import com.example.timerapplication.data.TimerModel
import com.example.timerapplication.databinding.TimerItemBinding

class TimerAdapter(
) : RecyclerView.Adapter<TimerAdapter.ViewHolder>() {
    private val timerList = mutableListOf<TimerModel>()

     class ViewHolder( val binding: TimerItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TimerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = timerList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val timer = timerList[position]
//        holder.binding.journeyName.text = timer.name
//        holder.itemView.setOnClickListener { onClick(timer) }
    }

    fun updateList(newList: List<TimerModel>) {
        val diffCallback = DIffUtils(timerList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        timerList.clear()
        timerList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)

    }
}
