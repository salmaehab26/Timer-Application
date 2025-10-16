package com.example.timerapplication.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.example.timerapplication.data.TimerModel
import com.example.timerapplication.data.TimerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import scheduleNotification
import java.util.UUID

class TimerViewModel(application: Application) : AndroidViewModel(application) {
    val timerName = MutableLiveData<String>()
    val timerDescription = MutableLiveData<String>()
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
        val ctx = getApplication<Application>().applicationContext
        scheduleNotification(ctx, timer)
    }
    fun deleteTimer(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        val currentList = repo.loadTimers()
        val updated = currentList.filter { it.id != id }.toMutableList()
        repo.saveTimers(updated)
        _timers.postValue(updated)

        val context = getApplication<Application>().applicationContext
        cancelScheduledNotification(context, id)
    }


    private fun cancelScheduledNotification(context: android.content.Context, id: Long) {
        val uuid = UUID.nameUUIDFromBytes(id.toString().toByteArray())
        WorkManager.getInstance(context).cancelWorkById(uuid)
    }
}