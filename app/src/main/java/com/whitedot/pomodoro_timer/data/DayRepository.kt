package com.whitedot.pomodoro_timer.data

import androidx.lifecycle.LiveData

class DayRepository(private val dayDao: DayDao) {

    val readAllData: LiveData<List<Day>> = dayDao.readAllData()

    suspend fun addUser(day: Day) {
        dayDao.addDay(day)
    }
}