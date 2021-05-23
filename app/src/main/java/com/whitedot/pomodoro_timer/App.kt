package com.whitedot.pomodoro_timer

import android.app.Application
import com.whitedot.pomodoro_timer.data.LabelRepository
import com.whitedot.pomodoro_timer.data.LabelRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class App : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val labelDatabase by lazy { LabelRoomDatabase.getDatabase(this, applicationScope) }
    val labelRepository by lazy { LabelRepository(labelDatabase.labelDao()) }
}