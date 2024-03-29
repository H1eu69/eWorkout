package com.seuit.eworkout.authentication.letsstart

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.seuit.eworkout.MainActivity
import com.seuit.eworkout.authentication.login.viewmodel.LoginViewModel
import com.seuit.eworkout.R
import com.seuit.eworkout.databinding.FragmentLetsStartBinding
import com.seuit.eworkout.databinding.FragmentLoginBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LetsStartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LetsStartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentLetsStartBinding? = null
    val binding get() = _binding!!
    private val viewModel: LetsStartViewModel by viewModels()
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
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLetsStartBinding.inflate(inflater,container,false)
        return binding.root
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LetsStartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LetsStartFragment().apply {
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

    override fun onResume() {
        super.onResume()
        hideBottomNav()
    }

    private fun hideBottomNav() {
        (requireActivity() as MainActivity).bottomNavigation.visibility = View.GONE
    }

    private fun setOnClickListener()
    {
        binding.btnLetsStart.setOnClickListener {
            if(viewModel.hasSignInPreviously()){
                findNavController().navigate(R.id.action_letsStartFragment_to_Training)
            }
            else
                findNavController().navigate(R.id.action_letsStartFragment_to_loginFragment)
        }

    }
}