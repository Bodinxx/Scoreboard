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
import com.laxstat.scoreboard.data.db.entities.EventType
import com.laxstat.scoreboard.data.db.entities.GameEvent
import com.laxstat.scoreboard.viewmodels.GameMemoryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameMemoryScreen(
    onNavigateBack: () -> Unit,
    viewModel: GameMemoryViewModel = viewModel()
) {
    val events by viewModel.events.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }
    var showClearDialog by remember { mutableStateOf(false) }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear All Events?") },
            text = { Text("This will permanently delete all game events.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearAllEvents()
                    showClearDialog = false
                }) { Text("Clear") }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showAddDialog) {
        AddEventDialog(
            onDismiss = { showAddDialog = false },
            onAddGoal = { teamId, scorerId, a1, a2 ->
                viewModel.logGoal(teamId, scorerId, a1, a2)
                showAddDialog = false
            },
            onAddPenalty = { teamId, playerId, infraction ->
                viewModel.logPenalty(teamId, playerId, infraction)
                showAddDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Game Events") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (events.isNotEmpty()) {
                        TextButton(onClick = { showClearDialog = true }) {
                            Text("Clear All")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Event")
            }
        }
    ) { paddingValues ->
        if (events.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No events yet. Tap + to log a goal or penalty.", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(events) { event ->
                    EventCard(event = event, onDelete = { viewModel.deleteEvent(event) })
                }
            }
        }
    }
}

@Composable
private fun EventCard(event: GameEvent, onDelete: () -> Unit) {
    val sdf = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }
    val timeStr = sdf.format(Date(event.timestamp))
    val icon = when (event.type) {
        EventType.GOAL -> "🥍"
        EventType.ASSIST -> "🏒"
        EventType.PENALTY -> "⚠️"
    }
    val teamLabel = if (event.teamId == 1L) "Home" else "Away"
    val desc = when (event.type) {
        EventType.GOAL -> "$icon Goal - $teamLabel (Player #${event.playerId ?: "?"})"
        EventType.ASSIST -> "$icon Assist"
        EventType.PENALTY -> "$icon Penalty - $teamLabel: ${event.subType ?: "Unknown"} (Player #${event.playerId ?: "?"})"
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(desc, style = MaterialTheme.typography.bodyMedium)
                Text(
                    timeStr,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete event", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEventDialog(
    onDismiss: () -> Unit,
    onAddGoal: (teamId: Long, scorerId: Long?, assist1: Long?, assist2: Long?) -> Unit,
    onAddPenalty: (teamId: Long, playerId: Long?, infraction: String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedTeamId by remember { mutableStateOf(1L) }
    var scorerText by remember { mutableStateOf("") }
    var assist1Text by remember { mutableStateOf("") }
    var assist2Text by remember { mutableStateOf("") }
    var penaltyPlayerText by remember { mutableStateOf("") }
    var infractionType by remember { mutableStateOf("Slashing") }

    val penaltyTypes = listOf("Slashing", "Tripping", "Cross-checking", "Holding", "Interference")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Event") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TabRow(selectedTabIndex = selectedTab) {
                    Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Goal") })
                    Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Penalty") })
                }
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Team: ", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.width(8.dp))
                    FilterChip(selected = selectedTeamId == 1L, onClick = { selectedTeamId = 1L }, label = { Text("Home") })
                    Spacer(Modifier.width(8.dp))
                    FilterChip(selected = selectedTeamId == 2L, onClick = { selectedTeamId = 2L }, label = { Text("Away") })
                }
                if (selectedTab == 0) {
                    OutlinedTextField(value = scorerText, onValueChange = { scorerText = it }, label = { Text("Scorer # (optional)") }, singleLine = true)
                    OutlinedTextField(value = assist1Text, onValueChange = { assist1Text = it }, label = { Text("Assist 1 # (optional)") }, singleLine = true)
                    OutlinedTextField(value = assist2Text, onValueChange = { assist2Text = it }, label = { Text("Assist 2 # (optional)") }, singleLine = true)
                } else {
                    OutlinedTextField(value = penaltyPlayerText, onValueChange = { penaltyPlayerText = it }, label = { Text("Player # (optional)") }, singleLine = true)
                    Text("Infraction:", style = MaterialTheme.typography.labelMedium)
                    penaltyTypes.forEach { type ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = infractionType == type, onClick = { infractionType = type })
                            Text(type, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (selectedTab == 0) {
                    onAddGoal(
                        selectedTeamId,
                        scorerText.toLongOrNull(),
                        assist1Text.toLongOrNull(),
                        assist2Text.toLongOrNull()
                    )
                } else {
                    onAddPenalty(selectedTeamId, penaltyPlayerText.toLongOrNull(), infractionType)
                }
            }) { Text("Log") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
