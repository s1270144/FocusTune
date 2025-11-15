package com.shutypy.focustune

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val musicController = remember { MusicController(context) }
    val viewModel = remember { TimerViewModel(musicController) }

    NavHost(navController = navController, startDestination = "timer") {
        composable("timer") {
            TimerScreen(
                viewModel = viewModel,
                onNavigateToMusicSelect = { navController.navigate("music") }
            )
        }

        composable("music") {
            MusicSelectScreen(
                onMusicSelected = { selected ->
                    viewModel.selectedMusic = selected
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
