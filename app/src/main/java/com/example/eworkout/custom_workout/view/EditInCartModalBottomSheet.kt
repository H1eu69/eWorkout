package com.example.eworkout.custom_workout.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import com.example.eworkout.R
import com.example.eworkout.custom_workout.model.AddToCartState
import com.example.eworkout.custom_workout.viewModel.CartViewModel
import com.example.eworkout.custom_workout.viewModel.EditInCartViewModel
import com.example.eworkout.custom_workout.viewModel.PickExercisesSharedViewModel
import com.example.eworkout.databinding.FragmentEditInCartModalBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditInCartModalBottomSheet.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditInCartModalBottomSheet(
    val bundle: Bundle
    ) : BottomSheetDialogFragment() {
    private var _binding: FragmentEditInCartModalBottomSheetBinding? = null
    private val binding: FragmentEditInCartModalBottomSheetBinding get() = _binding!!
    private val parentViewModel: CartViewModel by viewModels({requireParentFragment()})
    private val viewModel: EditInCartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditInCartModalBottomSheetBinding.inflate(inflater, container, false)
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
                viewModel.changeRepType()
                binding.toggleButton.check(binding.btnRep.id)
            }
            "TIME_CHOOSE" -> {
                binding.timeRepCountTextView.text = viewModel.exerciseInCart.value?.reps.toString() + "s"
                viewModel.changeRepType()
                binding.toggleButton.check(binding.btnTime.id)
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
        binding.btnEdit.setOnClickListener {
            parentViewModel.editExerciseInCart(viewModel.exerciseInCart.value)
            dismiss()
        }
    }

}