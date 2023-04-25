package com.example.eworkout.training.view

import android.os.Bundle
import android.os.SharedMemory
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.eworkout.R
import com.example.eworkout.databinding.FragmentWorkoutStart2Binding
import com.example.eworkout.training.viewmodel.SharedViewModel
import com.example.eworkout.training.viewmodel.Workout1ViewModel

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
    private lateinit var timer : Chronometer

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
        setAnimation()
        setListener()
        startCountUp()
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
        binding.btnPrevious.setOnClickListener {
            _sharedViewModel.decreaseCurrentExerciseIndex()
            findNavController().navigate(R.id.action_fragmentWorkoutStart2_to_fragmentWorkoutRest)
        }
        binding.btnMiddle.setOnClickListener {
            onClick()
        }
        binding.btnNext.setOnClickListener {
            onClick()
        }
        binding.exerciseInformationBtn.setOnClickListener {
            val bundle = Bundle().apply {
                putString("exercise_id", _sharedViewModel.getCurrentExercise().id)
            }
            findNavController().navigate(R.id.action_fragmentWorkoutStart2_to_workoutDetail2, bundle)
        }
        binding.backgroundAnimationView.setFailureListener {
            binding.backgroundImageview.visibility = View.VISIBLE
            binding.backgroundAnimationView.visibility = View.GONE
        }
    }

    private fun onClick()
    {
        if(_sharedViewModel.increaseCurrentExerciseIndex()){
            _sharedViewModel.calculateKcal((SystemClock.elapsedRealtime() - timer.base) / 1000)
            findNavController().navigate(R.id.action_fragmentWorkoutStart2_to_fragmentWorkoutRest)
        }
        else{
            val bundle = Bundle()
            bundle.putString("set_taken_id", _sharedViewModel.setTakenID)
            _sharedViewModel.calculateAndUpdate((SystemClock.elapsedRealtime() - timer.base) / 1000)
            findNavController().navigate(R.id.action_fragmentWorkoutStart2_to_fragmentWorkoutDone, bundle)
        }
    }
}