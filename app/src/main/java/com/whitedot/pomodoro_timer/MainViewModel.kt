package com.whitedot.pomodoro_timer

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

const val COUNTDOWN_INTERVAL: Long = 1000
const val COUNTDOWN_TOTAL_TIME_IN_MILLISECONDS: Long = 600000

enum class TimerState {
    STARTED, PAUSED, STOPPED
}

class MainViewModel : ViewModel() {

    private lateinit var countDownTimer: CountDownTimer

    private val _timeLeftInMilliseconds: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>(COUNTDOWN_TOTAL_TIME_IN_MILLISECONDS)
    }
    val timeLeftInMilliseconds: LiveData<Long> = _timeLeftInMilliseconds

    private val _timerIsRunning: MutableLiveData<TimerState> by lazy {
        MutableLiveData<TimerState>(TimerState.STOPPED)
    }
    val timerIsRunning: LiveData<TimerState> = _timerIsRunning

    fun startOrStopTimer() {
        if (_timerIsRunning.value!! == TimerState.STARTED) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        _timerIsRunning.value = TimerState.STARTED
        countDownTimer = object : CountDownTimer(_timeLeftInMilliseconds.value!!, COUNTDOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                _timeLeftInMilliseconds.value = millisUntilFinished
            }

            override fun onFinish() {
                _timeLeftInMilliseconds.value = COUNTDOWN_TOTAL_TIME_IN_MILLISECONDS
                _timerIsRunning.value = TimerState.STOPPED
            }

        }.start()
    }

    private fun pauseTimer() {
        countDownTimer.cancel()
        _timerIsRunning.value = TimerState.PAUSED
    }

    fun stopTimer() {
        if (_timerIsRunning.value!! == TimerState.STARTED) {
            countDownTimer.cancel()
            _timerIsRunning.value = TimerState.STOPPED
            _timeLeftInMilliseconds.value = COUNTDOWN_TOTAL_TIME_IN_MILLISECONDS
        }
    }
}