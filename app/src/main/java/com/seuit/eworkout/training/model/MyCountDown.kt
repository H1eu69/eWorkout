package com.seuit.eworkout.training.model

import android.os.CountDownTimer
import com.seuit.eworkout.training.listener.SonicCountDownTimerListener

abstract class MyCountDown(
    val millisTime: Long,
    val interval: Long) : CountDownTimer(millisTime, interval)
{
    var millisRemainingTime = 0L

}