package com.example.eworkout.training.view

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.eworkout.R
import com.example.eworkout.databinding.FragmentWorkoutDoneBinding
import com.example.eworkout.training.model.UpdateState
import com.example.eworkout.training.model.WorkoutDoneState
import com.example.eworkout.training.viewmodel.SharedViewModel
import com.example.eworkout.training.viewmodel.WorkoutDoneViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentWorkoutDone.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentWorkoutDone : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentWorkoutDoneBinding? = null
    val binding get() = _binding!!
    private val _viewModel: WorkoutDoneViewModel by viewModels()
    private val _sharedViewModel: SharedViewModel by navGraphViewModels(R.id.training_nav)

    private var setTakenID: String? = null
    private lateinit var mediaPlayer : MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            setTakenID = it.getString("set_taken_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutDoneBinding.inflate(inflater, container, false)
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
         * @return A new instance of fragment FragmentWorkoutDone.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentWorkoutDone().apply {
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
        observeViewModel()
    }

    private fun startSoundEffect()
    {
        mediaPlayer = MediaPlayer.create(context, R.raw.congrats_sound)
        mediaPlayer.start() // no need to call prepare(); create() does that for you
    }

    private fun setListener()
    {
        binding.backBtn.setOnClickListener {
            onClick()
        }
        binding.btnNext.setOnClickListener {
            _viewModel.addToCalendar(setTakenID.toString())
            onClick()
        }
        binding.textViewNext.setOnClickListener {
            onClick()
        }
        binding.textViewDoItAgain.setOnClickListener {
            doExerciseAgain()
        }
    }

    private fun onClick()
    {
        _sharedViewModel.resetIndexAndCalories()
        val bundle = Bundle()
        bundle.putString("setTakenId",setTakenID)
        findNavController().navigate(R.id.action_fragmentWorkoutDone_to_trainingFragment, bundle)
    }

    private fun doExerciseAgain()
    {
        val bundle = Bundle()
        bundle.putString("set_id", _viewModel.currentSetId)
        _sharedViewModel.resetIndexAndCalories()
        findNavController().navigate(R.id.action_fragmentWorkoutDone_to_workoutDetail1, bundle)
    }

    private fun observeViewModel()
    {
        /*_viewModel.state.observe(viewLifecycleOwner){
            handleState(it)
        }*/
        _sharedViewModel.updateState.observe(viewLifecycleOwner)
        {
            handleUpdateState(it)
        }
    }

    private fun handleUpdateState(updateState: UpdateState) {
        when(updateState.name){
            "RUNNING" -> {
                showLoading()
                _viewModel.getModelData(setTakenID!!)
            }
            "DONE" -> {
                hideLoading()
            }
        }
    }

    private fun handleState(state: WorkoutDoneState)
    {
        when(state.name){
            "LOADING" -> {
                showLoading()
                _viewModel.getModelData(setTakenID!!)
            }
            "LOADED" -> {
                hideLoading()
            }
        }
    }

    private fun hideLoading() {
        binding.hideShimmerLayout = true
        binding.hideDataLayout = false
    }

    private fun showLoading() {
        binding.hideShimmerLayout = false
        binding.hideDataLayout = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _sharedViewModel.resetUpdateState()
        mediaPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Destroy", "on destroy")
    }
}