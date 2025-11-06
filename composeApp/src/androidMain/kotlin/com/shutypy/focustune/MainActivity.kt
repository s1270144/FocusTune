package com.shutypy.focustune

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimerScreen() // Android拡張版（音付きUI）
            // または setContent { App() } で共通UIを直接使うことも可能
        }
    }
}
