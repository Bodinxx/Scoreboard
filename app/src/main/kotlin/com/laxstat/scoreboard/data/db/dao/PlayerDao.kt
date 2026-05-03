package com.laxstat.scoreboard.data.db.dao

import androidx.room.*
import com.laxstat.scoreboard.data.db.entities.Player
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("SELECT * FROM players WHERE teamId = :teamId ORDER BY jerseyNumber ASC")
    fun getPlayersForTeam(teamId: Long): Flow<List<Player>>

    @Query("SELECT * FROM players ORDER BY teamId, jerseyNumber ASC")
    fun getAllPlayers(): Flow<List<Player>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: Player): Long

    @Update
    suspend fun updatePlayer(player: Player)

    @Delete
    suspend fun deletePlayer(player: Player)
}
