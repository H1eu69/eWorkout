package com.example.eworkout.custom_workout.model

data class ExerciseToAdd(
    var id: String,
    var name: String,
    var image: String,
    var reps: Int,
    var repType: String
)
