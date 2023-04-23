package com.example.eworkout.authentication.signup.model

data class SignupModel(
    val firstName :String?,
    val lastName :String?,
    val email :String?,
    val password :String?,
    val confirmPassword :String?
)
