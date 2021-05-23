package com.whitedot.pomodoro_timer.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DayDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addDay(day: Day)

    @Query("SELECT * FROM week_day_data")
    fun readAllData(): LiveData<List<Day>>
}