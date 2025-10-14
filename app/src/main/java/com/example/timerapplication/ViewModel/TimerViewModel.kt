package com.example.timerapplication.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timerapplication.data.TimerModel
import com.example.timerapplication.data.TimerRepository
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {
    private val repository = TimerRepository()

    val timers: LiveData<List<TimerModel>> get() = repository.timers

    fun addTimer(timer: TimerModel) {
        viewModelScope.launch {
            repository.addTimer(timer)
        }
    }
}