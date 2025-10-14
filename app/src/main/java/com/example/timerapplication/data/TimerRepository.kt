package com.example.timerapplication.data

import androidx.lifecycle.MutableLiveData

class TimerRepository {
    private val timerList = mutableListOf<TimerModel>()
    val timers = MutableLiveData<List<TimerModel>>(timerList)

    fun addTimer(timer: TimerModel) {
        timerList.add(timer)
        timers.value = timerList.toList()
    }
}