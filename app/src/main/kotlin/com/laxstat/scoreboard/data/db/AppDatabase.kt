package com.laxstat.scoreboard.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.laxstat.scoreboard.data.db.dao.GameEventDao
import com.laxstat.scoreboard.data.db.dao.PlayerDao
import com.laxstat.scoreboard.data.db.dao.TeamDao
import com.laxstat.scoreboard.data.db.entities.GameEvent
import com.laxstat.scoreboard.data.db.entities.EventType
import com.laxstat.scoreboard.data.db.entities.Player
import com.laxstat.scoreboard.data.db.entities.Team

class EventTypeConverters {
    @TypeConverter
    fun fromEventType(value: EventType): String = value.name
    @TypeConverter
    fun toEventType(value: String): EventType = EventType.valueOf(value)
}

@Database(entities = [Team::class, Player::class, GameEvent::class], version = 1, exportSchema = false)
@TypeConverters(EventTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun teamDao(): TeamDao
    abstract fun playerDao(): PlayerDao
    abstract fun gameEventDao(): GameEventDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "laxstat.db"
                ).build().also { INSTANCE = it }
            }
    }
}
