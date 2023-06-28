package com.seuit.eworkout.training.view

import android.media.MediaPlayer
import android.os.Bundle
import android.os.SharedMemory
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.seuit.eworkout.R
import com.seuit.eworkout.databinding.FragmentWorkoutStart2Binding
import com.seuit.eworkout.training.model.UpdateState
import com.seuit.eworkout.training.viewmodel.SharedViewModel
import com.seuit.eworkout.training.viewmodel.Workout1ViewModel
import com.seuit.eworkout.training.viewmodel.WorkoutStartViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentWorkoutStart2.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentWorkoutStart2 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentWorkoutStart2Binding? = null
    val binding get() = _binding!!
    private val _sharedViewModel: SharedViewModel by navGraphViewModels(R.id.training_nav)
    private val viewModel: WorkoutStartViewModel by viewModels()
    private lateinit var timer : Chronometer
    private lateinit var mediaPlayer : MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            Log.d("FragmentStart2", it.getString("set_id").toString())
            Log.d("FragmentStart2", it.getBoolean("isSystemSet").toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWorkoutStart2Binding.inflate(inflater, container, false)
        binding.viewModel = _sharedViewModel
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
         * @return A new instance of fragment FragmentWorkoutStart2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentWorkoutStart2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        startSoundEffect()
        setAnimation()
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
                findNavController().navigate(R.id.action_fragmentWorkoutStart2_to_fragmentWorkoutDone, arguments)
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
        binding.backgroundAnimationView.setAnimation(_sharedViewModel.getCurrentExercise().name + ".json")
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
            onClick()
        }
        binding.btnNext.setOnClickListener {
            onClick()
        }
        binding.exerciseInformationBtn.setOnClickListener {
            viewInformation()
        }
        binding.backgroundAnimationView.setFailureListener {
            binding.backgroundImageview.visibility = View.VISIBLE
            binding.backgroundAnimationView.visibility = View.GONE
        }
    }

    private fun navigateToCamera()
    {
        findNavController().navigate(R.id.action_fragmentWorkoutStart2_to_AI)
    }

    private fun navigateToPreviousExercise()
    {
        _sharedViewModel.decreaseCurrentExerciseIndex()
        findNavController().navigate(R.id.action_fragmentWorkoutStart2_to_fragmentWorkoutRest, arguments)
    }

    private fun viewInformation()
    {
        val bundle = Bundle().apply {
            putString("exercise_id", _sharedViewModel.getCurrentExercise().id)
        }
        findNavController().navigate(R.id.action_fragmentWorkoutStart2_to_workoutDetail2, bundle)
    }

    private fun onClick()
    {
        val sec = (SystemClock.elapsedRealtime() - timer.base) / 1000
        val MET = _sharedViewModel.getCurrentExercise().MET
        val ID = _sharedViewModel.setTakenID
        if(_sharedViewModel.increaseCurrentExerciseIndex()){
            viewModel.calculateKcal(sec, MET)
            findNavController().navigate(R.id.action_fragmentWorkoutStart2_to_fragmentWorkoutRest, arguments)
        }
        else{
            viewModel.calculateAndUpdate(sec, MET, ID)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer.stop()
    }
}