package com.whitedot.pomodoro_timer.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "label_table")
data class Label(
    @PrimaryKey @ColumnInfo(name = "label") val label: String
)