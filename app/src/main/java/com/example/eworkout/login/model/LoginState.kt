package com.example.eworkout.login.model

enum class LoginState {
    SUCCESS,
    FAILED,
    ERROR_EMAIL_EMPTY,
    ERROR_EMAIL_FORMAT,
    ERROR_PASSWORD_EMPTY,
    ERROR_PASSWORD_LENGTH,
    ERROR_WRONG_PASSWORD,
    NO_ERROR_EMAIL,
    NO_ERROR_PASSWORD
}