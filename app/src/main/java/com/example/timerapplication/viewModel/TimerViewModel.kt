package com.example.timerapplication.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.timerapplication.data.TimerModel
import com.example.timerapplication.data.TimerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import scheduleNotification

class TimerViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = TimerRepository(application.applicationContext)

    private val _timers = MutableLiveData<MutableList<TimerModel>>(mutableListOf())
    val timers: LiveData<MutableList<TimerModel>> get() = _timers

    init {
        load()


    }

    fun load() = viewModelScope.launch(Dispatchers.IO) {
        val saved = repo.loadTimers()
        _timers.postValue(saved)
    }

    fun addTimer(timer: TimerModel) = viewModelScope.launch(Dispatchers.IO) {
        val currentList = repo.loadTimers()
        currentList.add(timer)

        repo.saveTimers(currentList)
        _timers.postValue(currentList)
        val context = getApplication<Application>().applicationContext
        scheduleNotification(context, timer)
    }


//
//    fun scheduleNotification( timer: TimerModel) {
//        val context = getApplication<Application>().applicationContext
//        val delayMillis = (timer.endTimeMillis - System.currentTimeMillis()) - 5 * 60 * 1000
//
//        if (delayMillis > 0) {
//            val data = androidx.work.Data.Builder()
//                .putString("TIMER_NAME", timer.name)
//                .build()
//
//            val request = androidx.work.OneTimeWorkRequestBuilder<com.example.timerapplication.data.NotificationWorker>()
//                .setInitialDelay(delayMillis, java.util.concurrent.TimeUnit.MILLISECONDS)
//                .setInputData(data)
//                .build()
//
//            androidx.work.WorkManager.getInstance(context).enqueue(request)
//        }
//    }
}