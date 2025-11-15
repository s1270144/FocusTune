package com.shutypy.focustune

import android.content.Context
import android.media.MediaPlayer

actual class MusicController(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var selectedResId: Int? = null
    private var selectedName: String = ""

    actual fun setMusic(name: String) {
        val resId = when (name) {
            "Focus Beats" -> R.raw.train
            "Ocean Wave" -> R.raw.train
            "Rain Sound" -> R.raw.train
            else -> null
        }
        selectedResId = resId
        selectedName = name
    }

    actual fun startMusic() {
        stopMusic()
        val resId = selectedResId ?: return

        mediaPlayer = MediaPlayer.create(context, resId).apply {
            isLooping = true
            start()
        }
    }

    actual fun pauseMusic() {
        mediaPlayer?.pause()
    }

    actual fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    val currentMusicName: String
        get() = selectedName
}
