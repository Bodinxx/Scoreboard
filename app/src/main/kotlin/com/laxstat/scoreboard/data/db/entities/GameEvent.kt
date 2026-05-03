package com.laxstat.scoreboard.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class EventType { GOAL, ASSIST, PENALTY }

@Entity(tableName = "game_events")
data class GameEvent(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val gameId: Long = 1L,
    val teamId: Long,
    val playerId: Long? = null,
    val assistPlayerId1: Long? = null,
    val assistPlayerId2: Long? = null,
    val type: EventType,
    val subType: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
