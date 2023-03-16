package com.example.eworkout.signup.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.eworkout.signup.model.SignupState
import com.example.eworkout.signup.viewmodel.SignupViewModel
import com.example.fithome.R
import com.example.fithome.databinding.FragmentSignupBinding
import kotlinx.coroutines.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentSignupBinding? = null
    val binding get() = _binding!!
    lateinit var _viewModel: SignupViewModel

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
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        val viewModel: SignupViewModel by viewModels()
        _viewModel = viewModel
        binding.viewModel = viewModel
        binding.lifecycleOwner = this@SignupFragment
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerViewModel()
        setOnClickListener()
    }

    private fun setOnClickListener()
    {
        binding.btnLogin.setOnClickListener {
            if(_viewModel.validateText())
            {
                lifecycleScope.launch{
                    val signUpSuccess = async(Dispatchers.IO) {
                        _viewModel.createUserWithEmailAndPassword()
                    }
                    if(signUpSuccess.await())
                        findNavController().navigate(R.id.action_signupFragment_to_letsStartFragment)
                    else
                        Log.d(TAG,"signup failed")
                }
            }
            else
                Log.d(TAG,"invalid input")
        }
    }

    private fun observerViewModel()
    {
        _viewModel._signupState.observe(viewLifecycleOwner)
        {
            handleState(it)
        }
    }

    private fun handleState(state: SignupState) {
        when(state.name)
        {
            "ERROR_FIRST_NAME_EMPTY" -> {
                binding.textFieldFirstName.error = "First name can not be empty!"
                binding.textFieldFirstName.isErrorEnabled = true
            }
            "ERROR_FIRST_NAME_LENGTH" -> {
                binding.textFieldFirstName.error = "First name must greater than 3 characters!"
                binding.textFieldFirstName.isErrorEnabled = true
            }
            "ERROR_LAST_NAME_EMPTY" -> {
                binding.textFieldLastName.error = "Last name can not be empty!"
                binding.textFieldLastName.isErrorEnabled = true
            }
            "ERROR_LAST_NAME_LENGTH" -> {
                binding.textFieldLastName.error = "Last name must greater than 3 characters!"
                binding.textFieldLastName.isErrorEnabled = true
            }
            "ERROR_EMAIL_EMPTY" -> {
                binding.textFieldEmail.error = "Email can not be empty!"
                binding.textFieldEmail.isErrorEnabled = true
            }
            "ERROR_EMAIL_FORMAT" -> {
                binding.textFieldEmail.error = "Invalid email format!"
                binding.textFieldEmail.isErrorEnabled = true
            }
            "ERROR_USED_EMAIL" -> {
                binding.textFieldEmail.error = "Email is used by another account!"
                binding.textFieldEmail.isErrorEnabled = true
            }
            "ERROR_PASSWORD_EMPTY" -> {
                binding.textFieldPassword.error = "Password can not be empty!"
                binding.textFieldPassword.isErrorEnabled = true
            }
            "ERROR_PASSWORD_LENGTH" -> {
                binding.textFieldPassword.error = "Password must greater than 8 characters!"
                binding.textFieldPassword.isErrorEnabled = true
            }
            "ERROR_CONFIRM_PASSWORD_MATCH" -> {
                binding.textFieldConfirmPassword.error = "Confirm password does not match!"
                binding.textFieldConfirmPassword.isErrorEnabled = true
            }
            //No above error
            "NO_ERROR_FIRST_NAME" -> binding.textFieldFirstName.isErrorEnabled = false
            "NO_ERROR_LAST_NAME" -> binding.textFieldLastName.isErrorEnabled = false
            "NO_ERROR_EMAIL" -> binding.textFieldEmail.isErrorEnabled = false
            "NO_ERROR_PASSWORD" -> binding.textFieldPassword.isErrorEnabled = false
            "NO_ERROR_CONFIRM_PASSWORD" -> binding.textFieldConfirmPassword.isErrorEnabled = false
        }
    }

    private fun showErrors(errors: MutableMap<String, String>)
    {
        if(errors.isEmpty())
        {
            if(!_viewModel.errorFirstName.hasError)
                binding.textFieldFirstName.isErrorEnabled = false
            if(!_viewModel.errorLastName.hasError)
                binding.textFieldLastName.isErrorEnabled = false
            if(!_viewModel.errorEmail.hasError)
                binding.textFieldEmail.isErrorEnabled = false
            if(!_viewModel.errorPassword.hasError)
                binding.textFieldPassword.isErrorEnabled = false
            if(!_viewModel.errorConfirmPassword.hasError)
                binding.textFieldConfirmPassword.isErrorEnabled = false
        }
        else
            errors.forEach()
            {
                when(it.key)
                {
                    "password_empty_error"-> {
                            binding.textFieldPassword.error = it.value
                            binding.textFieldPassword.isErrorEnabled = true
                    }
                    "password_length_error"-> {
                        binding.textFieldPassword.error = it.value
                        binding.textFieldPassword.isErrorEnabled = true
                    }
                    "confirm_password_empty_error"-> {
                        binding.textFieldConfirmPassword.error = it.value
                        binding.textFieldConfirmPassword.isErrorEnabled = true
                    }
                    "confirm_password_match_error"-> {
                        binding.textFieldConfirmPassword.error = it.value
                        binding.textFieldConfirmPassword.isErrorEnabled = true
                    }
                }
            }
    }
}