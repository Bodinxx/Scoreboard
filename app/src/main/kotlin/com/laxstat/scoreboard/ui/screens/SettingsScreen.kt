package com.laxstat.scoreboard.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.laxstat.scoreboard.viewmodels.ScoreboardViewModel
import com.laxstat.scoreboard.viewmodels.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    scoreboardViewModel: ScoreboardViewModel,
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val settings by settingsViewModel.settings.collectAsStateWithLifecycle()
    var newPenaltyType by remember { mutableStateOf("") }
    var homeNameText by remember { mutableStateOf(settings.homeTeamName) }
    var awayNameText by remember { mutableStateOf(settings.awayTeamName) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = {
                        scoreboardViewModel.applySettings(settings)
                        onNavigateBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Display Options", style = MaterialTheme.typography.titleMedium)
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Show Shots on Goal")
                    Switch(checked = settings.showShotsOnGoal, onCheckedChange = { settingsViewModel.setShowShotsOnGoal(it) })
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Show Save %")
                    Switch(checked = settings.showSavePercent, onCheckedChange = { settingsViewModel.setShowSavePercent(it) })
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Show -1 Buttons")
                    Switch(checked = settings.showDecrementButtons, onCheckedChange = { settingsViewModel.setShowDecrementButtons(it) })
                }
            }
            item { HorizontalDivider() }
            item {
                Text("Team Names", style = MaterialTheme.typography.titleMedium)
            }
            item {
                OutlinedTextField(
                    value = homeNameText,
                    onValueChange = {
                        homeNameText = it
                        settingsViewModel.setHomeTeamName(it)
                    },
                    label = { Text("Home Team Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
            item {
                OutlinedTextField(
                    value = awayNameText,
                    onValueChange = {
                        awayNameText = it
                        settingsViewModel.setAwayTeamName(it)
                    },
                    label = { Text("Away Team Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
            item { HorizontalDivider() }
            item {
                Text("Penalty Types", style = MaterialTheme.typography.titleMedium)
            }
            items(settings.penaltyTypes) { type ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(type, style = MaterialTheme.typography.bodyMedium)
                    IconButton(onClick = { settingsViewModel.removePenaltyType(type) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Remove", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newPenaltyType,
                        onValueChange = { newPenaltyType = it },
                        label = { Text("New Penalty Type") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    IconButton(onClick = {
                        if (newPenaltyType.isNotBlank()) {
                            settingsViewModel.addPenaltyType(newPenaltyType.trim())
                            newPenaltyType = ""
                        }
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Penalty Type")
                    }
                }
            }
        }
    }
}
