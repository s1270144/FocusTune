package com.shutypy.focustune

data class TimerState(
    val secondsLeft: Int,
    val isRunning: Boolean,
    val totalSeconds: Int
) {
    val minutes: Int get() = secondsLeft / 60
    val seconds: Int get() = secondsLeft % 60
    val progress: Float get() = secondsLeft.toFloat() / totalSeconds.toFloat()
}
