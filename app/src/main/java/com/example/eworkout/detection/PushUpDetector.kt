package com.example.eworkout.detection

data class PushUpDetector(
    var startAngle: Double,
    var ascendedAngle: Double,
    var endAngle: Double
)
{
    fun isOnePushUp() : Boolean = (startAngle in 170.0..180.0 && ascendedAngle in 40.0..110.0 && endAngle in 160.0..180.0)
}
