package com.example.eworkout.custom_workout.listener

import android.os.Bundle

interface PickExercisesOnClickListener {
    fun navigateToDetail(bundle: Bundle)

    fun addToCart(bundle: Bundle)
}