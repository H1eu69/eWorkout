package com.example.eworkout.custom_workout.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.eworkout.custom_workout.model.PickExercise
import com.example.eworkout.custom_workout.viewModel.PickExercisesViewModel
import com.example.eworkout.databinding.PickExercisesAddToCartBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddToCartModalBottomSheet(
    val bundle: Bundle
) : BottomSheetDialogFragment() {

    private var _binding: PickExercisesAddToCartBottomSheetBinding? = null
    private val binding: PickExercisesAddToCartBottomSheetBinding get() = _binding!!
    private var exerciseName: String? = null
    private var image: String? = null
    private val viewModel: PickExercisesViewModel by viewModels(
        {requireParentFragment()}
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PickExercisesAddToCartBottomSheetBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundle()
        observeViewModel()
        setListener()
    }

    private fun observeViewModel()
    {

    }

    private fun getBundle()
    {
        Log.d("Test bundle", bundle.toString())
        viewModel.getExerciseInCart(bundle)
    }

    private fun setListener()
    {

    }

}