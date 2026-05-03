package com.laxstat.scoreboard.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "players",
    foreignKeys = [ForeignKey(
        entity = Team::class,
        parentColumns = ["id"],
        childColumns = ["teamId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Player(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val teamId: Long,
    val jerseyNumber: Int,
    val name: String
)
