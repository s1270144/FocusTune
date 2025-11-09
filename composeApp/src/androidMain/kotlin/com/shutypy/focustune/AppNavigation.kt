package com.shutypy.focustune

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel = TimerViewModel() // 共通ViewModel

    NavHost(navController = navController, startDestination = "timer") {
        composable("timer") {
            TimerScreen(
                viewModel = viewModel,
                onNavigateToMusicSelect = {
                    navController.navigate("music")
                }
            )
        }
        composable("music") {
            MusicSelectScreen(
                onMusicSelected = { selected ->
                    viewModel.setSelectedMusic(selected)
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
