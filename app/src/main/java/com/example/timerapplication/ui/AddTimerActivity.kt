package com.example.timerapplication.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
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
        binding.timerViewModel = vm
        binding.lifecycleOwner = this
        binding.pickDateTime.setOnClickListener { pickDateTime() }

        binding.saveButton.setOnClickListener {
            val title = vm.timerName.value
            val desc = vm.timerDescription.value
            if (title.isNullOrEmpty() ||desc.isNullOrEmpty() || chosenMillis == 0L) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()

                return@setOnClickListener
            }
            if (chosenMillis <= System.currentTimeMillis()) {
                Toast.makeText(this, "Please choose a future time", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val timer = TimerModel(name = title, description = desc, endTimeMillis = chosenMillis)
            vm.addTimer(timer)
            scheduleNotification(this.applicationContext, timer)
            finish()
        }
    }


    private fun pickDateTime() {
        val cal = Calendar.getInstance()

        DatePickerDialog(this, { _, year, month, day ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, day)

            TimePickerDialog(this, { _, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                cal.set(Calendar.SECOND, 0)
                chosenMillis = cal.timeInMillis
                binding.tvTime.text = SimpleDateFormat("dd/MM/yyyy HH:mm").format(cal.time)
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()

        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }
}
