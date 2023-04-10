package com.example.eworkout.training.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.eworkout.R
import com.example.eworkout.databinding.FragmentLoginBinding
import com.example.eworkout.databinding.FragmentTrainingBinding
import com.example.eworkout.login.viewmodel.LoginViewModel
import com.example.eworkout.training.viewmodel.TrainingViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass.
 * Use the [TrainingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TrainingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentTrainingBinding? = null
    val binding get() = _binding!!
    private lateinit var _viewModel: TrainingViewModel

    val totalTime = ""
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
        // Inflate the layout for this fragment
        _binding = FragmentTrainingBinding.inflate(inflater,container,false)
        val viewModel: TrainingViewModel by viewModels()
        _viewModel = viewModel
        binding.viewModel = viewModel
        binding.lifecycleOwner = this@TrainingFragment
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TrainingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TrainingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.buttonViewMoreFullbody.setOnClickListener {
            val name = binding.textViewFullBody.text.toString()
            _viewModel.getSetId(name)
        }

        binding.buttonViewLowerBody.setOnClickListener{
            val name = binding.textViewLowerBody.text.toString()
            _viewModel.getSetId(name)
        }

        binding.buttonViewUpperBody.setOnClickListener{
            val name = binding.textViewUpperBody.text.toString()
            _viewModel.getSetId(name)
        }

        binding.buttonViewMoreAB.setOnClickListener{
            val name = binding.textViewAB.text.toString()
            _viewModel.getSetId(name)
        }
    }
}


