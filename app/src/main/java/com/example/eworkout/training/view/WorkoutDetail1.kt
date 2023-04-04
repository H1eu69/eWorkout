package com.example.eworkout.training.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.eworkout.R
import com.example.eworkout.databinding.FragmentWorkoutDetail1Binding
import com.example.eworkout.training.adapter.ExercisesAdapter
import com.example.eworkout.training.listener.ExercisesOnClickListener
import com.example.eworkout.training.viewmodel.Workout1ViewModel
import com.example.eworkout.training.model.WorkoutDetail1State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [WorkoutDetail1.newInstance] factory method to
 * create an instance of this fragment.
 */
class WorkoutDetail1 : Fragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var _binding: FragmentWorkoutDetail1Binding? = null
    val binding get() = _binding!!
    private lateinit var _viewModel: Workout1ViewModel
    private lateinit var exercisesOnClickListener : ExercisesOnClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mParam1 = it.getString(ARG_PARAM1)
            mParam2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutDetail1Binding.inflate(inflater, container, false)
        val viewModel: Workout1ViewModel by viewModels()
        _viewModel = viewModel
        binding.viewModel = _viewModel
        binding.lifecycleOwner = this
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
        fun newInstance(param1: String?, param2: String?): WorkoutDetail1 {
            val fragment = WorkoutDetail1()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setupRecyclerView()
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
                _viewModel.getDocumentFields("1iXUMoTZF1MxrQ9ResPr")
            }
            "LOADED" -> {
                showUI()
                notifyDataChanged()}
        }
    }

    private fun showUI()
    {
        binding.shimmerLayout.visibility = View.GONE
        binding.dataLayout.visibility = View.VISIBLE
    }

    private fun notifyDataChanged()
    {
        val list = _viewModel.exercises
        binding.recyclerView.adapter?.notifyItemInserted(list.size - 1)
    }

    private fun setupRecyclerView()
    {
        val listener = ExercisesOnClickListener {
            findNavController().navigate(R.id.action_workoutDetail1_to_workoutDetail2, it)
        }
        val list = _viewModel.exercises
        binding.recyclerView.adapter = ExercisesAdapter(list, listener)
    }
}