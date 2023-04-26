package com.example.eworkout.training.view

import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.eworkout.R
import com.example.eworkout.databinding.FragmentWorkoutRestBinding
import com.example.eworkout.training.viewmodel.SharedViewModel
import com.example.eworkout.training.viewmodel.Workout1ViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentWorkoutRest.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentWorkoutRest : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentWorkoutRestBinding? = null
    val binding get() = _binding!!
    private val _viewModel: SharedViewModel by navGraphViewModels(R.id.training_nav)
    private lateinit var countDownTimer : CountDownTimer
    private lateinit var animation: ObjectAnimator
    private lateinit var mediaPlayer : MediaPlayer
    private var timeRemaining = 0L
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
        _binding = FragmentWorkoutRestBinding.inflate(inflater, container, false)
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
         * @return A new instance of fragment FragmentWorkoutRest.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentWorkoutRest().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startSoundEffect()
        setListener()
        setAnimation()
        startProgressAnimation(30000)
        startCountDown(30000)
    }

    private fun startSoundEffect()
    {
        mediaPlayer = MediaPlayer.create(context, R.raw.ting_sound_effect)
        mediaPlayer.start() // no need to call prepare(); create() does that for you
    }

    private fun setAnimation() {
        binding.backgroundAnimationview.setAnimation(_viewModel.getCurrentExercise().name + ".json")
    }

    private fun setListener() {
        binding.skipTextView.setOnClickListener {
            if(_viewModel.getCurrentExercise().reps.contains("s"))
                findNavController().navigate(R.id.action_fragmentWorkoutRest_to_fragmentWorkoutStart1)
            else
                findNavController().navigate(R.id.action_fragmentWorkoutRest_to_fragmentWorkoutStart2)
        }

        binding.add10sTextView.setOnClickListener {
            animation.duration += 10000
            cancelAndStartNewCountDown(timeRemaining + 10000)
        }

        binding.backgroundAnimationview.setFailureListener {
            binding.backgroundImageview.visibility = View.VISIBLE
            binding.backgroundAnimationview.visibility = View.GONE
        }
    }

    private fun cancelAndStartNewCountDown(millisCountDown: Long)
    {
        countDownTimer.cancel()
        startCountDown(millisCountDown)
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

            override fun onTick(timeRemaining: Long) {
                this@FragmentWorkoutRest.timeRemaining = timeRemaining
                binding.repsTextview.text = (timeRemaining / 1000).toString()
            }

            override fun onFinish() {
                if(_viewModel.getCurrentExercise().reps.contains("s"))
                    findNavController().navigate(R.id.action_fragmentWorkoutRest_to_fragmentWorkoutStart1)
                else
                    findNavController().navigate(R.id.action_fragmentWorkoutRest_to_fragmentWorkoutStart2)
            }

        }
        countDownTimer.start()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel()
        mediaPlayer.stop()
    }
}