package com.whitedot.pomodoro_timer.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class LabelRepository(private val labelDao: LabelDao) {

    val allLabels: Flow<List<Label>> = labelDao.getAlphabetizedLabels()

    @WorkerThread
    suspend fun insert(label: Label) {
        labelDao.insert(label)
    }
}