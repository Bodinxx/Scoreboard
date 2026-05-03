package com.laxstat.scoreboard.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.laxstat.scoreboard.data.db.entities.EventType
import com.laxstat.scoreboard.data.db.entities.GameEvent
import com.laxstat.scoreboard.data.repositories.ScoreboardRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GameMemoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = ScoreboardRepository(application)

    val currentGameId = 1L

    val events: StateFlow<List<GameEvent>> = repo.getEventsForGame(currentGameId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun logGoal(teamId: Long, scorerId: Long? = null, assist1Id: Long? = null, assist2Id: Long? = null) {
        viewModelScope.launch {
            repo.insertEvent(
                GameEvent(
                    gameId = currentGameId,
                    teamId = teamId,
                    playerId = scorerId,
                    assistPlayerId1 = assist1Id,
                    assistPlayerId2 = assist2Id,
                    type = EventType.GOAL
                )
            )
        }
    }

    fun logPenalty(teamId: Long, playerId: Long? = null, infractionType: String) {
        viewModelScope.launch {
            repo.insertEvent(
                GameEvent(
                    gameId = currentGameId,
                    teamId = teamId,
                    playerId = playerId,
                    type = EventType.PENALTY,
                    subType = infractionType
                )
            )
        }
    }

    fun deleteEvent(event: GameEvent) {
        viewModelScope.launch { repo.deleteEvent(event) }
    }

    fun clearAllEvents() {
        viewModelScope.launch { repo.clearGame(currentGameId) }
    }
}
