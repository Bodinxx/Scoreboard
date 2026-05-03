package com.laxstat.scoreboard.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.laxstat.scoreboard.data.db.entities.Player
import com.laxstat.scoreboard.data.db.entities.Team
import com.laxstat.scoreboard.viewmodels.RosterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RosterScreen(
    onNavigateBack: () -> Unit,
    viewModel: RosterViewModel = viewModel()
) {
    val teams by viewModel.teams.collectAsStateWithLifecycle()
    val players by viewModel.allPlayers.collectAsStateWithLifecycle()
    var showAddTeamDialog by remember { mutableStateOf(false) }
    var showAddPlayerDialog by remember { mutableStateOf<Long?>(null) }
    var editingPlayer by remember { mutableStateOf<Player?>(null) }

    if (showAddTeamDialog) {
        AddTeamDialog(
            onDismiss = { showAddTeamDialog = false },
            onAdd = { name, color ->
                viewModel.addTeam(name, color)
                showAddTeamDialog = false
            }
        )
    }

    showAddPlayerDialog?.let { teamId ->
        AddPlayerDialog(
            onDismiss = { showAddPlayerDialog = null },
            onAdd = { name, number ->
                viewModel.addPlayer(teamId, name, number)
                showAddPlayerDialog = null
            }
        )
    }

    editingPlayer?.let { player ->
        EditPlayerDialog(
            player = player,
            onDismiss = { editingPlayer = null },
            onSave = { updated ->
                viewModel.updatePlayer(updated)
                editingPlayer = null
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Roster") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddTeamDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Team")
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (teams.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No teams yet. Tap + to add a team.", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
            teams.forEach { team ->
                val teamPlayers = players.filter { it.teamId == team.id }
                item(key = "team_${team.id}") {
                    TeamSection(
                        team = team,
                        players = teamPlayers,
                        onAddPlayer = { showAddPlayerDialog = team.id },
                        onDeleteTeam = { viewModel.deleteTeam(team) },
                        onEditPlayer = { editingPlayer = it },
                        onDeletePlayer = { viewModel.deletePlayer(it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TeamSection(
    team: Team,
    players: List<Player>,
    onAddPlayer: () -> Unit,
    onDeleteTeam: () -> Unit,
    onEditPlayer: (Player) -> Unit,
    onDeletePlayer: (Player) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(team.name, style = MaterialTheme.typography.titleMedium)
                Row {
                    IconButton(onClick = onAddPlayer) {
                        Icon(Icons.Default.Add, contentDescription = "Add Player")
                    }
                    IconButton(onClick = onDeleteTeam) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Team", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
            if (players.isEmpty()) {
                Text("No players. Tap + to add.", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(8.dp))
            }
            players.forEach { player ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("#${player.jerseyNumber} ${player.name}", style = MaterialTheme.typography.bodyMedium)
                    Row {
                        IconButton(onClick = { onEditPlayer(player) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = { onDeletePlayer(player) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTeamDialog(onDismiss: () -> Unit, onAdd: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("#1565C0") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Team") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Team Name") }, singleLine = true)
                OutlinedTextField(value = color, onValueChange = { color = it }, label = { Text("Color (hex)") }, singleLine = true)
            }
        },
        confirmButton = {
            TextButton(onClick = { if (name.isNotBlank()) onAdd(name.trim(), color) }) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddPlayerDialog(onDismiss: () -> Unit, onAdd: (String, Int) -> Unit) {
    var name by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Player") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Player Name") }, singleLine = true)
                OutlinedTextField(value = number, onValueChange = { number = it }, label = { Text("Jersey Number") }, singleLine = true)
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val num = number.toIntOrNull() ?: 0
                if (name.isNotBlank()) onAdd(name.trim(), num)
            }) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditPlayerDialog(player: Player, onDismiss: () -> Unit, onSave: (Player) -> Unit) {
    var name by remember { mutableStateOf(player.name) }
    var number by remember { mutableStateOf(player.jerseyNumber.toString()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Player") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, singleLine = true)
                OutlinedTextField(value = number, onValueChange = { number = it }, label = { Text("Jersey #") }, singleLine = true)
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val num = number.toIntOrNull() ?: player.jerseyNumber
                if (name.isNotBlank()) onSave(player.copy(name = name.trim(), jerseyNumber = num))
            }) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
