package com.seuit.eworkout.custom_workout.model

import com.seuit.eworkout.training.model.Set

data class CustomSet(
    var id: String,
    var name: String,
    val numOfExercises: Int,
    var image: String,
    var createdDate: Long
)
{
    fun toSet() = Set(id, name, 0, 0, numOfExercises.toLong(), image)
}
