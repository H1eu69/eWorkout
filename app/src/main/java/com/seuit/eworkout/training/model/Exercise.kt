package com.seuit.eworkout.training.model

data class Exercise(
    val id: String,
    val name: String,
    var image: String,
    var reps: String,
    val description: String,
    val calo: String,
    val instruction: String,
    val animation_url: String,
    val MET: Double
)