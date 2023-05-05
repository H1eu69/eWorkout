package com.example.eworkout.custom_workout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.eworkout.R
import com.example.eworkout.databinding.FragmentCustomSetBinding

/**
 * A simple [Fragment] subclass.
 * Use the [CustomSetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomSetFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding : FragmentCustomSetBinding? = null
    val binding : FragmentCustomSetBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomSetBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CustomSetFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CustomSetFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClick()
    }

    private fun setOnClick() {
        binding.createSetBtn.setOnClickListener {
            findNavController().navigate(R.id.action_customSetFragment_to_customCreateSetChooseName)
        }
    }
}