package com.example.timerapplication.ui

import TimerAdapter
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.timerapplication.R
import com.example.timerapplication.ViewModel.TimerViewModel
import com.example.timerapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewModel: TimerViewModel by viewModels()

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = TimerAdapter()
        binding.rvTimerList.adapter = adapter

        viewModel.timers.observe(this) { list ->
            adapter.updateList(list)
        }

        binding.bnAdd.setOnClickListener {
            startActivity(Intent(this, AddTimerActivity::class.java))
        }
    }
}