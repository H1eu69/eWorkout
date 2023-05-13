package com.example.eworkout.custom_workout.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.createGraph
import androidx.navigation.fragment.findNavController
import com.example.eworkout.MainActivity
import com.example.eworkout.R
import com.example.eworkout.custom_workout.adapter.CustomSetAdapter
import com.example.eworkout.custom_workout.adapter.PickExercisesAdapter
import com.example.eworkout.custom_workout.listener.CustomSetOnClickListener
import com.example.eworkout.custom_workout.listener.PickExercisesOnClickListener
import com.example.eworkout.custom_workout.model.CustomSetState
import com.example.eworkout.custom_workout.viewModel.CustomSetViewModel
import com.example.eworkout.databinding.FragmentCustomSetBinding

/**
 * A simple [Fragment] subclass.
 * Use the [CustomSetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomSetFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding : FragmentCustomSetBinding? = null
    val binding : FragmentCustomSetBinding get() = _binding!!
    private val viewModel: CustomSetViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomSetBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CustomSetFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CustomSetFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setOnClick()
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner)
        {
            handleState(it)
        }
    }

    private fun handleState(it: CustomSetState) {
        when(it.name)
        {
            "LOADING" -> {
                showLoading()
                viewModel.getCustomSet()
            }
            "LOADED" -> {
                showData()
            }
        }
    }

    private fun showLoading() {
        binding.lottie.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.createSetBtn.visibility = View.GONE
    }

    private fun hideLoading()
    {
        binding.lottie.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.createSetBtn.visibility = View.VISIBLE
    }

    private fun showData() {
        hideLoading()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val listener = object : CustomSetOnClickListener
        {
            override fun onClick(bundle: Bundle) {
                val graph = findNavController().navInflater.inflate(R.navigation.training_nav)
                graph.setStartDestination(R.id.workoutDetail1)
                findNavController().setGraph(graph, bundle)
            }

        }
        binding.recyclerView.adapter = CustomSetAdapter(viewModel.sets.value!!, listener)

    }

    private fun setOnClick() {
        binding.createSetBtn.setOnClickListener {
            findNavController().navigate(R.id.action_customSetFragment_to_customCreateSetChooseName)
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNav()
    }

    private fun showBottomNav() {
        (requireActivity() as MainActivity).bottomNavigation.visibility = View.VISIBLE
    }
}