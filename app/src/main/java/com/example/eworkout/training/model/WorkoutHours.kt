package com.example.eworkout.training.model

import java.time.LocalDate
import java.util.Date

data class WorkoutHours(
    val date: LocalDate,
    val workoutHours : Long
)