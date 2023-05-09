package com.example.eworkout.custom_workout.view

import android.content.DialogInterface
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
import com.example.eworkout.custom_workout.adapter.CartAdapter
import com.example.eworkout.custom_workout.listener.CartOnCLickListener
import com.example.eworkout.custom_workout.model.CartState
import com.example.eworkout.custom_workout.viewModel.CartViewModel
import com.example.eworkout.custom_workout.viewModel.PickExercisesSharedViewModel
import com.example.eworkout.databinding.FragmentCustomSetCartBinding

class CustomCreateSetCartFragment: Fragment() {
    private var _binding: FragmentCustomSetCartBinding? = null
    private val binding: FragmentCustomSetCartBinding get() = _binding!!
    private val _shareViewModel: PickExercisesSharedViewModel by navGraphViewModels(R.id.custom_set)
    private val viewModel : CartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomSetCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromShareVM()
        observeViewModel()
        setListener()
    }

    private fun setListener() {
        binding.btnPickExercise.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnNavigateUp.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnCreate.setOnClickListener {
            viewModel.addToDb(arguments)
        }
    }

    private fun getDataFromShareVM()
    {
        viewModel.getExerciseFromSharedVM(_shareViewModel.exercisesInCartLiveData.value)
        Log.d("test cart vm", _shareViewModel.exercisesInCartLiveData.value?.size.toString())
        Log.d("test cart vm", viewModel.exercisesInCartLiveData.value?.size.toString())
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner)
        {
            handleState(it)
        }
    }

    private fun handleState(state: CartState) {
        when(state.name)
        {
            "CHECKING_CART" -> {
                checkEmptyCart()
            }
            "EMPTY_CART" -> showEmptyUI()
            "NOT_EMPTY_CART" -> {
                showUI()
                setupRecyclerView()
            }
            "ELEMENT_UPDATED" -> {
                refreshUIUpdate()
            }
            "ELEMENT_DELETED" -> {
                refreshUIDelete()
            }
            "ADDED_NEW_SET" -> {
                findNavController().navigate(R.id.action_customCreateSetCartFragment_to_customSetChooseImageFragment)
            }
        }
    }

    private fun refreshUIDelete() {
        binding.recyclerView.adapter?.notifyItemRemoved(viewModel.itemChangePosition)
        _shareViewModel.updateExercisesInCart(viewModel.exercisesInCartLiveData.value!!)
        Log.d("test cart delete", viewModel.exercisesInCartLiveData.value?.size.toString())
    }

    private fun refreshUIUpdate() {
        binding.recyclerView.adapter?.notifyItemChanged(viewModel.itemChangePosition)
        _shareViewModel.updateExercisesInCart(viewModel.exercisesInCartLiveData.value!!)
        Log.d("test cart update", viewModel.exercisesInCartLiveData.value?.size.toString())
    }

    private fun showUI() {
        binding.recyclerView.visibility = View.VISIBLE
        binding.btnCreate.visibility = View.VISIBLE

        binding.lottieView.visibility = View.GONE
        binding.btnPickExercise.visibility = View.GONE
    }

    private fun setupRecyclerView()
    {
        val listener = object : CartOnCLickListener
        {
            override fun onEdit(bundle: Bundle) {
                val editModalBottomSheet = EditInCartModalBottomSheet(bundle)
                editModalBottomSheet.show(childFragmentManager, EditInCartModalBottomSheet.TAG)
            }

            override fun onDelete(bundle: Bundle) {
                viewModel.deleteExerciseInCart(bundle)
            }
        }
        binding.recyclerView.adapter = CartAdapter(
            viewModel.exercisesInCartLiveData.value!!,
            listener
        )
        Log.d("test cart init", viewModel.exercisesInCartLiveData.value?.size.toString())
    }

    private fun checkEmptyCart() {
        viewModel.isEmptyCart()
    }

    private fun showEmptyUI() {
        binding.recyclerView.visibility = View.GONE
        binding.btnCreate.visibility = View.GONE

        binding.lottieView.visibility = View.VISIBLE
        binding.btnPickExercise.visibility = View.VISIBLE
    }


}