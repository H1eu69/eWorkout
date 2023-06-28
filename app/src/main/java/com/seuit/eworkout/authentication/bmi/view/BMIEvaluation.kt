package com.seuit.eworkout.authentication.bmi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.seuit.eworkout.R
import com.seuit.eworkout.authentication.bmi.model.BMIState
import com.seuit.eworkout.authentication.bmi.viewmodel.BMIViewModel
import com.seuit.eworkout.authentication.signup.viewmodel.SignupViewModel
import com.seuit.eworkout.databinding.FragmentBMIEvaluationBinding
import com.seuit.eworkout.databinding.FragmentSignupBinding

/**
 * A simple [Fragment] subclass.
 * Use the [BMIEvaluation.newInstance] factory method to
 * create an instance of this fragment.
 */
class BMIEvaluation : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentBMIEvaluationBinding? = null
    val binding get() = _binding!!
    private val _viewModel: BMIViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBMIEvaluationBinding.inflate(inflater, container, false)
        binding.viewModel = _viewModel
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
         * @return A new instance of fragment BMIEvaluation.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BMIEvaluation().apply {
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setListener()
    }

    private fun observeViewModel()
    {
        _viewModel.state.observe(viewLifecycleOwner)
        {
            handleState(it)
        }
    }

    private fun handleState(bmiState: BMIState) {
        when(bmiState.name)
        {
            "SIGNIN_SUCCESS" -> {
                findNavController().navigate(R.id.action_BMIEvaluation_to_trainingFragment)
            }
            "MISSING_AGE" -> {
                binding.editTextAge.error = "Please input this"
                binding.editTextAge.isErrorEnabled = true
            }
            "MISSING_WEIGHT" -> {
                binding.editTextWeight.error = "Please input this"
                binding.editTextWeight.isErrorEnabled = true
            }
            "MISSING_HEIGHT" -> {
                binding.editTextHeight.error = "Please input this"
                binding.editTextHeight.isErrorEnabled = true
            }
            "NO_AGE_ERROR" -> {
                binding.editTextAge.isErrorEnabled = false
            }
            "NO_WEIGHT_ERROR" -> {
                binding.editTextWeight.isErrorEnabled = false
            }
            "NO_HEIGHT_ERROR" -> {
                binding.editTextHeight.isErrorEnabled = false
            }
        }
    }

    private fun setListener()
    {
        binding.textViewSkip.setOnClickListener {
            _viewModel.skipWithDefaultValue()
        }
        binding.btnNext.setOnClickListener {
            if(_viewModel.validateText())
                _viewModel.signInIfNotSignInPrevious()
        }
    }
}