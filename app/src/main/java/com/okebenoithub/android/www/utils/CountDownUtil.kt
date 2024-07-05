package com.okebenoithub.android.www.utils

import android.os.CountDownTimer
import java.util.Locale

class CountDownUtil {
    private lateinit var countDownTimer: CountDownTimer
    private var timeLeftInMillis: Long = 60000 // 1 minute

    /**
     * Start the countdown timer
     */
    fun startCountdown(countDownListener: CountDownListener) {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                countDownListener.onCountDownTick(updateCountdownText())
            }

            override fun onFinish() {
                // implement an interface here
                countDownListener.onCountDownFinished()
            }
        }.start()
    }

    /**
     * Cancel the countdown timer
     */
    fun cancelCountdown() {
        countDownTimer.cancel()
        timeLeftInMillis = 60000 // 1 minute
    }


    /**
     * Update the countdown text view with the remaining time in the format "HH:MM:SS"
     * This method is called every second to update the countdown text view
     */
    private fun updateCountdownText(): String {
        val hours = (timeLeftInMillis / (1000 * 60 * 60)).toInt()
        val minutes = ((timeLeftInMillis / (1000 * 60)) % 60).toInt()
        val seconds = ((timeLeftInMillis / 1000) % 60).toInt()
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }
}

// This interface class is used to communicate with the countdown timer
interface CountDownListener {
    fun onCountDownFinished()
    fun onCountDownTick(timeLeft: String)
}