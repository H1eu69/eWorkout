package com.example.eworkout.signup.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eworkout.signup.model.EnumError
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

    val _errorMessages: MutableLiveData<MutableMap<String, String>> = MutableLiveData()

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
            Log.d(TAG, ex.message.toString())
            false
        }
    }

    fun validateText()
    {
        validateFirstName()
        validateLastName()
        validateEmail()
        validatePassword()
        validateConfirmPassword()
    }

    fun validateFirstName() {
        val errorMessages = mutableMapOf<String, String>()
        errorFirstName.hasError = false

        if(firstName.value.isNullOrEmpty())
        {
            errorMessages["first_name_empty_error"] = "First name can not be empty!"
            errorFirstName.hasError = true
        }
        else if(firstName.value.toString().length <= 3)
        {
            errorMessages["first_name_length_error"] = "First name must greater than 3 characters!"
            errorFirstName.hasError = true
        }
        _errorMessages.value = errorMessages
    }

    fun validateLastName() {
        val errorMessages = mutableMapOf<String, String>()
        errorLastName.hasError = false

        if(lastName.value.isNullOrEmpty())
        {
            errorMessages["last_name_empty_error"] = "Last name can not be empty!"
            errorLastName.hasError = true
        }
        else if(lastName.value.toString().length <= 3)
        {
            errorMessages["last_name_length_error"] = "Last name must greater than 3 characters!"
            errorLastName.hasError = true
        }
        _errorMessages.value = errorMessages
    }

    fun validateEmail() {
        val errorMessages = mutableMapOf<String, String>()
        errorEmail.hasError = false

        if(email.value.isNullOrEmpty())
        {
            errorMessages["email_empty_error"] = "Email can not be empty!"
            errorEmail.hasError = true
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches())
        {
            errorMessages["email_format_error"] = "Invalid email format!"
            errorEmail.hasError = true
        }
        _errorMessages.value = errorMessages

    }
    fun validatePassword() {
        val errorMessages = mutableMapOf<String, String>()
        errorPassword.hasError = false

        if(password.value.isNullOrEmpty())
        {
            errorMessages["password_empty_error"] = "Password can not be empty!"
            errorPassword.hasError = true
        }
        else if(password.value.toString().length < 8)
        {
            errorMessages["password_length_error"] = "Password must greater than 8 characters!"
            errorPassword.hasError = true
        }
        _errorMessages.value = errorMessages
    }

    fun validateConfirmPassword() {
        val errorMessages = mutableMapOf<String, String>()
        errorConfirmPassword.hasError = false

        if(confirmPassword.value.toString() != password.value.toString())
        {
            errorMessages["confirm_password_match_error"] = "Confirm password does not match!"
            errorConfirmPassword.hasError = true
        }
        _errorMessages.value = errorMessages
    }

    fun isValidInput(): Boolean
    {
        if(errorFirstName.hasError ||
            errorLastName.hasError ||
            errorEmail.hasError ||
            errorPassword.hasError ||
            errorConfirmPassword.hasError)
            return false
        return true
    }
}