package com.shutypy.focustune

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    actual var selectedMusic: String
        get() = musicController.currentMusicName
        set(value) {
            musicController.setMusic(value)
        }
    
    actual fun start() {
        if (_state.value.isRunning) return
        _state.value = _state.value.copy(isRunning = true)

        musicController.startMusic()

        viewModelScope.launch {
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
        musicController.pauseMusic()
    }

    actual fun reset() {
        _state.value = _state.value.copy(
            secondsLeft = _state.value.totalSeconds,
            isRunning = false
        )
        musicController.stopMusic()
    }

    actual fun setTimer(minutes: Int) {
        val total = minutes * 60
        _state.value = TimerState(total, false, total)
    }
}
