package com.whitedot.pomodoro_timer

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.whitedot.pomodoro_timer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val model: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = model

        model.timeLeftInMilliseconds.observe(this, { newTime ->
            val minutes = newTime / 60000
            val seconds = newTime % 60000 / COUNTDOWN_INTERVAL
            val timeLeftString = "${addZero(minutes)}:${addZero(seconds)}"
            binding.timerTextView.text = timeLeftString
        })

        model.timerIsRunning.observe(this, { isRunning ->
            when (isRunning) {
                TimerState.STARTED -> {
                    binding.startPauseImageButton.apply {
                        setImageResource(R.drawable.ic_pause)
                        contentDescription = getString(R.string.pause_button)
                    }
                    binding.stopImageButton.visibility = View.VISIBLE
                }
                TimerState.PAUSED -> {
                    binding.startPauseImageButton.apply {
                        setImageResource(R.drawable.ic_play_arrow)
                        contentDescription = getString(R.string.play_button)
                    }
                }
                else -> {
                    binding.startPauseImageButton.apply {
                        setImageResource(R.drawable.ic_play_arrow)
                        contentDescription = getString(R.string.stop_button)
                    }
                    binding.stopImageButton.visibility = View.GONE
                }
            }
        })
    }

    private fun addZero(number: Long): String {
        return if (number < 10) "0$number" else number.toString()
    }
}