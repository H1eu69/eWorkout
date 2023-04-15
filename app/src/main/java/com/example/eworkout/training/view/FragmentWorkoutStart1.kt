package com.example.eworkout.training.view

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.eworkout.R
import com.example.eworkout.databinding.FragmentWorkoutStart1Binding
import com.example.eworkout.training.util.StringUlti
import com.example.eworkout.training.viewmodel.Workout1SharedViewModel
import com.orbitalsonic.sonictimer.SonicCountDownTimer

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentWorkoutStart1.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentWorkoutStart1 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentWorkoutStart1Binding? = null
    val binding get() = _binding!!
    private val _viewModel: Workout1SharedViewModel by navGraphViewModels(R.id.training_nav)
    private lateinit var countDownTimer : SonicCountDownTimer
    private lateinit var animation: ObjectAnimator
    private var isPaused = false
    private var millisLeft = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWorkoutStart1Binding.inflate(inflater, container, false)
        binding.viewModel = _viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WorkoutStart.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentWorkoutStart1().apply {
                }
            }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var time = (StringUlti.removeRepsPostfix(_viewModel.getCurrentExercise().reps)).toLong()
        time *= 1000

        startProgressAnimation(time)

        startCountDown(time)

        setOnClickListener()
    }

    private fun startProgressAnimation(millisCountDown: Long)
    {
        animation = ObjectAnimator.ofInt(binding.countDownProgressbar,
                "progress",
                0, 10000)
        animation.duration = millisCountDown
        animation.interpolator = LinearInterpolator()
        animation.start()
    }

    private fun startCountDown(millisCountDown: Long)
    {
        countDownTimer =
            object : SonicCountDownTimer(millisCountDown, 1000)
        {
            override fun onTimerTick(timeRemaining: Long) {
                binding.repsTextview.text = (timeRemaining / 1000).toString()
            }

            override fun onTimerFinish() {
                Log.d("sa","dsa")
            }

        }
        countDownTimer.startCountDownTimer()
    }

    private fun setOnClickListener()
    {
        binding.btnPrevious.setOnClickListener {
            _viewModel.decreaseCurrentExerciseIndex()
            findNavController().navigate(R.id.action_fragmentWorkoutStart2_to_fragmentWorkoutRest)
        }
        binding.btnMiddle.setOnClickListener {
            if(isPaused)
            {
                isPaused = false
                it.background =  requireContext().resources.getDrawable(R.drawable.btn_pause_training_background , requireContext().theme)
                resumeCountDown()
                resumeProgress()
            }
            else
            {
                isPaused = true
                it.background =  requireContext().resources.getDrawable(R.drawable.btn_play_training_background, requireContext().theme)
                pauseCountDown()
                pauseProgress()
            }
        }
        binding.btnNext.setOnClickListener {
            _viewModel.increaseCurrentExerciseIndex()
            findNavController().navigate(R.id.action_fragmentWorkoutStart2_to_fragmentWorkoutRest)
        }
    }

    private fun pauseCountDown()
    {
        countDownTimer.pauseCountDownTimer()
    }

    private fun resumeCountDown()
    {
        countDownTimer.resumeCountDownTimer()
    }

    private fun pauseProgress()
    {
        animation.pause()
    }

    private fun resumeProgress()
    {
        animation.resume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.stopCountDownTimer()
    }
}