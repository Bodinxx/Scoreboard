package com.laxstat.scoreboard.data.db.dao

import androidx.room.*
import com.laxstat.scoreboard.data.db.entities.GameEvent
import com.laxstat.scoreboard.data.db.entities.EventType
import kotlinx.coroutines.flow.Flow

@Dao
interface GameEventDao {
    @Query("SELECT * FROM game_events WHERE gameId = :gameId ORDER BY timestamp DESC")
    fun getEventsForGame(gameId: Long): Flow<List<GameEvent>>

    @Query("SELECT * FROM game_events WHERE gameId = :gameId AND type = :type ORDER BY timestamp DESC")
    fun getEventsByType(gameId: Long, type: EventType): Flow<List<GameEvent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: GameEvent): Long

    @Delete
    suspend fun deleteEvent(event: GameEvent)

    @Query("DELETE FROM game_events WHERE gameId = :gameId")
    suspend fun deleteAllEventsForGame(gameId: Long)
}
