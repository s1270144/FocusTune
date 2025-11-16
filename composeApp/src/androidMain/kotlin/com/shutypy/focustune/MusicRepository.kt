package com.shutypy.focustune

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MusicRepository {

    fun loadMusicList(context: Context): List<Music> {
        val json = context.assets.open("music.json").bufferedReader().use { it.readText() }

        val type = object : TypeToken<List<Music>>() {}.type
        return Gson().fromJson(json, type)
    }
}
