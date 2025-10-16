package com.example.timerapplication.data

data class TimerModel(
    val id: Long = System.currentTimeMillis(),
    val name: String,
    val description: String,
    val endTimeMillis: Long,
    var remainingTime: Long = 0L
) {
    val formattedTime: String
        get() {
            val total = remainingTime
            val hours = java.util.concurrent.TimeUnit.MILLISECONDS.toHours(total)
            val minutes = java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(total) % 60
            val seconds = java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(total) % 60
            return String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }
}