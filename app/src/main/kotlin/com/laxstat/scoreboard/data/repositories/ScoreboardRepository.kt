package com.laxstat.scoreboard.data.repositories

import android.content.Context
import com.laxstat.scoreboard.data.db.AppDatabase
import com.laxstat.scoreboard.data.db.entities.GameEvent
import com.laxstat.scoreboard.data.db.entities.Player
import com.laxstat.scoreboard.data.db.entities.Team
import kotlinx.coroutines.flow.Flow

class ScoreboardRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val teamDao = db.teamDao()
    private val playerDao = db.playerDao()
    private val gameEventDao = db.gameEventDao()

    fun getAllTeams(): Flow<List<Team>> = teamDao.getAllTeams()
    fun getPlayersForTeam(teamId: Long): Flow<List<Player>> = playerDao.getPlayersForTeam(teamId)
    fun getAllPlayers(): Flow<List<Player>> = playerDao.getAllPlayers()
    fun getEventsForGame(gameId: Long): Flow<List<GameEvent>> = gameEventDao.getEventsForGame(gameId)

    suspend fun insertTeam(team: Team): Long = teamDao.insertTeam(team)
    suspend fun updateTeam(team: Team) = teamDao.updateTeam(team)
    suspend fun deleteTeam(team: Team) = teamDao.deleteTeam(team)

    suspend fun insertPlayer(player: Player): Long = playerDao.insertPlayer(player)
    suspend fun updatePlayer(player: Player) = playerDao.updatePlayer(player)
    suspend fun deletePlayer(player: Player) = playerDao.deletePlayer(player)

    suspend fun insertEvent(event: GameEvent): Long = gameEventDao.insertEvent(event)
    suspend fun deleteEvent(event: GameEvent) = gameEventDao.deleteEvent(event)
    suspend fun clearGame(gameId: Long) = gameEventDao.deleteAllEventsForGame(gameId)
}
