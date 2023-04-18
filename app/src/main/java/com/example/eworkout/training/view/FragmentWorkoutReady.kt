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
import com.example.eworkout.databinding.FragmentWorkoutReadyBinding
import com.example.eworkout.training.viewmodel.Workout1SharedViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [FragmentWorkoutReady.newInstance] factory method to
 * create an instance of this fragment.
 */// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FragmentWorkoutReady : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentWorkoutReadyBinding? = null
    val binding get() = _binding!!
    private val _viewModel: Workout1SharedViewModel by navGraphViewModels(R.id.training_nav)
    private lateinit var countDownTimer : CountDownTimer
    private lateinit var animation: ObjectAnimator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWorkoutReadyBinding.inflate(inflater, container, false)
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
         * @return A new instance of fragment FragmentWorkoutReady.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentWorkoutReady().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        startProgressAnimation(16000)
        startCountDown(16000)
    }

    private fun setOnClickListener() {
        binding.btnStart.setOnClickListener {
            if(_viewModel.getCurrentExercise().reps.contains("s"))
                findNavController().navigate(R.id.action_fragmentWorkoutReady_to_fragmentWorkoutStart1)
            else
                findNavController().navigate(R.id.action_fragmentWorkoutReady_to_fragmentWorkoutStart2)
        }
    }

    private fun startProgressAnimation(millisCountDown: Long)
    {
        animation =
        ObjectAnimator.ofInt(binding.countDownProgressbar,
            "progress",
            0, 10000)
        animation.duration = millisCountDown
        animation.interpolator = LinearInterpolator()
        animation.start()
    }

    private fun startCountDown(millisCountDown: Long)
    {
        countDownTimer = object : CountDownTimer(millisCountDown, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                binding.countDownTimeTextview.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                if(_viewModel.getCurrentExercise().reps.contains("s"))
                    findNavController().navigate(R.id.action_fragmentWorkoutReady_to_fragmentWorkoutStart1)
                else
                    findNavController().navigate(R.id.action_fragmentWorkoutReady_to_fragmentWorkoutStart2)
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel()
    }
}