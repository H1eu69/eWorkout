package com.example.eworkout.training.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eworkout.MainActivity
import com.example.eworkout.R
import com.example.eworkout.databinding.FragmentDailyScheduleBinding
import com.example.eworkout.training.model.ScheduleState
import com.example.eworkout.training.viewmodel.DailyScheduleViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DailyScheduleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DailyScheduleFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentDailyScheduleBinding? = null
    val binding get() = _binding!!
    private lateinit var _viewModel: DailyScheduleViewModel

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
        _binding = FragmentDailyScheduleBinding.inflate(inflater,container,false)
        val viewModel: DailyScheduleViewModel by viewModels()
        _viewModel = viewModel
        binding.viewModel = viewModel
        binding.lifecycleOwner = this@DailyScheduleFragment
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DailyScheduleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DailyScheduleFragment().apply {
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
        pickDate()
    }
    override fun onResume() {
        super.onResume()
        hideBottomNav()
    }

    private fun hideBottomNav() {
        (requireActivity() as MainActivity).bottomNavigation.visibility = View.GONE
    }
    private fun observeViewModel()
    {
        _viewModel.state.observe(viewLifecycleOwner){
            handleState(it)
        }
    }

    private fun handleState(state: ScheduleState)
    {
        when(state.name){
            "LOADING" -> {
                _viewModel.watching()
            }
            "LOADED" -> {
            }
        }
    }

    private fun setOnClick() {
        binding.buttonBackToHome.setOnClickListener {
            findNavController().navigate(R.id.action_dailyScheduleFragment_to_trainingFragment)
        }
    }

    private fun pickDate(){
        binding.calendarView.setOnDateChangeListener{
            calendarView, i, i1, i2 ->
            val year = i
            val month = i1
            val day = i2
            val date = formatDateChange(i,i1,i2)
            val date1 = formatDateChange1(i,i1,i2)
            //_viewModel.date = formatDateChange(i,i1,i2)
            //_viewModel.indicator(_viewModel.date.toString())
            val bundle = Bundle().apply {
                putString("date", date)
                putString("date1", date1)
            }
            findNavController().navigate(R.id.action_dailyScheduleFragment_to_historyFragment, bundle)
        }
    }

    private fun formatDateChange1(i: Int, i1: Int, i2: Int): String? {
        val day: String = i2.toString()
        var month = ""
        val year: String = i.toString()
        when(i1+1){
            1 -> month = "Jan"
            2 -> month = "Feb"
            3 -> month = "Mar"
            4 -> month = "Apr"
            5 -> month = "May"
            6 -> month = "Jun"
            7 -> month = "Jul"
            8 -> month = "Aug"
            9 -> month = "Sep"
            10 -> month = "Oct"
            11 -> month = "Nov"
            12 -> month = "Dec"
        }
        return day + " " + month + " " + year
    }

    private fun formatDateChange(i: Int, i1: Int, i2: Int): String{
        val day: String = i2.toString()
        var month = ""
        val year: String = i.toString()
        when(i1+1){
            1 -> month = "Jan"
            2 -> month = "Feb"
            3 -> month = "Mar"
            4 -> month = "Apr"
            5 -> month = "May"
            6 -> month = "Jun"
            7 -> month = "Jul"
            8 -> month = "Aug"
            9 -> month = "Sep"
            10 -> month = "Oct"
            11 -> month = "Nov"
            12 -> month = "Dec"
        }
        return month + " " + day + ", " + year
    }

}