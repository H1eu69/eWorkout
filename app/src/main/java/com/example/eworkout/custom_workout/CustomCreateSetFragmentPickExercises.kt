package com.example.eworkout.custom_workout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eworkout.R
import com.example.eworkout.custom_workout.adapter.PickExercisesAdapter
import com.example.eworkout.custom_workout.listener.PickExercisesOnClickListener
import com.example.eworkout.custom_workout.model.ChooseNameState
import com.example.eworkout.custom_workout.model.PickExercisesState
import com.example.eworkout.custom_workout.viewModel.PickExercisesViewModel
import com.example.eworkout.databinding.FragmentCustomCreateSetPickExercisesBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CustomCreateSetFragmentPickExercises.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomCreateSetFragmentPickExercises : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentCustomCreateSetPickExercisesBinding? = null
    private val binding: FragmentCustomCreateSetPickExercisesBinding get() = _binding!!
    private val viewModel : PickExercisesViewModel by viewModels()
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
        _binding = FragmentCustomCreateSetPickExercisesBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
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
         * @return A new instance of fragment CustomCreateSetFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CustomCreateSetFragmentPickExercises().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setupSearchView()
        setupRecyclerView()
    }
    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner)
        {
            handleState(it)
        }
    }

    private fun handleState(state: PickExercisesState) {
        when(state.name)
        {
            "LOADING" -> {
                viewModel.getExercise()
            }
            "LOADED" -> {
                setupRecyclerView()
            }
        }
    }
    private fun setupSearchView()
    {
        binding.searchView.setupWithSearchBar(binding.searchBar)
    }

    private fun setupRecyclerView()
    {
        val listener = object : PickExercisesOnClickListener
        {
            override fun onClick(bundle: Bundle) {
                findNavController().navigate(R.id.action_customCreateSetFragmentPickExercises_to_customCreateSetDetailFragment, bundle)
            }
        }
        binding.recyclerView2.adapter = PickExercisesAdapter(viewModel.list.value!!, listener)
    }
}