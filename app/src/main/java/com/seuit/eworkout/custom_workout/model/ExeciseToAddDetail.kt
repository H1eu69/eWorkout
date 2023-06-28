package com.seuit.eworkout.custom_workout.model

data class ExerciseToAddDetail(
    var id: String,
    var name: String,
    var image: String,
    var reps: Int,
    var repType: String,
    var video: String,
    var description: String,
    var level: String
)
