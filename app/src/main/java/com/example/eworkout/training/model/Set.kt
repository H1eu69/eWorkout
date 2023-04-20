package com.example.eworkout.training.model

data class Set(
    val setId: String,
    val setName: String,
    val totalTime: Long,
    val totalCalories: Long,
    val totalExercises: Long,
    var setImage: String
)
