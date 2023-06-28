package com.seuit.eworkout.training.listener

interface SonicCountDownTimerListener {
    fun onTimerTick(timeRemaining: Long)

    /**
     * Method to be called by [SonicCountDownTimer] when the thread is getting  finished
     */
    fun onTimerFinish()
}