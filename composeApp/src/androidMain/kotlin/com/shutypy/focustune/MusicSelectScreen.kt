package com.shutypy.focustune

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MusicSelectScreen(
    onMusicSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val musicList: List<Pair<String, Int>> = listOf(
        "Focus Beats" to R.raw.finish_sound,
        "Ocean Wave" to R.raw.finish_sound,
        "Rain Sound" to R.raw.finish_sound
    )

    var currentPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var currentPlaying by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D14))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "音楽を選択",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        musicList.forEach { (name, resId) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        currentPlayer?.stop()
                        currentPlayer?.release()
                        val player = MediaPlayer.create(context, resId)
                        player.start()
                        currentPlayer = player
                        currentPlaying = name
                    }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    fontSize = 18.sp,
                    color = if (currentPlaying == name) Color(0xFF5E88FC) else Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            currentPlayer?.stop()
            currentPlayer?.release()
            currentPlaying?.let { onMusicSelected(it) }
        }) {
            Text("選択する")
        }

        Spacer(Modifier.height(12.dp))

        Button(onClick = {
            currentPlayer?.stop()
            currentPlayer?.release()
            onBack()
        }) {
            Text("戻る")
        }
    }
}
