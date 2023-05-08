package com.example.eworkout.custom_workout.util

import com.example.eworkout.custom_workout.model.ExerciseInCart
import com.example.eworkout.custom_workout.model.ExerciseToAdd

object ConvertTypeUtil {
    fun convert(list: List<ExerciseToAdd>?): MutableList<ExerciseInCart>
    {
        val newList = mutableListOf<ExerciseInCart>()
        list?.forEach {
            newList.add(
                ExerciseInCart(
                    it.id,
                    it.name,
                    it.image,
                    it.reps,
                    it.repType
                )
            )
        }
        return newList
    }
}