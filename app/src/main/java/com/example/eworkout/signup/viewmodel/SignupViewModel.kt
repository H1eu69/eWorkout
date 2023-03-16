package com.example.eworkout.signup.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eworkout.signup.model.EnumError
import com.example.eworkout.signup.model.SignupState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class SignupViewModel : ViewModel() {
    private val _firstName: MutableLiveData<String> = MutableLiveData()
    val firstName get() = _firstName

    private val _lastName: MutableLiveData<String> = MutableLiveData()
    val lastName get() = _lastName

    private val _email: MutableLiveData<String> = MutableLiveData()
    val email get() = _email

    private val _password: MutableLiveData<String> = MutableLiveData()
    val password get() = _password

    private val _confirmPassword: MutableLiveData<String> = MutableLiveData()
    val confirmPassword get() = _confirmPassword

    val _signupState: MutableLiveData<SignupState> = MutableLiveData()

    var errorFirstName = EnumError.FIRST_NAME
    var errorLastName = EnumError.LAST_NAME
    var errorEmail = EnumError.EMAIL
    var errorPassword = EnumError.PASSWORD
    var errorConfirmPassword = EnumError.CONFIRM_PASSWORD

    private val auth: FirebaseAuth = Firebase.auth

    suspend fun createUserWithEmailAndPassword(): Boolean {
        return try {
            auth.createUserWithEmailAndPassword(email.value.toString(), password.value.toString())
                .await().user != null
        } catch (ex: Exception) {
            _signupState.postValue(SignupState.ERROR_USED_EMAIL)
            false
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
        else if(firstName.value.toString().length <= 3)
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
        else if(lastName.value.toString().length <= 3)
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