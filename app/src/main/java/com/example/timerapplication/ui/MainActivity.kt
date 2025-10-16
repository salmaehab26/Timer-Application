package com.example.timerapplication.ui

import TimerAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timerapplication.R
import com.example.timerapplication.data.TimerRepository
import com.example.timerapplication.viewModel.TimerViewModel
import com.example.timerapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewModel: TimerViewModel by viewModels()
    private val REQUEST_POST_NOTIF = 1001

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.timerViewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = TimerAdapter { id ->
            viewModel.deleteTimer(id)
        }
        binding.rvTimerList.adapter = adapter
        binding.rvTimerList.layoutManager = LinearLayoutManager(this)

        viewModel.timers.observe(this) { list ->
            adapter.updateTimers(list)
        }

        binding.bnAdd.setOnClickListener {
            startActivity(Intent(this, AddTimerActivity::class.java))
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), REQUEST_POST_NOTIF)
            }
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.load()
    }
}