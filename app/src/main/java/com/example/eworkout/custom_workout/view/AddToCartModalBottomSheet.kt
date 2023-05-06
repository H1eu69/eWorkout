package com.example.eworkout.custom_workout.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.eworkout.custom_workout.viewModel.PickExercisesViewModel
import com.example.eworkout.databinding.PickExercisesBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddToCartModalBottomSheet  : BottomSheetDialogFragment() {

    private var _binding: PickExercisesBottomSheetBinding? = null
    private val binding: PickExercisesBottomSheetBinding get() = _binding!!
    private val viewModel: PickExercisesViewModel by viewModels(
        {requireParentFragment()}
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PickExercisesBottomSheetBinding.inflate(inflater, container, false)
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

    }

}