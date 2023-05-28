package com.example.eworkout.report.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.PaintDrawable
import android.graphics.drawable.ShapeDrawable.ShaderFactory
import android.graphics.drawable.shapes.RectShape
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.eworkout.R
import com.example.eworkout.databinding.FragmentReportBinding
import com.example.eworkout.report.model.ReportState
import com.example.eworkout.report.viewmodel.ReportViewModel
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
    private lateinit var _viewModel: ReportViewModel

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
        val viewModel: ReportViewModel by viewModels()
        _viewModel = viewModel
        binding.viewModel = viewModel
        binding.lifecycleOwner = this@ReportFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        onClick()
    }

    private fun onClick() {
        binding.openDialog.setOnClickListener{
            showdialog()
        }
    }

    private fun showdialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.update_bmi_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(1000,1000)


        val btnSave : TextView = dialog.findViewById(R.id.SAVE)
        val btnCancel : TextView = dialog.findViewById(R.id.CANCEL)

        btnSave.setOnClickListener {
            if(dialog.findViewById<EditText>(R.id.editTextWEIGHT).text.isNotEmpty() && dialog.findViewById<EditText>(R.id.editTextHEIGHT).text.isNotEmpty())
            {
                val current_weight : Float = dialog.findViewById<EditText>(R.id.editTextWEIGHT).text.toString().toFloat()
                val current_height : Float = dialog.findViewById<EditText>(R.id.editTextHEIGHT).text.toString().toFloat()

                _viewModel.update_weight_height(current_weight,current_height)
                _viewModel.change_state()
                dialog.dismiss()
            }
        }

        btnCancel.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
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
                _viewModel.watching()
                _viewModel.get_weight_data()
                showLoading()
            }

            "CHART_LOADING" -> {
                _viewModel.get_weight_data()
            }

            "LOADED" -> {
                binding.TotalKcalReport.text = _viewModel.num.toString()
                binding.TotalTimeReport.text = _viewModel.min.toString()
                binding.TotalExercisesReport.text = _viewModel.exercises.toString()
                binding.txtCurrentWeight.text = _viewModel.current_weight.toString() + " kg"
                binding.txtHeaviestWeight.text = _viewModel.heviest_weight.toString() + " kg"
                binding.txtLightestWeight.text = _viewModel.lightest_weight.toString() + " kg"
                setLineChartData()
                BMIProgress()
                hideLoading()
            }
        }
    }

    private fun hideLoading() {
        binding.reportDataLayout.visibility = View.VISIBLE
    }

    private fun showLoading() {
        binding.reportDataLayout.visibility = View.INVISIBLE
    }

    fun setLineChartData(){

        val lineEntry = ArrayList<Entry>()
        var xValue = 0f
        for(data in _viewModel.point_list) {
            lineEntry.add(Entry(xValue,data.toFloat()))
            xValue += 1
        }


        val lineDataSet = LineDataSet(lineEntry,"")

        val data = LineData(lineDataSet)

        binding.lineChart.data = data

        // Style Line
        lineDataSet.setColor(R.color.fill1)
        lineDataSet.lineWidth = 4f
        lineDataSet.mode = CUBIC_BEZIER
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawValues(false)

        lineDataSet.setDrawFilled(true)
        lineDataSet.fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.chart_gradient)

        //Style Chart
        binding.lineChart.legend.isEnabled = false
        binding.lineChart.description = null
        binding.lineChart.setDrawGridBackground(false)
        binding.lineChart.setBackgroundColor(resources.getColor(R.color.white))

        binding.lineChart.axisLeft.axisMaximum = _viewModel.heviest_weight.toFloat() + 5f
        binding.lineChart.axisLeft.axisMinimum = _viewModel.lightest_weight.toFloat() - 5f
        binding.lineChart.axisRight.isEnabled = false

        binding.lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.lineChart.xAxis.axisMinimum = 1f
        binding.lineChart.xAxis.axisMaximum = 12f //_viewModel.weight_data_list.count().toFloat() - 1f
        binding.lineChart.xAxis.granularity = 1f
        binding.lineChart.xAxis.setDrawAxisLine(false)
        binding.lineChart.xAxis.setDrawGridLines(false)

        binding.lineChart.animateY(1000)
    }

    fun BMIProgress(){
        binding.seekBar.max = 40
        binding.seekBar.min = 15
        binding.seekBar.progress = _viewModel.current_bmi.toInt()
        binding.seekBar.isEnabled = false
        binding.currentBMI.text = _viewModel.current_bmi.toString()
        if(_viewModel.current_bmi <= 18.5)
            binding.BMIText.text = "Under Weight"
        if(_viewModel.current_bmi <= 25 && _viewModel.current_bmi > 18.5)
            binding.BMIText.text = "Normal"
        if(_viewModel.current_bmi <= 30 && _viewModel.current_bmi > 25)
            binding.BMIText.text = "Over Weight"
        if(_viewModel.current_bmi > 30)
            binding.BMIText.text = "Obesity"
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
}