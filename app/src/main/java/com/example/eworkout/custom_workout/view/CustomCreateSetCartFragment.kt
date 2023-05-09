package com.example.eworkout.custom_workout.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import com.example.eworkout.R
import com.example.eworkout.custom_workout.adapter.CartAdapter
import com.example.eworkout.custom_workout.listener.CartOnCLickListener
import com.example.eworkout.custom_workout.model.CartState
import com.example.eworkout.custom_workout.model.PickExercisesState
import com.example.eworkout.custom_workout.viewModel.CartViewModel
import com.example.eworkout.custom_workout.viewModel.PickExercisesSharedViewModel
import com.example.eworkout.custom_workout.viewModel.SetDetailViewModel
import com.example.eworkout.databinding.FragmentCustomCreateSetDetailBinding
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
            "NOT_EMPTY_CART" -> showUI()
            "ELEMENT_UPDATED" -> {
                refreshUI()
            }
        }
    }

    private fun refreshUI() {
        binding.recyclerView.adapter?.notifyItemChanged(viewModel.itemChangePosition)
    }

    private fun showUI() {
        val listener = object : CartOnCLickListener
        {
            override fun onEdit(bundle: Bundle) {
                val editModalBottomSheet = EditInCartModalBottomSheet(bundle)
                editModalBottomSheet.show(childFragmentManager, EditInCartModalBottomSheet.TAG)
            }

            override fun onDelete(bundle: Bundle) {

            }

        }
        binding.recyclerView.adapter = CartAdapter(
            viewModel.exercisesInCartLiveData.value!!,
            listener
        )
    }

    private fun checkEmptyCart() {
        viewModel.isEmptyCart()
    }

    private fun showEmptyUI() {
        TODO("Not yet implemented")
    }


}