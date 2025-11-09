package com.shutypy.focustune

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {

    // --- タイマーの状態管理 ---
    private val _state = MutableStateFlow(
        TimerState(secondsLeft = 25 * 60, isRunning = false, totalSeconds = 25 * 60)
    )
    val state = _state.asStateFlow()

    // --- 音楽選択状態 ---
    val selectedMusic: MutableState<String> = mutableStateOf("")

    // --- タイマー開始 ---
    fun start() {
        if (_state.value.isRunning) return
        _state.value = _state.value.copy(isRunning = true)

        viewModelScope.launch {
            while (_state.value.isRunning && _state.value.secondsLeft > 0) {
                delay(1000)
                _state.value = _state.value.copy(secondsLeft = _state.value.secondsLeft - 1)
            }

            if (_state.value.secondsLeft == 0) {
                _state.value = _state.value.copy(isRunning = false)
            }
        }
    }

    // --- 一時停止 ---
    fun pause() {
        _state.value = _state.value.copy(isRunning = false)
    }

    // --- リセット ---
    fun reset() {
        _state.value = _state.value.copy(
            secondsLeft = _state.value.totalSeconds,
            isRunning = false
        )
    }

    // --- タイマー時間を変更 ---
    fun setTimer(minutes: Int) {
        val total = minutes * 60
        _state.value = TimerState(total, false, total)
    }

    // --- 音楽を選択 ---
    fun setSelectedMusic(name: String) {
        selectedMusic.value = name
    }
}
