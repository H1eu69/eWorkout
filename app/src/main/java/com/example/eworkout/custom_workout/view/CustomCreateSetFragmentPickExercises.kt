package com.example.eworkout.custom_workout.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eworkout.R
import com.example.eworkout.custom_workout.adapter.PickExercisesAdapter
import com.example.eworkout.custom_workout.listener.PickExercisesOnClickListener
import com.example.eworkout.custom_workout.model.PickExercisesState
import com.example.eworkout.custom_workout.viewModel.PickExercisesViewModel
import com.example.eworkout.databinding.FragmentCustomCreateSetPickExercisesBinding
import com.example.eworkout.generated.callback.AfterTextChanged

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
    private var modalBottomSheet: PickExercisesFilterModalBottomSheet? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
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

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("bottomsheet", viewModel.toString())
        observeViewModel()
        setListener()
        setupSearchView()
        setupRecyclerView()
    }

    private fun setListener() {
        binding.filterBtn.setOnClickListener {
            if(modalBottomSheet == null){
                modalBottomSheet = PickExercisesFilterModalBottomSheet()
            }
            modalBottomSheet?.show(childFragmentManager, PickExercisesFilterModalBottomSheet.TAG)
        }

        binding.searchView.editText.setOnEditorActionListener(object : TextView.OnEditorActionListener
        {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                Log.d("searchview filter",p0?.text.toString().lowercase())
                viewModel.filterByName(p0?.text.toString().lowercase())
                binding.searchBar.text = binding.searchView.text
                binding.searchView.hide()
                return false
            }

        })

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