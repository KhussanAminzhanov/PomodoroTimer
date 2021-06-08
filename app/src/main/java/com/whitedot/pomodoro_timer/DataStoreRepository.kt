package com.whitedot.pomodoro_timer

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PomodoroPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    private val TAG: String = "UserPreferencesRepo"

    private object PreferencesKeys {
        val TOTAL_TIME = longPreferencesKey("total_time")
        val CURRENT_DATE = stringPreferencesKey("current_date")
    }

    suspend fun saveTotalTime(totalTime: Long) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TOTAL_TIME] = totalTime
        }
    }

    suspend fun saveCurrentDate(currentDate: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CURRENT_DATE] = currentDate
        }
    }


    val totalTimePreferenceFlow: Flow<Long> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preference ->
            val totalTime: Long = preference[PreferencesKeys.TOTAL_TIME] ?: 0
            totalTime
        }

    val currentDatePreferenceFlow: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preference ->
            val currentDate: String = preference[PreferencesKeys.CURRENT_DATE] ?: LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)
            currentDate
        }

}