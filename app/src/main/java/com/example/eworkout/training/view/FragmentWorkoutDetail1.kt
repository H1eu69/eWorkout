package com.example.eworkout.training.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.eworkout.R
import com.example.eworkout.databinding.FragmentWorkoutDetail1Binding
import com.example.eworkout.training.adapter.ExercisesAdapter
import com.example.eworkout.training.listener.ExercisesOnClickListener
import com.example.eworkout.training.viewmodel.Workout1ViewModel
import com.example.eworkout.training.model.WorkoutDetail1State
import com.example.eworkout.training.viewmodel.SharedViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentWorkoutDetail1.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentWorkoutDetail1 : Fragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var setId:String? = null
    private var _binding: FragmentWorkoutDetail1Binding? = null
    val binding get() = _binding!!
    private val _viewModel: Workout1ViewModel by viewModels()
    private val _sharedViewModel: SharedViewModel by navGraphViewModels(R.id.training_nav)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mParam1 = it.getString(ARG_PARAM1)
            mParam2 = it.getString(ARG_PARAM2)
            setId = it.getString("set_id")
            Log.d("test set id", setId.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("test view state", " create view")
        _binding = FragmentWorkoutDetail1Binding.inflate(inflater, container, false)
        binding.viewModel = _viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WorkoutDetail1.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): FragmentWorkoutDetail1 {
            val fragment = FragmentWorkoutDetail1()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        observeViewModel()
        setupRecyclerView()
    }

    private fun setOnClickListener()
    {
        binding.btnStart.setOnClickListener {
            _sharedViewModel.addNewSetTaken(setId!!)
            findNavController().navigate(R.id.action_workoutDetail1_to_fragmentWorkoutReady)
        }
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeViewModel()
    {
        _viewModel.state.observe(viewLifecycleOwner){
            handleState(it)
        }
    }

    private fun handleState(state: WorkoutDetail1State)
    {
        when(state.name){
            "LOADING" -> {
                showLoading()
                _viewModel.getSetsFieldsById((setId!!))
                _sharedViewModel.getSetsFieldsById((setId!!))
            }
            "LOADED" -> {
                Log.d("hahohi", " LOADED")
                hideLoading()
                setupRecyclerView()
            }
            "IMAGE_LOADED" -> {
                Log.d("hahohi", " IMAGE_LOADED")
                notifyDataChange()
            }
        }
    }
    private fun showLoading()
    {
        //binding.shimmerLayout.visibility = View.VISIBLE
        //binding.dataLayout.visibility = View.GONE
        binding.hideShimmerLayout = false
        binding.hideDataLayout = true
    }
    private fun hideLoading()
    {
        //binding.shimmerLayout.visibility = View.GONE
        //binding.dataLayout.visibility = View.VISIBLE
        binding.hideShimmerLayout = true
        binding.hideDataLayout = false
    }

    private fun setupRecyclerView()
    {
        val listener = ExercisesOnClickListener {
            findNavController().navigate(R.id.action_workoutDetail1_to_workoutDetail2, it)
        }
        val list = _viewModel.exercises
        binding.recyclerView.adapter = ExercisesAdapter(list, listener)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun notifyDataChange()
    {
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()
        Log.d("test view state", " start")

    }
    override fun onResume() {
        super.onResume()
        Log.d("test view state", " resume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("test view state", "pause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("test view state", " on stop")

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("test view state", " saveinstance view")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d("test view state", " view state resotre view")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("test view state", " destroy view")
    }
}