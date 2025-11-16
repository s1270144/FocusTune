package com.shutypy.focustune

import android.content.Context
import android.media.MediaPlayer

actual class MusicController(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    var selectedMusic: Music? = null

    actual fun setMusic(music: Music) {
        stopMusic()
        selectedMusic = music
    }

    actual fun startMusic() {
        val music = selectedMusic ?: return
        stopMusic()

        val player = MediaPlayer()
        val afd = context.assets.openFd("music/${music.file}")
        player.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        player.isLooping = true
        player.prepare()
        player.start()

        mediaPlayer = player
    }

    actual fun pauseMusic() {
        mediaPlayer?.pause()
    }

    actual fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    actual val currentMusicName: String
        get() = selectedMusic?.title ?: ""
}
