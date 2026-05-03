package com.laxstat.scoreboard.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teams")
data class Team(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val colorCode: String = "#1565C0"
)
