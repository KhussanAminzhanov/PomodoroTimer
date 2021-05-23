package com.whitedot.pomodoro_timer

import android.app.Application
import android.media.MediaPlayer
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.whitedot.pomodoro_timer.data.Day
import com.whitedot.pomodoro_timer.data.DayDatabase
import com.whitedot.pomodoro_timer.data.DayRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val COUNTDOWN_INTERVAL: Long = 1000
const val ONE_SESSION_TIME: Long = 25 * 60 * 1000
const val ONE_BREAK_TIME: Long = 5 * 60 * 1000

enum class TimerState {
    RUNNING, PAUSED, STOPPED
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var countDownTimer: CountDownTimer
    private lateinit var mediaPlayer: MediaPlayer

    private var isBreak = false

//    private var allData: LiveData<List<Day>>
//    private var repository: DayRepository

    private val _timeLeftInMilliseconds: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>(ONE_SESSION_TIME)
    }
    val timeLeftInMilliseconds: LiveData<Long> = _timeLeftInMilliseconds

    private val _timerIsRunning: MutableLiveData<TimerState> by lazy {
        MutableLiveData<TimerState>(TimerState.STOPPED)
    }
    val timerIsRunning: LiveData<TimerState> = _timerIsRunning

//    // Bug in that init block
//    init {
//        val dayDao = DayDatabase.getDatabase(application).dayDao()
//        repository = DayRepository(dayDao)
//        allData = repository.readAllData
//    }

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

//    //Room functions
//    fun addUser(day: Day) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.addUser(day)
//        }
//    }
}