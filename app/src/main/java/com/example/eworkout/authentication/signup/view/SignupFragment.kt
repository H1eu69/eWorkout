package com.example.eworkout.authentication.signup.view

import android.app.Activity
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.eworkout.authentication.signup.model.SignupState
import com.example.eworkout.authentication.signup.viewmodel.SignupViewModel
import com.example.eworkout.R
import com.example.eworkout.databinding.FragmentSignupBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
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
    private lateinit var _viewModel: SignupViewModel

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest


    val googleSignInRegister = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult())
    {
        when (it.resultCode) {
            Activity.RESULT_OK -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(it.data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                            lifecycleScope.launch(Dispatchers.IO) {
                                _viewModel.signInWithCredential(firebaseCredential)
                            }
                        }
                    }
                }catch(e: ApiException) {
                    Log.d(TAG, e.toString())
                }
            }
        }
    }


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
        binding.lifecycleOwner = viewLifecycleOwner
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
        setupGoogleSignIn()
    }

    private fun setOnClickListener()
    {
        binding.btnLogin.setOnClickListener {
            if(_viewModel.validateText())
            {
                lifecycleScope.launch(Dispatchers.IO){
                    _viewModel.createUserWithEmailAndPassword()
                }
            }
            else
                Log.d(TAG,"invalid input")
        }
        binding.btnGoogleSignIn.setOnClickListener{
            displayGoogleSignInUI()
        }

        binding.textViewLoginHere.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observerViewModel()
    {
        _viewModel.signupState.observe(viewLifecycleOwner)
        {
            handleState(it)
        }
    }

    private fun handleState(state: SignupState) {
        when(state.name)
        {
            "ERROR_FIRST_NAME_EMPTY" -> {
                binding.textFieldFirstName.error = getString(R.string.ERROR_FIRST_NAME_EMPTY)
                binding.textFieldFirstName.isErrorEnabled = true
            }
            "ERROR_FIRST_NAME_LENGTH" -> {
                binding.textFieldFirstName.error = getString(R.string.ERROR_FIRST_NAME_LENGTH)
                binding.textFieldFirstName.isErrorEnabled = true
            }
            "ERROR_LAST_NAME_EMPTY" -> {
                binding.textFieldLastName.error = getString(R.string.ERROR_LAST_NAME_EMPTY)
                binding.textFieldLastName.isErrorEnabled = true
            }
            "ERROR_LAST_NAME_LENGTH" -> {
                binding.textFieldLastName.error = getString(R.string.ERROR_LAST_NAME_LENGTH)
                binding.textFieldLastName.isErrorEnabled = true
            }
            "ERROR_EMAIL_EMPTY" -> {
                binding.textFieldEmail.error = getString(R.string.ERROR_EMAIL_EMPTY)
                binding.textFieldEmail.isErrorEnabled = true
            }
            "ERROR_EMAIL_FORMAT" -> {
                binding.textFieldEmail.error = getString(R.string.ERROR_EMAIL_FORMAT)
                binding.textFieldEmail.isErrorEnabled = true
            }
            "ERROR_USED_EMAIL" -> {
                binding.textFieldEmail.error = getString(R.string.ERROR_USED_EMAIL)
                binding.textFieldEmail.isErrorEnabled = true
            }
            "ERROR_PASSWORD_EMPTY" -> {
                binding.textFieldPassword.error = getString(R.string.ERROR_PASSWORD_EMPTY)
                binding.textFieldPassword.isErrorEnabled = true
            }
            "ERROR_PASSWORD_LENGTH" -> {
                binding.textFieldPassword.error = getString(R.string.ERROR_PASSWORD_LENGTH)
                binding.textFieldPassword.isErrorEnabled = true
            }
            "ERROR_CONFIRM_PASSWORD_MATCH" -> {
                binding.textFieldConfirmPassword.error = getString(R.string.ERROR_CONFIRM_PASSWORD_MATCH)
                binding.textFieldConfirmPassword.isErrorEnabled = true
            }
            //No above error
            "NO_ERROR_FIRST_NAME" -> binding.textFieldFirstName.isErrorEnabled = false
            "NO_ERROR_LAST_NAME" -> binding.textFieldLastName.isErrorEnabled = false
            "NO_ERROR_EMAIL" -> binding.textFieldEmail.isErrorEnabled = false
            "NO_ERROR_PASSWORD" -> binding.textFieldPassword.isErrorEnabled = false
            "NO_ERROR_CONFIRM_PASSWORD" -> binding.textFieldConfirmPassword.isErrorEnabled = false
            //Sign up Success
            "SUCCESS" -> {
                findNavController().navigate(R.id.action_signupFragment_to_BMIEvaluation)
            }
            "GOOGLE_SIGN_IN_SUCCESS" -> {
                findNavController().navigate(R.id.action_signupFragment_to_in_app_nav)
            }
        }
    }

    private fun setupGoogleSignIn()
    {
        oneTapClient = Identity.getSignInClient(requireActivity())
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()
    }

    private fun displayGoogleSignInUI()
    {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(requireActivity()) { result ->
                val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                googleSignInRegister.launch(intentSenderRequest)
            }
            .addOnFailureListener(requireActivity()) { e ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                e.localizedMessage?.let { Log.d(TAG, it) }
            }

    }

}