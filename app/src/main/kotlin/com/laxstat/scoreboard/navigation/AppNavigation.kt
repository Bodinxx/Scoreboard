package com.laxstat.scoreboard.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.laxstat.scoreboard.ui.screens.GameMemoryScreen
import com.laxstat.scoreboard.ui.screens.RosterScreen
import com.laxstat.scoreboard.ui.screens.ScoreboardScreen
import com.laxstat.scoreboard.ui.screens.SettingsScreen
import com.laxstat.scoreboard.viewmodels.ScoreboardViewModel

object Routes {
    const val SCOREBOARD = "scoreboard"
    const val GAME_MEMORY = "game_memory"
    const val ROSTER = "roster"
    const val SETTINGS = "settings"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val scoreboardViewModel: ScoreboardViewModel = viewModel()

    NavHost(navController = navController, startDestination = Routes.SCOREBOARD) {
        composable(Routes.SCOREBOARD) {
            ScoreboardScreen(
                onNavigateToSettings = { navController.navigate(Routes.SETTINGS) },
                onNavigateToGameMemory = { navController.navigate(Routes.GAME_MEMORY) },
                onNavigateToRoster = { navController.navigate(Routes.ROSTER) },
                viewModel = scoreboardViewModel
            )
        }
        composable(Routes.GAME_MEMORY) {
            GameMemoryScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Routes.ROSTER) {
            RosterScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                scoreboardViewModel = scoreboardViewModel
            )
        }
    }
}
