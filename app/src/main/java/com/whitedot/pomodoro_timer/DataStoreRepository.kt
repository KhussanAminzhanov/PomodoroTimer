package com.whitedot.pomodoro_timer

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

const val PREFERENCE_NAME = "total_time"

class PomodoroPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    private val TAG: String = "UserPreferencesRepo"

    private object PreferencesKeys {
        val TOTAL_TIME = longPreferencesKey("total_time")
    }

    suspend fun saveToDataStore(totalTime: Long) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TOTAL_TIME] = totalTime
        }
    }

    val timerPreferencesFlow: Flow<Long> = dataStore.data
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

}