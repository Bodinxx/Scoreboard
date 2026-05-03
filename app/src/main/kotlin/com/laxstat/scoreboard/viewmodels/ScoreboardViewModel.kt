package com.laxstat.scoreboard.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.laxstat.scoreboard.data.models.AppSettings
import com.laxstat.scoreboard.data.models.TeamState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ScoreboardViewModel(application: Application) : AndroidViewModel(application) {

    private val _homeTeam = MutableStateFlow(TeamState(id = 1L, name = "Home", colorCode = "#1565C0"))
    val homeTeam: StateFlow<TeamState> = _homeTeam.asStateFlow()

    private val _awayTeam = MutableStateFlow(TeamState(id = 2L, name = "Away", colorCode = "#B71C1C"))
    val awayTeam: StateFlow<TeamState> = _awayTeam.asStateFlow()

    private val _settings = MutableStateFlow(AppSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    fun homeGoalIncrement() = _homeTeam.update { it.copy(goals = it.goals + 1) }
    fun homeGoalDecrement() = _homeTeam.update { if (it.goals > 0) it.copy(goals = it.goals - 1) else it }
    fun homeShotIncrement() = _homeTeam.update { it.copy(shotsOnGoal = it.shotsOnGoal + 1) }
    fun homeShotDecrement() = _homeTeam.update { if (it.shotsOnGoal > 0) it.copy(shotsOnGoal = it.shotsOnGoal - 1) else it }

    fun awayGoalIncrement() = _awayTeam.update { it.copy(goals = it.goals + 1) }
    fun awayGoalDecrement() = _awayTeam.update { if (it.goals > 0) it.copy(goals = it.goals - 1) else it }
    fun awayShotIncrement() = _awayTeam.update { it.copy(shotsOnGoal = it.shotsOnGoal + 1) }
    fun awayShotDecrement() = _awayTeam.update { if (it.shotsOnGoal > 0) it.copy(shotsOnGoal = it.shotsOnGoal - 1) else it }

    fun applySettings(settings: AppSettings) {
        _settings.update { settings }
        _homeTeam.update { it.copy(name = settings.homeTeamName, colorCode = settings.homeTeamColor) }
        _awayTeam.update { it.copy(name = settings.awayTeamName, colorCode = settings.awayTeamColor) }
    }

    fun resetScores() {
        _homeTeam.update { it.copy(goals = 0, shotsOnGoal = 0) }
        _awayTeam.update { it.copy(goals = 0, shotsOnGoal = 0) }
    }
}
