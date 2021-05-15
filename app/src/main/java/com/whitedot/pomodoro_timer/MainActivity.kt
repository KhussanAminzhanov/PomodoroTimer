package com.whitedot.pomodoro_timer

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginEnd

class MainActivity : AppCompatActivity() {

    private val model: MainViewModel by viewModels()
//    private lateinit var binding: ActivityMainBinding

    private lateinit var timerTextView: TextView
    private lateinit var startPauseImageButton: ImageButton
    private lateinit var stopImageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        binding.viewModel = model

        timerTextView = findViewById(R.id.timer_text_view)
        startPauseImageButton = findViewById(R.id.start_pause_button)
        stopImageButton = findViewById(R.id.stop_button)

        model.timeLeftInMilliseconds.observe(this, { newTime ->
            val minutes = newTime / 60000
            val seconds = newTime % 60000 / COUNTDOWN_INTERVAL
            val timeLeftString = "${addZero(minutes)}:${addZero(seconds)}"
            timerTextView.text = timeLeftString
        })

        model.timerIsRunning.observe(this, { isRunning ->
            when(isRunning) {
                TimerState.STARTED -> {
                    startPauseImageButton.setImageResource(R.drawable.ic_pause)
                    stopImageButton.visibility = View.VISIBLE
                }
                TimerState.PAUSED -> {
                    startPauseImageButton.setImageResource(R.drawable.ic_play_arrow)
                }
                else -> {
                    startPauseImageButton.setImageResource(R.drawable.ic_play_arrow)
                    stopImageButton.visibility = View.GONE
                }
            }
        })

        startPauseImageButton.setOnClickListener {
            model.startOrStopTimer()
        }

        stopImageButton.setOnClickListener {
            model.stopTimer()
        }
    }

    private fun addZero(number: Long): String {
        return if (number < 10) "0$number" else number.toString()
    }
}