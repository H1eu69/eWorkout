package com.example.eworkout.training.model

import android.os.CountDownTimer
import com.example.eworkout.training.listener.SonicCountDownTimerListener

abstract class MyCountDown(
    val millisTime: Long,
    val interval: Long) : CountDownTimer(millisTime, interval)
{
    var millisRemainingTime = 0L

}