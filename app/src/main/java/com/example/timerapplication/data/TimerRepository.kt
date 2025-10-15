package com.example.timerapplication.data

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TimerRepository(private val context: Context) {
    private val prefsName = "timers_prefs"
    private val keyTimers = "timers_list"
    private val gson = Gson()

    private val prefs get() = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    fun loadTimers(): MutableList<TimerModel> {
        val json = prefs.getString(keyTimers, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<TimerModel>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveTimers(list: List<TimerModel>) {
        val json = gson.toJson(list)
        prefs.edit().putString(keyTimers, json).apply()
    }
}