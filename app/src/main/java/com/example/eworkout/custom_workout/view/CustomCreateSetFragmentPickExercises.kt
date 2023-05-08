package com.example.eworkout.custom_workout.view

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eworkout.R
import com.example.eworkout.custom_workout.adapter.PickExercisesAdapter
import com.example.eworkout.custom_workout.listener.PickExercisesOnClickListener
import com.example.eworkout.custom_workout.model.PickExercisesState
import com.example.eworkout.custom_workout.viewModel.PickExercisesSharedViewModel
import com.example.eworkout.databinding.FragmentCustomCreateSetPickExercisesBinding

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
    private val viewModel : PickExercisesSharedViewModel by viewModels()
    private var filterBottomSheet: PickExercisesFilterModalBottomSheet? = null
    private var addBottomSheet: AddToCartModalBottomSheet? = null

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
    private fun showLoading()
    {
        //binding.shimmerLayout.visibility = View.VISIBLE
        //binding.dataLayout.visibility = View.GONE
        binding.hideShimmer = false
    }
    private fun hideLoading()
    {
        //binding.shimmerLayout.visibility = View.GONE
        //binding.dataLayout.visibility = View.VISIBLE
        binding.hideShimmer = true
    }
    private fun setListener() {
        binding.filterBtn.setOnClickListener {
            if(filterBottomSheet == null){
                filterBottomSheet = PickExercisesFilterModalBottomSheet()
            }
            filterBottomSheet?.show(childFragmentManager, PickExercisesFilterModalBottomSheet.TAG)
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

        binding.btnViewCart.setOnClickListener {
            findNavController().navigate(R.id.action_customCreateSetFragmentPickExercises_to_customCreateSetCartFragment)
        }
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
                showLoading()
                viewModel.getExercise()
            }
            "LOADED" -> {
                hideLoading()
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
            override fun navigateToDetail(bundle: Bundle) {
                findNavController().navigate(R.id.action_customCreateSetFragmentPickExercises_to_customCreateSetDetailFragment, bundle)
            }

            override fun showAddToCartBottomSheet(bundle: Bundle) {
                openAddToCartBottomSheet(bundle)
            }
        }
        viewModel.list.value?.let {
            binding.recyclerView2.adapter = PickExercisesAdapter(it, listener)
        }

    }

    fun openAddToCartBottomSheet(bundle: Bundle)
    {
        addBottomSheet = AddToCartModalBottomSheet(bundle)
        addBottomSheet?.show(childFragmentManager, AddToCartModalBottomSheet.TAG)
    }
}