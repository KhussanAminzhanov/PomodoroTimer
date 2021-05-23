package com.whitedot.pomodoro_timer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.whitedot.pomodoro_timer.databinding.FragmentTimerBinding

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

        binding?.apply {
            viewModel = sharedViewModel
            timerLabelTextView.setOnClickListener { changeLabel() }
        }

        sharedViewModel.timeLeftInMilliseconds.observe(viewLifecycleOwner, { newTime ->
            val minutes = newTime / 60000
            val seconds = newTime % 60000 / COUNTDOWN_INTERVAL
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

    private fun changeLabel() {
        findNavController().navigate(R.id.action_timerFragment_to_labelFragment)
    }
}