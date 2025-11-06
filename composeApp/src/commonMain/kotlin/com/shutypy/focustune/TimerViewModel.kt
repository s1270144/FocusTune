package com.shutypy.focustune

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {

    var secondsLeft: Int = 25 * 60
        private set

    var isRunning: Boolean = false
        private set

    var initialSeconds: Int = 25 * 60
        private set

    // 画面へ通知するための StateFlow に変更予定だがまずは簡単に
    fun start() {
        if (isRunning) return
        isRunning = true

        viewModelScope.launch {
            while (isRunning && secondsLeft > 0) {
                delay(1000)
                secondsLeft--
            }
            if (secondsLeft == 0) {
                isRunning = false
                // UI側で onFinished 動かす
            }
        }
    }

    fun pause() {
        isRunning = false
    }

    fun reset() {
        isRunning = false
        secondsLeft = initialSeconds
    }

    fun setTimer(totalMinutes: Int) {
        initialSeconds = totalMinutes * 60
        reset()
    }
}