package com.whitedot.pomodoro_timer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "week_day_data")
data class Day(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val totalFocusTime: Long,
    val weekDay: Int
)