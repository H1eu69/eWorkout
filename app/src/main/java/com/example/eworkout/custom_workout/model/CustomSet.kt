package com.example.eworkout.custom_workout.model

data class CustomSet(
    var id: String,
    var name: String,
    val numOfExercises: Int,
    var image: String,
    var createdDate: Long
)
