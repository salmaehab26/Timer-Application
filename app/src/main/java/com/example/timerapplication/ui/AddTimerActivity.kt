package com.example.timerapplication.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.timerapplication.R
import com.example.timerapplication.viewModel.TimerViewModel
import com.example.timerapplication.data.TimerModel
import com.example.timerapplication.databinding.ActivityAddTimerBinding
import scheduleNotification
import java.text.SimpleDateFormat
import java.util.Calendar

class AddTimerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTimerBinding
    private val vm: TimerViewModel by viewModels()
    private var chosenMillis: Long = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_timer)

        binding.pickDateTime.setOnClickListener { pickDateTime() }

        binding.saveButton.setOnClickListener {
            val title = binding.inputTitle.text.toString().trim()
            val desc = binding.inputDesc.text.toString().trim()
            if (title.isEmpty() || chosenMillis == 0L) return@setOnClickListener

            val timer = TimerModel(name = title, description = desc, endTimeMillis = chosenMillis)
            vm.addTimer(timer)
            scheduleNotification(this.applicationContext, timer)
            finish()
        }
    }

    private fun pickDateTime() {
        val cal = Calendar.getInstance()
        DatePickerDialog(this, { _, y, m, d ->
            cal.set(Calendar.YEAR, y)
            cal.set(Calendar.MONTH, m)
            cal.set(Calendar.DAY_OF_MONTH, d)
            TimePickerDialog(this, { _, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                cal.set(Calendar.SECOND, 0)
                chosenMillis = cal.timeInMillis
                binding.pickDateTime.text = SimpleDateFormat("yyyy-MM-dd HH:mm").format(cal.time)
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }
}