package com.example.eworkout.training.util

import kotlin.math.atan2

class MathUlti {
    data class Point(val x: Float, val y: Float)
    companion object{
        fun calculateAngle(point1: Point, point2: Point, point3: Point): Double {
            val dx1 = point1.x - point2.x
            val dy1 = point1.y - point2.y
            val dx2 = point3.x - point2.x
            val dy2 = point3.y - point2.y

            return Math.toDegrees((atan2(dy2, dx2) - atan2(dy1, dx1)).toDouble())
        }
    }
}