package com.shutypy.focustune

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

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

            // 背景グレーの円
            drawArc(
                color = Color(0xFF3A3A3D),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = stroke
            )

            // 青の進行円
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
    var secondsLeft by remember { mutableStateOf(viewModel.secondsLeft) }
    var isRunning by remember { mutableStateOf(viewModel.isRunning) }

    // タイマー更新ループ
    LaunchedEffect(isRunning) {
        while (isRunning && secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
    }

    val minutes = secondsLeft / 60
    val seconds = secondsLeft % 60
    val progress = secondsLeft.toFloat() / viewModel.initialSeconds.toFloat()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D14)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularTimer(
                remainingTime = "%02d:%02d".format(minutes, seconds),
                progress = progress
            )

            Spacer(Modifier.height(24.dp))

            Row {
                Button(onClick = { isRunning = !isRunning }) {
                    Text(if (isRunning) "Pause" else "Start")
                }

                Spacer(Modifier.width(12.dp))

                Button(onClick = {
                    secondsLeft = viewModel.initialSeconds
                    isRunning = false
                }) {
                    Text("Reset")
                }
            }
        }
    }
}