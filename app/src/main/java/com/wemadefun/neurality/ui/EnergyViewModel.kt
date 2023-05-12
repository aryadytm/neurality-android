package com.wemadefun.neurality.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wemadefun.neurality.data.userdata.modules.energy.EnergyModule
import com.wemadefun.neurality.utils.CONFIG_ENERGY_MAX
import com.wemadefun.neurality.utils.isIapMode
import javax.inject.Inject

class EnergyViewModel @Inject constructor(private val energyModule: EnergyModule) : ViewModel() {

    private val _eventNavigateToGame = MutableLiveData<Boolean>()
    private val _eventLowEnergy = MutableLiveData<Boolean>()
    private val energyValue: Int get() = energyModule.energyValue

    val eventNavigateToGame: LiveData<Boolean> = _eventNavigateToGame
    val eventLowEnergy: LiveData<Boolean> = _eventLowEnergy
    val energyValueLiveData: LiveData<Int> = energyModule.energyLiveData
    val energyStringLiveData = Transformations.map(energyModule.energyLiveData) { "$it/$CONFIG_ENERGY_MAX" }
    val energyTimeLeftLiveData = Transformations.map(energyModule.energyTimeLeftLiveData) {
        if (it > 0L) { formatDuration(it) }
        else if (it == 1L) { "" }
        else { "READY TO GO" }
    }

    private var buttonClickable = true

    fun onNavigateToGame() {
        if (buttonClickable) {
            if (!isIapMode()) {
                onNotSubscribed()
            } else {
                onSubscribed()
            }
        }
    }

    fun onButtonClickable() {
        buttonClickable = true
    }

    fun onButtonNotClickable() {
        buttonClickable = false
    }

    fun onEventDone() {
        _eventNavigateToGame.value = false
        _eventLowEnergy.value = false
        buttonClickable = true
    }

    private fun onNotSubscribed() {
        if (energyValue > 0) {
            playGame()
        } else {
            lowEnergy()
        }
    }

    private fun onSubscribed() {
        playGame()
    }

    private fun lowEnergy() {
        _eventLowEnergy.value = true
    }

    private fun playGame() {
        energyModule.onPlayGame()
        _eventNavigateToGame.value = true
    }

    private fun formatDuration(dur: Long): String {
        return try {
            val s = dur.toDouble()
            String.format("%02d:%02d", ((s % 3600) / 60).toInt(), (s % 60).toInt())
        } catch (e: Exception) {
            "00:00"
        }
    }
}
