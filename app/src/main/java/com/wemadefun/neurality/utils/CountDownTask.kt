package com.wemadefun.neurality.utils

import android.os.CountDownTimer

interface OnCountdownTick {
    fun onTick(timeLeft: Long)
}

interface OnCountdownFinish {
    fun onFinish()
}

class CountDownTask(private var countdownInSecond: Float,
                    private var countDownInterval: Long)
{
    private var countdownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 0L

    private lateinit var callOnTick: (timeLeft: Long) -> Unit
    private lateinit var callOnFinish: () -> Unit

    private fun setupCountdownTimer(millisInFuture: Long) {
        countdownTimer = object : CountDownTimer(millisInFuture, countDownInterval) {
            override fun onTick(timeLeft: Long) {
                timeLeftInMillis = timeLeft
                callOnTick(timeLeft)
            }
            override fun onFinish() {
                timeLeftInMillis = 0L
                callOnFinish()
            }
        }
    }

    private fun setOnTick(onTickListener: (Long) -> Unit) { callOnTick = onTickListener }
    private fun setOnFinish(onFinishListener: () -> Unit) { callOnFinish = onFinishListener }

    private fun startNew() {
        timeLeftInMillis = (countdownInSecond * 1000).toLong()
        setupCountdownTimer(timeLeftInMillis)
        countdownTimer?.start()
    }

    fun clear() {
        countdownTimer?.cancel()
        countdownTimer = null
    }

    fun start(onTickListener: OnCountdownTick, onFinishListener: OnCountdownFinish) {
        clear()
        setOnTick { onTickListener.onTick(it) }
        setOnFinish { onFinishListener.onFinish() }
        startNew()
    }

    fun pause() {
        clear()
    }

    fun resume() {
        if (timeLeftInMillis > 0f) {
            clear()
            setupCountdownTimer(timeLeftInMillis)
            countdownTimer?.start()
        }
    }
}