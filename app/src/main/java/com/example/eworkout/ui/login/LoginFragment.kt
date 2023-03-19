package com.example.eworkout.ui.login

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fithome.R
import android.view.OnReceiveContentListener
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.eworkout.ui.login.ViewModel.LoginViewModel
import com.example.fithome.databinding.FragmentLoginBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentLoginBinding? = null
    val binding get() = _binding!!
    private lateinit var _viewModel: LoginViewModel

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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val viewModel: LoginViewModel by viewModels()
        _viewModel = viewModel
        //binding.viewModel = viewModel
        //binding.lifecycleOwner = this@LoginFragment
        return binding.root
    }

    //private lateinit var user: FirebaseAuth
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        /*val email = view.findViewById<TextInputEditText>(R.id.email)
        val password = view.findViewById<TextInputEditText>(R.id.password)
        val txtviewForgotPassword = view.findViewById<TextView>(R.id.textForgetPassword)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val txtCreateAccount = view.findViewById<TextView>(R.id.textViewCreateAccount)

        user = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener(){
            if (email.text.toString().isNotEmpty() && password.text.toString().isNotEmpty())
            {
                user.signInWithEmailAndPassword(email.text.toString(),password.text.toString())
                    .addOnCompleteListener{mTask ->
                        if(mTask.isSuccessful)
                        {
                            findNavController().navigate(R.id.action_loginFragment_to_trainingFragment)
                        }

                    }

            }
        }
        txtviewForgotPassword.setOnClickListener(){
            findNavController().navigate(R.id.action_loginFragment_to_forgotFragment)
        }
        txtCreateAccount.setOnClickListener(){
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }*/

    }
    private fun setOnClickListener()
    {
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_trainingFragment)
            /*lifecycleScope.launch{
                val loginSuccess = async(Dispatchers.IO) {
                    _viewModel.signInWithEmailAndPassword()
                }
                if(loginSuccess.await())

                else
                    Log.d(TAG,"signin failed")
            }*/
        }
        binding.textForgetPassword.setOnClickListener(){
            findNavController().navigate(R.id.action_loginFragment_to_forgotFragment)
        }
        binding.textViewCreateAccount.setOnClickListener(){
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)

                }
            }
    }
}

