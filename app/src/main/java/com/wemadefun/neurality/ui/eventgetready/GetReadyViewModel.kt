package com.wemadefun.neurality.ui.eventgetready

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GetReadyViewModel : ViewModel() {

    companion object {
        // Countdown time interval
        private const val ONE_SECOND = 1000L

        // Total time for the game
        private const val COUNTDOWN_TIME = 3000L
    }

    private val timer: CountDownTimer

    private val _currentTime = MutableLiveData<Int>()
    val currentTime: LiveData<Int>
        get() = _currentTime

    private val _eventStartGame = MutableLiveData<Boolean>()
    val eventStartGame: LiveData<Boolean>
        get() = _eventStartGame

    init {
        _eventStartGame.value = false
        _currentTime.value = 3

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished / ONE_SECOND).toInt() + 1
            }

            override fun onFinish() {
                _currentTime.value = 1
                _eventStartGame.value = true
            }
        }
        timer.start()
    }

    fun doneNavigating() {
        _eventStartGame.value = false
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}
