package com.shutypy.focustune

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

actual class TimerViewModel actual constructor(
    private val musicController: MusicController
) : ViewModel() {

    private val _state = MutableStateFlow(
        TimerState(secondsLeft = 25 * 60, isRunning = false, totalSeconds = 25 * 60)
    )
    actual val state = _state.asStateFlow()

    private var timerJob: Job? = null

    actual var selectedMusic: Music?
        get() = musicController.selectedMusic
        set(value) {
            value?.let { musicController.setMusic(it) }
        }

    actual fun start() {
        if (_state.value.isRunning) return
        _state.value = _state.value.copy(isRunning = true)

        musicController.startMusic()

        timerJob?.cancel()  // ← 既存のループを止める
        timerJob = viewModelScope.launch {
            while (_state.value.isRunning && _state.value.secondsLeft > 0) {
                delay(1000)
                _state.value = _state.value.copy(secondsLeft = _state.value.secondsLeft - 1)
            }

            if (_state.value.secondsLeft == 0) {
                _state.value = _state.value.copy(isRunning = false)
                musicController.stopMusic()
            }
        }
    }

    actual fun pause() {
        _state.value = _state.value.copy(isRunning = false)
        timerJob?.cancel()     // ← ループ停止
        musicController.pauseMusic()
    }

    actual fun reset() {
        timerJob?.cancel()     // ← ループ停止
        _state.value = _state.value.copy(
            secondsLeft = _state.value.totalSeconds,
            isRunning = false
        )
        musicController.stopMusic()
    }

    actual fun setTimer(minutes: Int) {
        val total = minutes * 60
        timerJob?.cancel()     // ← 新しい時間にしたらループ停止
        _state.value = TimerState(total, false, total)
    }
}
