package com.whitedot.pomodoro_timer

import android.app.Application
import android.media.MediaPlayer
import android.os.CountDownTimer
import androidx.lifecycle.*
import kotlinx.coroutines.launch

const val COUNTDOWN_INTERVAL: Long = 1000
const val ONE_MINUTE: Long = 60000
const val ONE_SESSION_TIME: Long = 25 * ONE_MINUTE
const val ONE_BREAK_TIME: Long = 5 * ONE_MINUTE

enum class TimerState {
    RUNNING, PAUSED, STOPPED
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var countDownTimer: CountDownTimer
    private lateinit var mediaPlayer: MediaPlayer

    private var dataStoreRepository = PomodoroPreferencesRepository(application.dataStore)
    val totalTime = dataStoreRepository.timerPreferencesFlow.asLiveData()
    private var isBreak = false

    private val _timeLeftInMilliseconds: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>(ONE_SESSION_TIME)
    }
    val timeLeftInMilliseconds: LiveData<Long> = _timeLeftInMilliseconds

    private val _timerIsRunning: MutableLiveData<TimerState> by lazy {
        MutableLiveData<TimerState>(TimerState.STOPPED)
    }
    val timerIsRunning: LiveData<TimerState> = _timerIsRunning

    fun startOrPauseTimer() {
        if (_timerIsRunning.value!! == TimerState.RUNNING) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        _timerIsRunning.value = TimerState.RUNNING
        countDownTimer = object : CountDownTimer(_timeLeftInMilliseconds.value!!, COUNTDOWN_INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                _timeLeftInMilliseconds.value = millisUntilFinished
            }

            override fun onFinish() {
                _timerIsRunning.value = TimerState.STOPPED

                if (!isBreak) {
                    viewModelScope.launch {
                        saveTotalTimeSpent(totalTime.value?.plus(ONE_SESSION_TIME)!!)
                    }
                }

                changeTimerIntervalLength()

                mediaPlayer = MediaPlayer.create(getApplication(), R.raw.ding)
                mediaPlayer.setOnCompletionListener { mediaPlayer.release() }
                mediaPlayer.start()
            }

        }.start()
    }

    private fun pauseTimer() {
        countDownTimer.cancel()
        _timerIsRunning.value = TimerState.PAUSED
    }

    private suspend fun saveTotalTimeSpent(newTotalTime: Long) {
        dataStoreRepository.saveToDataStore(newTotalTime)
    }

    fun stopTimer() {
        if (_timerIsRunning.value != TimerState.STOPPED) {
            countDownTimer.cancel()
            _timerIsRunning.value = TimerState.STOPPED
            _timeLeftInMilliseconds.value = ONE_SESSION_TIME
        }
    }

    fun changeTimerIntervalLength() {
        if (_timerIsRunning.value == TimerState.STOPPED) {
            _timeLeftInMilliseconds.value = if (isBreak) ONE_SESSION_TIME else ONE_BREAK_TIME
            isBreak = !isBreak
        }
    }
    
    fun resetTotalTime() {
        viewModelScope.launch { dataStoreRepository.saveToDataStore(0) }
    }
}