package com.shutypy.focustune

/**
 * 共通ロジックで扱う「音楽再生」の抽象インターフェース。
 * 実装はプラットフォームごと（Android/iOS）で異なるため expect 宣言のみ。
 */
expect class MusicController {
    fun setMusic(name: String)
    fun startMusic()
    fun pauseMusic()
    fun stopMusic()
}