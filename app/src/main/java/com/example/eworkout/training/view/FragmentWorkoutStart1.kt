package com.example.eworkout.training.view

import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Chronometer
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.eworkout.R
import com.example.eworkout.databinding.FragmentWorkoutStart1Binding
import com.example.eworkout.training.model.UpdateState
import com.example.eworkout.training.util.StringUlti
import com.example.eworkout.training.viewmodel.SharedViewModel
import com.example.eworkout.training.viewmodel.WorkoutStartViewModel
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
    private val sharedViewModel: SharedViewModel by navGraphViewModels(R.id.training_nav)
    private val viewModel: WorkoutStartViewModel by viewModels()

    private lateinit var countDownTimer : SonicCountDownTimer
    private lateinit var animation: ObjectAnimator
    private var isPaused = false
    private lateinit var timer : Chronometer
    private lateinit var mediaPlayer : MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            Log.d("FragmentStart1", it.getString("set_id").toString())
            Log.d("FragmentStart1", it.getBoolean("isSystemSet").toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWorkoutStart1Binding.inflate(inflater, container, false)
        binding.viewModel = sharedViewModel
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
        observeViewModel()

        startSoundEffect()

        var timeToCount = (StringUlti.removeRepsPostfix(sharedViewModel.getCurrentExercise().reps)).toLong()
        timeToCount *= 1000

        setAnimation()

        startProgressAnimation(timeToCount)

        startCountDown(timeToCount)

        setListener()

        startCountUp()
    }

    private fun observeViewModel() {
        viewModel.updateState.observe(viewLifecycleOwner)
        {
            handleState(it)
        }
    }

    private fun handleState(state: UpdateState?) {
        when(state)
        {
            UpdateState.DONE -> {
                findNavController().navigate(R.id.action_fragmentWorkoutStart1_to_fragmentWorkoutDone, arguments)
            }

            else -> {
                viewModel.setTakenID = requireArguments().getString("set_id").toString()
            }
        }
    }

    private fun startSoundEffect()
    {
        mediaPlayer = MediaPlayer.create(context, R.raw.trumpet_sound_effect)
        mediaPlayer.start() // no need to call prepare(); create() does that for you
    }
    private fun startCountUp()
    {
        timer = Chronometer(context)
        timer.start()
    }

    private fun setAnimation() {
        binding.backgroundAnimationView.setAnimation(sharedViewModel.getCurrentExercise().name + ".json")
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
            object : SonicCountDownTimer(millisCountDown, 1000) {
            override fun onTimerTick(timeRemaining: Long) {
                binding.repsTextview.text = (timeRemaining / 1000).toString()
            }

            override fun onTimerFinish() {
                Log.d("navigate", "timer fninish")
                if(sharedViewModel.increaseCurrentExerciseIndex()) // If can increase mean it is not the last exercise so we navigate to rest fragment
                {
                    navigateToRest()
                }
                else{ // Else we update calories and navigate to done fragment
                    updateCalo()
                }
            }
        }
        countDownTimer.startCountDownTimer()
    }

    private fun setListener()
    {
        binding.cameraBtn.setOnClickListener {
            navigateToCamera()
        }
        binding.btnPrevious.setOnClickListener {
            navigateToPreviousExercise()
        }
        binding.btnMiddle.setOnClickListener {
            if(isPaused)
            {
                //Change to pause button
                isPaused = false
                it.background =  requireContext().resources.getDrawable(R.drawable.btn_pause_training_background , requireContext().theme)
                resumeCountDown()
                resumeProgress()
            }
            else
            {
                //Change to play button
                isPaused = true
                it.background =  requireContext().resources.getDrawable(R.drawable.btn_play_training_background, requireContext().theme)
                pauseCountDown()
                pauseProgress()
            }
        }
        binding.btnNext.setOnClickListener {
            if(sharedViewModel.increaseCurrentExerciseIndex()) // If can increase mean it is not the last exercise so we navigate to rest fragment
            {
                navigateToRest()
            }
            else{ // Else we update calories and navigate to done fragment
                updateCalo()
            }
        }

        binding.exerciseInformationBtn.setOnClickListener {
            navigateToInformation()
        }

        binding.backgroundAnimationView.setFailureListener {
            binding.backgroundImageview.visibility = View.VISIBLE
            binding.backgroundAnimationView.visibility = View.GONE
        }
    }

    private fun navigateToInformation()
    {
        val bundle = Bundle().apply {
            putString("exercise_id", sharedViewModel.getCurrentExercise().id)
        }
        findNavController().navigate(R.id.action_fragmentWorkoutStart1_to_workoutDetail2, bundle)
    }

    private fun updateCalo()
    {
        val sec = (SystemClock.elapsedRealtime() - timer.base) / 1000
        val MET = sharedViewModel.getCurrentExercise().MET
        val ID = sharedViewModel.setTakenID
        viewModel.calculateAndUpdate(sec, MET, ID)
    }

    private fun navigateToRest()
    {
        val sec = (SystemClock.elapsedRealtime() - timer.base) / 1000
        val MET = sharedViewModel.getCurrentExercise().MET
        viewModel.calculateKcal(sec, MET)
        findNavController().navigate(R.id.action_fragmentWorkoutStart1_to_fragmentWorkoutRest, arguments)
    }

    private fun navigateToCamera()
    {
        findNavController().navigate(R.id.action_fragmentWorkoutStart1_to_AI)
    }

    private fun navigateToPreviousExercise()
    {
        sharedViewModel.decreaseCurrentExerciseIndex()
        findNavController().navigate(R.id.action_fragmentWorkoutStart1_to_fragmentWorkoutRest, arguments)
    }

    private fun pauseCountDown()
    {
        Log.d("test timer", (SystemClock.elapsedRealtime() - timer.base).toString())
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
        Log.d("navigate", "destroyview")
        countDownTimer.cancelCountDownTimer()
        timer.stop()
        mediaPlayer.stop()
    }
}