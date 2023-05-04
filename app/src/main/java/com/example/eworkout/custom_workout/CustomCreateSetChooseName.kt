package com.example.eworkout.custom_workout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eworkout.R
import com.example.eworkout.custom_workout.model.ChooseNameState
import com.example.eworkout.custom_workout.viewModel.CustomCreateSetChooseNameViewModel
import com.example.eworkout.databinding.FragmentCustomCreateSetChooseNameBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CustomCreateSetChooseName.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomCreateSetChooseName : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentCustomCreateSetChooseNameBinding? = null
    private val binding: FragmentCustomCreateSetChooseNameBinding get() = _binding!!
    private val viewModel: CustomCreateSetChooseNameViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomCreateSetChooseNameBinding.inflate(inflater, container, false)
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
         * @return A new instance of fragment CustomCreateSetChooseName.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CustomCreateSetChooseName().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setOnClick()
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner)
        {
            handleState(it)
        }
    }

    private fun handleState(state: ChooseNameState) {
        when(state.name)
        {
            "INVALID" -> {
                binding.textInputLayout.error = "Set name can not be empty!"
                binding.textInputLayout.isErrorEnabled = true
            }
            "VALID" -> {
                binding.textInputLayout.isErrorEnabled = false
            }
        }
    }

    private fun setOnClick() {
        binding.buttonNavigate.setOnClickListener {
            if(viewModel.validateText()){
                val bundle = Bundle()
                bundle.putString("set_name", viewModel.setName.value)
                findNavController().navigate(R.id.action_customCreateSetChooseName_to_customCreateSetFragment, bundle)
            }
        }
    }
}