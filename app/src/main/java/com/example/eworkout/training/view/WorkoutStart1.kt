package com.example.eworkout.training.view

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.CountDownTimer
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
import com.example.eworkout.training.viewmodel.Workout1ViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [WorkoutStart1.newInstance] factory method to
 * create an instance of this fragment.
 */
class WorkoutStart1 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentWorkoutStart1Binding? = null
    val binding get() = _binding!!
    private val _viewModel: Workout1ViewModel by navGraphViewModels(R.id.training_nav)

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
        return binding.root    }

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
            WorkoutStart1().apply {
                }
            }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startCountDown()
    }

    private fun startCountDown()
    {
        val millisCountDown = (StringUlti.removeRepsPostfix(_viewModel.getCurrentExercise().reps)).toLong()
        val animation: ObjectAnimator =
            ObjectAnimator.ofInt(binding.countDownProgressbar,
                "progress",
                0, 10000)
        animation.duration = millisCountDown * 1000
        animation.interpolator = LinearInterpolator()
        animation.start()

        object : CountDownTimer(millisCountDown * 1000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                binding.repsTextview.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {

            }
        }.start()

    }
}