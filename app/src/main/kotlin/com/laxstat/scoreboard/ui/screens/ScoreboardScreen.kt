package com.laxstat.scoreboard.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.laxstat.scoreboard.ui.components.TeamScorePanel
import com.laxstat.scoreboard.viewmodels.ScoreboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreboardScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToGameMemory: () -> Unit,
    onNavigateToRoster: () -> Unit,
    viewModel: ScoreboardViewModel = viewModel()
) {
    val homeTeam by viewModel.homeTeam.collectAsStateWithLifecycle()
    val awayTeam by viewModel.awayTeam.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    var showResetDialog by remember { mutableStateOf(false) }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Scores?") },
            text = { Text("This will reset all goals and shots to 0.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.resetScores()
                    showResetDialog = false
                }) { Text("Reset") }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("LaxStat") },
                actions = {
                    IconButton(onClick = { showResetDialog = true }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset")
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Text("⚡") },
                    label = { Text("Score") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToGameMemory,
                    icon = { Text("📋") },
                    label = { Text("Events") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToRoster,
                    icon = { Text("👥") },
                    label = { Text("Roster") }
                )
            }
        }
    ) { paddingValues ->
        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TeamScorePanel(
                    teamState = homeTeam,
                    showShotsOnGoal = settings.showShotsOnGoal,
                    showSavePercent = settings.showSavePercent,
                    showDecrementButtons = settings.showDecrementButtons,
                    onGoalIncrement = viewModel::homeGoalIncrement,
                    onGoalDecrement = viewModel::homeGoalDecrement,
                    onShotIncrement = viewModel::homeShotIncrement,
                    onShotDecrement = viewModel::homeShotDecrement,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                TeamScorePanel(
                    teamState = awayTeam,
                    showShotsOnGoal = settings.showShotsOnGoal,
                    showSavePercent = settings.showSavePercent,
                    showDecrementButtons = settings.showDecrementButtons,
                    onGoalIncrement = viewModel::awayGoalIncrement,
                    onGoalDecrement = viewModel::awayGoalDecrement,
                    onShotIncrement = viewModel::awayShotIncrement,
                    onShotDecrement = viewModel::awayShotDecrement,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TeamScorePanel(
                    teamState = homeTeam,
                    showShotsOnGoal = settings.showShotsOnGoal,
                    showSavePercent = settings.showSavePercent,
                    showDecrementButtons = settings.showDecrementButtons,
                    onGoalIncrement = viewModel::homeGoalIncrement,
                    onGoalDecrement = viewModel::homeGoalDecrement,
                    onShotIncrement = viewModel::homeShotIncrement,
                    onShotDecrement = viewModel::homeShotDecrement,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
                TeamScorePanel(
                    teamState = awayTeam,
                    showShotsOnGoal = settings.showShotsOnGoal,
                    showSavePercent = settings.showSavePercent,
                    showDecrementButtons = settings.showDecrementButtons,
                    onGoalIncrement = viewModel::awayGoalIncrement,
                    onGoalDecrement = viewModel::awayGoalDecrement,
                    onShotIncrement = viewModel::awayShotIncrement,
                    onShotDecrement = viewModel::awayShotDecrement,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
        }
    }
}
