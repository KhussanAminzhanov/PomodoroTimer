package com.whitedot.pomodoro_timer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LabelDao {

    @Query("SELECT * FROM label_table ORDER BY label ASC")
    fun getAlphabetizedLabels(): Flow<List<Label>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(label: Label)

    @Query("DELETE FROM label_table")
    suspend fun deleteAll()
}