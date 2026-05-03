package com.laxstat.scoreboard.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.laxstat.scoreboard.data.db.entities.Player
import com.laxstat.scoreboard.data.db.entities.Team
import com.laxstat.scoreboard.data.repositories.ScoreboardRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RosterViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = ScoreboardRepository(application)

    val teams: StateFlow<List<Team>> = repo.getAllTeams()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPlayers: StateFlow<List<Player>> = repo.getAllPlayers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTeam(name: String, colorCode: String = "#1565C0") {
        viewModelScope.launch { repo.insertTeam(Team(name = name, colorCode = colorCode)) }
    }

    fun updateTeam(team: Team) {
        viewModelScope.launch { repo.updateTeam(team) }
    }

    fun deleteTeam(team: Team) {
        viewModelScope.launch { repo.deleteTeam(team) }
    }

    fun addPlayer(teamId: Long, name: String, jerseyNumber: Int) {
        viewModelScope.launch {
            repo.insertPlayer(Player(teamId = teamId, jerseyNumber = jerseyNumber, name = name))
        }
    }

    fun updatePlayer(player: Player) {
        viewModelScope.launch { repo.updatePlayer(player) }
    }

    fun deletePlayer(player: Player) {
        viewModelScope.launch { repo.deletePlayer(player) }
    }
}
