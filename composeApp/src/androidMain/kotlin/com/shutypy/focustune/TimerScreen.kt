package com.shutypy.focustune

import android.media.MediaPlayer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CircularTimer(
    remainingTime: String,
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 220.dp,
    strokeWidth: Dp = 18.dp,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .size(size)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val sweepAngle = 360 * progress
            val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)

            // 背景グレー
            drawArc(
                color = Color(0xFF3A3A3D),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = stroke
            )

            // 青い進行円
            drawArc(
                color = Color(0xFF5E88FC),
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = stroke
            )
        }

        Text(
            text = remainingTime,
            color = Color.White,
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TimerScreen(
    viewModel: TimerViewModel = viewModel(),
    onNavigateToMusicSelect: () -> Unit = {} // ← 🟢 追加：ナビゲーションコールバック
) {
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.finish_sound) }

    val timerState by viewModel.state.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var selectedMinutes by remember { mutableStateOf(25) }

    // 入力モード用
    var isEditingText by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }

    // 終了時サウンド
    LaunchedEffect(timerState.secondsLeft) {
        if (timerState.secondsLeft == 0) {
            mediaPlayer.start()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D14)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // 🕓 円形タイマー
            CircularTimer(
                remainingTime = "%02d:%02d".format(timerState.minutes, timerState.seconds),
                progress = timerState.progress,
                onClick = { showDialog = true }
            )

            Spacer(Modifier.height(24.dp))

            // ▶️ ボタン群
            Row {
                Button(onClick = {
                    if (timerState.isRunning) viewModel.pause() else viewModel.start()
                }) {
                    Text(if (timerState.isRunning) "Pause" else "Start")
                }

                Spacer(Modifier.width(12.dp))

                Button(onClick = { viewModel.reset() }) {
                    Text("Reset")
                }
            }

            Spacer(Modifier.height(16.dp))

            // 🎵 音楽選択ボタン（画面遷移）
            Button(onClick = { onNavigateToMusicSelect() }) {
                Text("🎵 音楽を選択")
            }

            // 選択中の音楽名を表示（仮）
            if (viewModel.selectedMusic.value.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "選択中: ${viewModel.selectedMusic.value}",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }

    // 🕓 時間設定ダイアログ
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("タイマー時間を設定") },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("分数を選んでください（5〜60分）")
                    Spacer(Modifier.height(8.dp))

                    // 🟢 スライダー
                    if (!isEditingText) {
                        Slider(
                            value = selectedMinutes.toFloat(),
                            onValueChange = { selectedMinutes = it.toInt() },
                            valueRange = 5f..60f,
                            steps = 55
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    // 🟢 数値表示 or 入力欄
                    if (isEditingText) {
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it.filter { c -> c.isDigit() } },
                            singleLine = true,
                            label = { Text("分を入力") },
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.width(120.dp)
                        )
                    } else {
                        Text(
                            text = "$selectedMinutes 分",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable {
                                    isEditingText = true
                                    inputText = selectedMinutes.toString()
                                }
                                .padding(4.dp)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val minutes = when {
                        isEditingText -> inputText.toIntOrNull()?.coerceIn(5, 60) ?: selectedMinutes
                        else -> selectedMinutes
                    }
                    viewModel.setTimer(minutes)
                    showDialog = false
                    isEditingText = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    isEditingText = false
                }) {
                    Text("キャンセル")
                }
            }
        )
    }
}
