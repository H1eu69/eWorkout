package com.example.eworkout.training.model

data class Set(
    val setId: String ?= null,
    val setName: String?= null,
    val totalTime: String?= null,
    val totalCalories: String?= null,
    val totalExercises: Int?= null
)
