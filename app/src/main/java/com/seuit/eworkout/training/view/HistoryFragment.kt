package com.seuit.eworkout.training.view

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seuit.eworkout.R
import com.seuit.eworkout.databinding.FragmentHistoryBinding
import com.seuit.eworkout.databinding.FragmentSetsItemBinding
import com.seuit.eworkout.databinding.FragmentTrainingBinding
import com.seuit.eworkout.training.adapter.HistoryAdapter
import com.seuit.eworkout.training.model.HistoryState
import com.seuit.eworkout.training.model.ScheduleState
import com.seuit.eworkout.training.model.Set
import com.seuit.eworkout.training.viewmodel.HistoryViewModel
import com.seuit.eworkout.training.viewmodel.TrainingViewModel
import org.checkerframework.checker.units.qual.Length

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentHistoryBinding? = null
    val binding get() = _binding!!
    private lateinit var _viewModel: HistoryViewModel

    private var date: String? = null
    private var date1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            date = it.getString("date")
            date1 = it.getString("date1")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHistoryBinding.inflate(inflater,container,false)
        val viewModel: HistoryViewModel by viewModels()
        _viewModel = viewModel
        binding.viewModel = viewModel
        binding.lifecycleOwner = this@HistoryFragment
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
        observeViewModel()
        setUpReyclerView()
    }

    private fun setUpReyclerView() {
        val list = _viewModel.sets
        binding.recyclerHistory.adapter = HistoryAdapter(list)
    }

    private fun observeViewModel()
    {
        _viewModel.state.observe(viewLifecycleOwner){
            handleState(it)
        }
    }

    private fun handleState(state: HistoryState)
    {
        when(state.name){
            "LOADING" -> {
                _viewModel.indicator(date.toString(), date1.toString())
                _viewModel.getSetTakenId(date.toString(), date1.toString())
                showLoading()
            }
            "LOADED" -> {
                binding.num.text = _viewModel.num.toString()
                binding.min.text = _viewModel.min.toString()
                binding.numOfExercisesHistory.text = _viewModel.exercises.toInt().toString()
                hideLoading()
            }
            "SET_LOADED" -> {
                setUpReyclerView()
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
        binding.historyLayout.visibility = View.GONE
    }
    private fun hideLoading()
    {
        binding.historyLayout.visibility = View.VISIBLE
    }
    private fun onClick() {
        binding.buttonBack.setOnClickListener {
            findNavController().navigate(R.id.action_historyFragment_to_dailyScheduleFragment)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun notifyDataChange()
    {
        binding.recyclerHistory.adapter?.notifyDataSetChanged()
    }
}