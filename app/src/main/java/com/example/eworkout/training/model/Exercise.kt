package com.example.eworkout.training.model

data class Exercise(
    val id: String,
    val name: String,
    var image: String,
    val reps: String,
    val description: String,
    val calo: String,
    val instruction: String,
    val animation_url: String,
    val MET: Double
)