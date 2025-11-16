package com.shutypy.focustune

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MusicSelectScreen(
    onMusicSelected: (Music) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val musicList = remember { MusicRepository.loadMusicList(context) }

    val currentPlayer = remember { mutableStateOf<MediaPlayer?>(null) }
    val currentPlaying = remember { mutableStateOf<Music?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D14))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "音楽を選択",
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 曲リストをスクロール可能に
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(musicList) { music ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // 前の音楽を停止
                            currentPlayer.value?.stop()
                            currentPlayer.value?.release()

                            // 新しい音楽を再生
                            val player = MediaPlayer()
                            val afd = context.assets.openFd("music/${music.file}")
                            player.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                            player.prepare()
                            player.start()

                            currentPlayer.value = player
                            currentPlaying.value = music
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = music.title,
                        color = if (currentPlaying.value == music) Color(0xFF5E88FC) else Color.White,
                        fontSize = 18.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                currentPlayer.value?.stop()
                currentPlayer.value?.release()
                currentPlaying.value?.let { onMusicSelected(it) }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("選択する")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                currentPlayer.value?.stop()
                currentPlayer.value?.release()
                onBack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("戻る")
        }
    }
}
