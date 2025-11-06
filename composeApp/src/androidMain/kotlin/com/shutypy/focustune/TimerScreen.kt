package com.shutypy.focustune

import android.media.MediaPlayer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
    strokeWidth: Dp = 18.dp
) {
    Box(
        modifier = modifier.size(size),
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
fun TimerScreen(viewModel: TimerViewModel = viewModel()) {
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.finish_sound) }

    val timerState by viewModel.state.collectAsState()

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
            CircularTimer(
                remainingTime = "%02d:%02d".format(timerState.minutes, timerState.seconds),
                progress = timerState.progress
            )

            Spacer(Modifier.height(24.dp))

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
        }
    }
}
