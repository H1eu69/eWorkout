package com.seuit.eworkout.custom_workout.model

data class ExerciseToAdd(
    var id: String,
    var name: String,
    var image: String,
    var reps: Int,
    var repType: String
){
    constructor(exercise: ExerciseToAddDetail) : this(exercise.id, exercise.name, exercise.image, exercise.reps, exercise.repType)
}
