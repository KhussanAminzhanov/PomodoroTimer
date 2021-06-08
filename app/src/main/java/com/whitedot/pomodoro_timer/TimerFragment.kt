package com.whitedot.pomodoro_timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.whitedot.pomodoro_timer.databinding.FragmentTimerBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TimerFragment : Fragment() {

    private var binding: FragmentTimerBinding? = null
    private val sharedViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentTimerBinding = FragmentTimerBinding.inflate(inflater, container, false)
        binding = fragmentTimerBinding
        return fragmentTimerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.updateCurrentDate()

        binding?.apply {
            viewModel = sharedViewModel
        }

        sharedViewModel.currentDay.observe(viewLifecycleOwner, { newCurrentDay ->
            binding!!.timerDateTextView.text = newCurrentDay
            if (!newCurrentDay.equals(
                    LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)
                )
            ) {
                sharedViewModel.resetTotalTime()
            }
        })

        sharedViewModel.timeLeftInMilliseconds.observe(viewLifecycleOwner, { newTime ->
            val minutes = newTime / ONE_MINUTE
            val seconds = newTime % ONE_MINUTE / COUNTDOWN_INTERVAL
            val timeLeftString = "${addZero(minutes)}:${addZero(seconds)}"
            binding?.apply { timerTextView.text = timeLeftString }
        })

        sharedViewModel.timerIsRunning.observe(viewLifecycleOwner, { isRunning ->
            when (isRunning) {
                TimerState.RUNNING -> {
                    updateButtons(R.drawable.ic_pause, R.string.pause_button, View.VISIBLE)
                }
                TimerState.PAUSED -> {
                    updateButtons(R.drawable.ic_play_arrow, R.string.play_button, View.VISIBLE)
                }
                else -> {
                    updateButtons(R.drawable.ic_play_arrow, R.string.play_button, View.GONE)
                }
            }
        })

        sharedViewModel.totalTime.observe(viewLifecycleOwner, { totalTime ->
            val hours: Long = totalTime / (60 * ONE_MINUTE)
            val minutes: Long = totalTime % (60 * ONE_MINUTE) / ONE_MINUTE

            var totalTimeString = "$minutes minutes"
            if (hours > 0) {
                totalTimeString = "$hours hours $totalTimeString"
            }

            binding?.apply { totalSessionsCounterTextView.text = totalTimeString }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun updateButtons(imageResource: Int, stringResource: Int, visibility: Int) {
        binding?.apply {
            startPauseImageButton.apply {
                setImageResource(imageResource)
                contentDescription = getString(stringResource)
            }
            stopImageButton.visibility = visibility
        }
    }

    private fun addZero(number: Long): String {
        return if (number < 10) "0$number" else number.toString()
    }
}