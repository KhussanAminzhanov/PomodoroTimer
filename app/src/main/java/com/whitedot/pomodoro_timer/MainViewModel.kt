package com.whitedot.pomodoro_timer

import android.app.Application
import android.media.MediaPlayer
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

const val COUNTDOWN_INTERVAL: Long = 1000
const val ONE_SESSION_TIME: Long = 25 * 60 * 1000
const val ONE_BREAK_TIME: Long = 5 * 60 * 1000

enum class TimerState {
    STARTED, PAUSED, STOPPED
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var countDownTimer: CountDownTimer
    private lateinit var mediaPlayer: MediaPlayer

    private val _timeLeftInMilliseconds: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>(ONE_SESSION_TIME)
    }
    val timeLeftInMilliseconds: LiveData<Long> = _timeLeftInMilliseconds

    private val _timerIsRunning: MutableLiveData<TimerState> by lazy {
        MutableLiveData<TimerState>(TimerState.STOPPED)
    }
    val timerIsRunning: LiveData<TimerState> = _timerIsRunning

    private var isBreak = false

    fun startOrPauseTimer() {
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
                _timeLeftInMilliseconds.value = ONE_SESSION_TIME
                _timerIsRunning.value = TimerState.STOPPED

                mediaPlayer = MediaPlayer.create(getApplication(), R.raw.timer)
                mediaPlayer.setOnCompletionListener { mediaPlayer.release() }
                mediaPlayer.start()
            }

        }.start()
    }

    private fun pauseTimer() {
        countDownTimer.cancel()
        _timerIsRunning.value = TimerState.PAUSED
    }

    fun stopTimer() {
        if (_timerIsRunning.value!! != TimerState.STOPPED) {
            countDownTimer.cancel()
            _timerIsRunning.value = TimerState.STOPPED
            _timeLeftInMilliseconds.value = ONE_SESSION_TIME
        }
    }

    fun changeTimerIntervalLength() {
        if (isBreak) {
            _timeLeftInMilliseconds.value = ONE_SESSION_TIME
        } else {
            _timeLeftInMilliseconds.value = ONE_BREAK_TIME
        }
        isBreak = !isBreak

    }
}