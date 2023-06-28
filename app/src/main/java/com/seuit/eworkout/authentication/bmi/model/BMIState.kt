package com.seuit.eworkout.authentication.bmi.model

enum class BMIState {
    SIGNIN_SUCCESS,
    MISSING_AGE,
    MISSING_WEIGHT,
    MISSING_HEIGHT,
    NO_AGE_ERROR,
    NO_WEIGHT_ERROR,
    NO_HEIGHT_ERROR
}