package com.example.eworkout.signup.model

enum class EnumError(var hasError: Boolean) {
    FIRST_NAME(false),
    LAST_NAME(false),
    EMAIL(false),
    PASSWORD(false),
    CONFIRM_PASSWORD(false)
}