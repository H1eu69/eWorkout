package com.seuit.eworkout.training.util

import kotlin.math.atan2

class MathUlti {
    data class Point(val x: Float, val y: Float)
    companion object{
        fun calculateAngle(p1: Point, p2: Point, p3: Point): Double {
            val angle1 = atan2(p1.y - p2.y, p1.x - p2.x)
            val angle2 = atan2(p3.y - p2.y, p3.x - p2.x)
            var angle = (angle2 - angle1).toDouble()

            if (angle < 0) {
                angle += 2 * Math.PI
            }

            val positiveAngle = Math.toDegrees(angle)
            return if (positiveAngle < 0) positiveAngle + 360 else positiveAngle
        }
    }
}