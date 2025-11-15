package com.shutypy.focustune

import kotlinx.coroutines.flow.StateFlow

expect class TimerViewModel(
    musicController: MusicController
) {
    val state: StateFlow<TimerState>

    var selectedMusic: String

    fun start()

    fun pause()

    fun reset()

    fun setTimer(minutes: Int)
}
