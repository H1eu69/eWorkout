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
import com.example.eworkout.custom_workout.model.AddToCartState
import com.example.eworkout.custom_workout.model.PickExercise
import com.example.eworkout.custom_workout.viewModel.AddToCartViewModel
import com.example.eworkout.custom_workout.viewModel.PickExercisesViewModel
import com.example.eworkout.databinding.PickExercisesAddToCartBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddToCartModalBottomSheet(
    val bundle: Bundle
) : BottomSheetDialogFragment() {

    private var _binding: PickExercisesAddToCartBottomSheetBinding? = null
    private val binding: PickExercisesAddToCartBottomSheetBinding get() = _binding!!
    private val shareViewModel: PickExercisesViewModel by viewModels(
        {requireParentFragment()}
    )
    private val viewModel: AddToCartViewModel by viewModels()
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
        checkRepsButton()
    }

    private fun checkRepsButton()
    {
        binding.toggleButton.check(binding.btnRep.id)
    }

    private fun observeViewModel()
    {
        viewModel.state.observe(viewLifecycleOwner)
        {
            handleState(it)
        }
    }

    private fun handleState(state: AddToCartState) {
        when(state.name)
        {
            "REPS_CHOOSE" -> {
                binding.timeRepCountTextView.text = "x" + viewModel.exerciseInCart.value?.reps.toString()
            }
            "TIME_CHOOSE" -> {
                binding.timeRepCountTextView.text = viewModel.exerciseInCart.value?.reps.toString() + "s"
            }
        }
    }

    private fun getBundle()
    {
        Log.d("Test bundle", bundle.toString())
        viewModel.initExerciseInCart(bundle)
    }

    private fun setListener()
    {
        binding.btnRep.setOnClickListener {
            viewModel.changeState(AddToCartState.REPS_CHOOSE)
        }
        binding.btnTime.setOnClickListener {
            viewModel.changeState(AddToCartState.TIME_CHOOSE)
        }
        binding.btnMinus.setOnClickListener {
            viewModel.decreaseRepByOne()
        }
        binding.btnAdd.setOnClickListener {
            viewModel.increaseRepByOne()
        }
    }

}