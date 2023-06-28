package com.seuit.eworkout.custom_workout.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.seuit.eworkout.custom_workout.viewModel.PickExercisesSharedViewModel
import com.seuit.eworkout.databinding.PickExercisesFilterBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PickExercisesFilterModalBottomSheet : BottomSheetDialogFragment() {

    private var _binding: PickExercisesFilterBottomSheetBinding? = null
    private val binding: PickExercisesFilterBottomSheetBinding get() = _binding!!
    private val filterTypes = mutableListOf<String>()
    private val viewModel: PickExercisesSharedViewModel by viewModels(
        {requireParentFragment()}
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PickExercisesFilterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClick()
        Log.d("bottomsheet", viewModel.toString())
    }

    private fun setOnClick()
    {
        binding.btnApplyFilter.setOnClickListener {
            getFilterTypes()
            viewModel.filter(filterTypes)
            dismiss()
        }
        binding.textViewReset.setOnClickListener {
            clearAllToogleButton()
        }
    }

    private fun clearAllToogleButton() {
        binding.toggleButtonLevel.clearChecked()
        binding.toggleButtonMuscleGroup.clearChecked()
        binding.toggleButtonMuscleGroup2.clearChecked()
    }

    private fun getFilterTypes()
    {
        filterTypes.clear()
        for(id in binding.toggleButtonMuscleGroup.checkedButtonIds){
            when(id)
            {
                binding.btnFullbody.id -> {
                    filterTypes.add("Full")
                }
                binding.btnABS.id -> {
                    filterTypes.add("ABS")
                }
            }
        }

        for(id in binding.toggleButtonMuscleGroup2.checkedButtonIds){
            when(id)
            {
                binding.btnUpper.id -> {
                    filterTypes.add("Upper")
                }
                binding.btnLower.id -> {
                    filterTypes.add("Lower")
                }
            }
        }

        for(id in binding.toggleButtonLevel.checkedButtonIds){
            when(id)
            {
                binding.btnEasy.id -> {
                    filterTypes.add("Easy")
                }
                binding.btnIntermediate.id -> {
                    filterTypes.add("Intermediate")
                }
                binding.btnAdvanced.id -> {
                    filterTypes.add("Advanced")
                }
            }
        }
    }
}