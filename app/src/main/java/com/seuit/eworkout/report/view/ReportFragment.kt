package com.seuit.eworkout.report.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.seuit.eworkout.R
import com.seuit.eworkout.databinding.FragmentReportBinding
import com.seuit.eworkout.report.`interface`.UpdateBMIDialogOnClick
import com.seuit.eworkout.report.model.ReportState
import com.seuit.eworkout.report.util.MathRounder
import com.seuit.eworkout.report.viewmodel.ReportViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineDataSet.Mode.CUBIC_BEZIER


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReportFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReportFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentReportBinding? = null
    val binding get() = _binding!!
    private val _viewModel: ReportViewModel by viewModels()

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
        _binding = FragmentReportBinding.inflate(inflater,container,false)
        binding.viewModel = _viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        onClick()
        onDestroyView()
    }

    private fun onClick() {
        binding.openDialog.setOnClickListener{
            showDialog()
        }
    }

    private fun showDialog() {

        val listener = object : UpdateBMIDialogOnClick
        {
            override fun onClick(weight: Double, height: Double) {
                _viewModel.updateWeightHeight(weight.toFloat(), height.toFloat())
                _viewModel.changeStateTo(ReportState.CHART_LOADING)
            }

        }
        val dialog = UpdateBMIDialog(listener, _viewModel.currentWeight.value!!, _viewModel.currentHeight.value!!)
        dialog.show(parentFragmentManager, "Report Fragment")
    }

    private fun observeViewModel()
    {
        _viewModel.state.observe(viewLifecycleOwner){
            handleState(it)
        }
    }

    private fun handleState(state: ReportState) {
        when (state.name) {
            "LOADING" -> {
                _viewModel.getReportData()
                showLoading()
            }
            "LOADED" -> {
                hideLoading()
                setupChart()
                BMIProgress()
            }
            "CHART_LOADING" -> {
                setupChart()
            }
        }
    }

    private fun hideLoading() {
        binding.reportDataLayout.visibility = View.VISIBLE
        binding.lottieLoading.visibility = View.GONE
    }

    private fun showLoading() {
        binding.reportDataLayout.visibility = View.GONE
        binding.lottieLoading.visibility = View.VISIBLE
    }

    private fun setupChart(){

        val lineEntry = ArrayList<Entry>()
        var xValue = 1f
        for(data in _viewModel.pointList) {
            data?.let {
                lineEntry.add(Entry(xValue, it.toFloat()))
                xValue += 1
            }
        }
        val lineDataSet = LineDataSet(lineEntry,"")

        val data = LineData(lineDataSet)

        binding.lineChart.data = data

        // Style Line
        lineDataSet.color = Color.rgb(62,29,255)
        lineDataSet.lineWidth = 2f
        lineDataSet.mode = CUBIC_BEZIER
        lineDataSet.valueTextSize = 12f
        lineDataSet.valueTextColor = Color.rgb(62,29,255)
        lineDataSet.circleRadius = 5f
        lineDataSet.circleHoleRadius = 3f
        lineDataSet.setDrawHighlightIndicators(false)
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawValues(false)


        lineDataSet.setDrawFilled(true)
        lineDataSet.fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.chart_gradient)

        binding.lineChart.legend.direction = Legend.LegendDirection.RIGHT_TO_LEFT
        binding.lineChart.legend.yEntrySpace = 10f
        binding.lineChart.legend.isWordWrapEnabled = true

        //Style Chart
        /*val legend = LegendEntry()
        legend.label = "Weight"
        legend.formSize = 20f
        legend.formLineWidth = 2f
        legend.formColor = Color.rgb(62,29,255)

        val legendList = mutableListOf(legend)

        binding.lineChart.legend.setCustom(legendList)
        binding.lineChart.legend.isEnabled = true*/

        binding.lineChart.legend.isEnabled = false
        binding.lineChart.description = null
        binding.lineChart.setDrawGridBackground(false)
        binding.lineChart.setBackgroundColor(Color.WHITE)

        //binding.lineChart.axisLeft.axisMaximum = _viewModel.heviest_weight.toFloat() + 5f
        binding.lineChart.axisLeft.axisMinimum = _viewModel.lightestWeight.value!!.toFloat() - 5f
        binding.lineChart.axisRight.isEnabled = false

        binding.lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.lineChart.xAxis.axisMinimum = 0f
        binding.lineChart.xAxis.axisMaximum = 12f //_viewModel.weight_data_list.count().toFloat() - 1f
        binding.lineChart.xAxis.granularity = 1f
        binding.lineChart.xAxis.setDrawAxisLine(false)
        binding.lineChart.xAxis.setDrawGridLines(false)
        binding.lineChart.marker = CustomMarkerView(requireContext(), R.layout.marker_layout)

        binding.lineChart.animateY(1000)
    }

    fun BMIProgress(){
        binding.seekBar.isEnabled = false
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReportFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReportFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewModel.changeStateTo(ReportState.LOADING)
    }
}