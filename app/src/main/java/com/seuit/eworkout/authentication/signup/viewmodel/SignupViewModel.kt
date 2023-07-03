package com.seuit.eworkout.authentication.signup.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seuit.eworkout.authentication.signup.model.SignupState
import com.google.firebase.Timestamp
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class SignupViewModel : ViewModel() {
    val firstName: MutableLiveData<String> = MutableLiveData()

    val lastName: MutableLiveData<String> = MutableLiveData()

    val email: MutableLiveData<String> = MutableLiveData()

    val password: MutableLiveData<String> = MutableLiveData()

    val confirmPassword: MutableLiveData<String> = MutableLiveData()

    private val _signupState: MutableLiveData<SignupState> = MutableLiveData()
    val signupState: LiveData<SignupState> get() = _signupState

    private val auth: FirebaseAuth = Firebase.auth

    private val firestore = Firebase.firestore
    suspend fun createUserWithEmailAndPassword() {
        try {
            if(auth.createUserWithEmailAndPassword(email.value.toString(), password.value.toString())
                .await().user != null) {
                _signupState.postValue(SignupState.SUCCESS)
                createInFireStore()
            }
        } catch (ex: Exception) {
            Log.d(TAG, ex.toString())
            _signupState.postValue(SignupState.ERROR_USED_EMAIL)
        }
    }

    private fun createInFireStore()
    {
        val data = mutableMapOf(
            "email" to auth.currentUser?.email,
            "is_guest" to false,
            "first_name" to "",
            "last_name" to "",
            "age" to 18,
            "current_weight" to 60.0,
            "current_height" to 165.0,
        )
        auth.currentUser?.let {
            firestore.collection("Users").document(it.uid).set(data)
        }
    }

    private fun createAdditionalInfo()
    {
        val _BMI = (60.0/((165.0/100)*(165.0/100)))
        val data2 = hashMapOf(
            "user_id" to auth.currentUser!!.uid,
            "height" to 165.0,
            "weight" to 60.0,
            "time" to Timestamp.now(),
            "bmi" to _BMI
        )
        firestore.collection("Height_and_Weight").add(data2)
    }
    suspend fun signInWithCredential(credential: AuthCredential)
    {
        try{
            val authResult = auth.signInWithCredential(credential).await()
            if(authResult.user != null)
            {
                createInFireStore()
                if(authResult.additionalUserInfo!!.isNewUser)
                    createAdditionalInfo()
                _signupState.postValue(SignupState.GOOGLE_SIGN_IN_SUCCESS)
            }
        }
        catch (ex: Exception) {
            Log.d(TAG, ex.toString())
        }
    }

    fun validateText(): Boolean
    {
        if(validateFirstName() &&
            validateLastName() &&
            validateEmail() &&
            validatePassword() &&
            validateConfirmPassword()
        ) {
            return true
        }
        return false
    }

    fun validateFirstName(): Boolean {
        if(firstName.value.isNullOrEmpty())
        {
            _signupState.value = SignupState.ERROR_FIRST_NAME_EMPTY
            return false
        }
        else if(firstName.value.toString().length < 3)
        {
            _signupState.value = SignupState.ERROR_FIRST_NAME_LENGTH
            return false
        }
        _signupState.value = SignupState.NO_ERROR_FIRST_NAME
        return true
    }

    fun validateLastName(): Boolean {
        if(lastName.value.isNullOrEmpty())
        {
            _signupState.value = SignupState.ERROR_LAST_NAME_EMPTY
            return false
        }
        else if(lastName.value.toString().length < 3)
        {
            _signupState.value = SignupState.ERROR_LAST_NAME_LENGTH
            return false
        }
        _signupState.value = SignupState.NO_ERROR_LAST_NAME
        return true
    }

    fun validateEmail(): Boolean {
        if(email.value.isNullOrEmpty())
        {
            _signupState.value = SignupState.ERROR_EMAIL_EMPTY
            return false
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches())
        {
            _signupState.value = SignupState.ERROR_EMAIL_FORMAT
            return false
        }
        _signupState.value = SignupState.NO_ERROR_EMAIL
        return true
    }
    fun validatePassword(): Boolean {
        if(password.value.isNullOrEmpty())
        {
            _signupState.value = SignupState.ERROR_PASSWORD_EMPTY
            return false
        }
        else if(password.value.toString().length < 8)
        {
            _signupState.value = SignupState.ERROR_PASSWORD_LENGTH
            return false
        }
        _signupState.value = SignupState.NO_ERROR_PASSWORD
        return true
    }

    fun validateConfirmPassword(): Boolean {
        if(confirmPassword.value.toString() != password.value.toString())
        {
            _signupState.value = SignupState.ERROR_CONFIRM_PASSWORD_MATCH
            return false
        }
        _signupState.value = SignupState.NO_ERROR_CONFIRM_PASSWORD
        return true
    }

}