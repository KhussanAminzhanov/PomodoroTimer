package com.whitedot.pomodoro_timer.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Day::class], version = 1, exportSchema = false)
abstract class DayDatabase : RoomDatabase() {

    abstract fun dayDao(): DayDao

    companion object {
        @Volatile
        private var INSTANCE: DayDatabase? = null

        fun getDatabase(context: Context): DayDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DayDatabase::class.java,
                    "day_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}