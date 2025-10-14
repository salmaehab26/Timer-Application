package com.example.timerapplication.data

data class TimerModel ( val id: Long = System.currentTimeMillis(),
                        val name: String,
                        val description: String,
                        val endTimeMillis: Long)