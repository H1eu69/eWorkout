package com.example.eworkout.training.view

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eworkout.R
import com.example.eworkout.databinding.FragmentTrainingBinding
import com.example.eworkout.training.adapter.SetsAdapter
import com.example.eworkout.training.listener.SetOnClickListener
import com.example.eworkout.training.model.TrainingState
import com.example.eworkout.training.viewmodel.TrainingViewModel

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

    private var setTakenID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            setTakenID = it.getString("setTakenId")
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewModel.getCurrentUserEmail()
        /*if (setTakenID.isNullOrEmpty()) {
            binding.textViewCaloriesNumber.text = "0.0"
            binding.textViewCalories.text = "0.0"
            binding.textViewHours.text = "0"
        }
        else{
            _viewModel.indicatorWatching()
        }*/
        observeViewModel()
        setupRecyclerView()
        setOnlistener()
    }

    private fun observeViewModel()
    {
        _viewModel.state.observe(viewLifecycleOwner){
            handleState(it)
        }
    }

    private fun handleState(state: TrainingState)
    {
        when(state.name){
            "LOADING" -> {
                _viewModel.loadSets()
                _viewModel.indicatorWatching()
                showLoading()
            }
            "LOADED" -> {
                setupRecyclerView()
                hideLoading()
            }
            "IMAGE_LOADED" -> {
                notifyDataChange()
                hideLoading()
            }
        }
    }

    private fun showLoading()
    {
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.dataLayout.visibility = View.GONE
    }
    private fun hideLoading()
    {
        binding.shimmerLayout.visibility = View.GONE
        binding.dataLayout.visibility = View.VISIBLE
    }

    private fun setupRecyclerView()
    {
        val listener = SetOnClickListener {
            findNavController().navigate(R.id.action_trainingFragment_to_workoutDetail1, it)
        }
        val list = _viewModel.sets
        binding.recyclerView.adapter = SetsAdapter(list, listener)
        binding.textView2.text = _viewModel.userEmail
        binding.textViewCalories.text = _viewModel.num.toString()
        binding.textViewCaloriesNumber.text = _viewModel.num.toString()
        binding.textViewHours.text = _viewModel.min.toString()
    }

    private fun setOnlistener(){
        binding.buttonCheck.setOnClickListener(){
            //findNavController().navigate(R.id.action_train)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun notifyDataChange()
    {
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}


