package com.seuit.eworkout.report.util

class MathRounder {
    companion object{
        fun round(number: Double) : Double
        {
            return Math.round(number * 100) / 100.0
        }
    }
}