package com.wemadefun.neurality.data.userdata.modules.energy.model

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wemadefun.neurality.data.userdata.UserData
import com.wemadefun.neurality.utils.CONFIG_ENERGY_ADD_TIME_MS
import com.wemadefun.neurality.utils.CONFIG_ENERGY_MAX
import com.wemadefun.neurality.utils.currentTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.floor

object EnergyModel {
    private var activeUserData: UserData? = null

    private var energyTimer: CountDownTimer? = null
    private val _energy = MutableLiveData<Int>()
    private val _energyTimeLeft = MutableLiveData<Long>()
    private val job = Job()
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + job)

    private var currentEnergy: Int = 0
    private var energyLastAddTime: Long = 0L
    private lateinit var onUpdateEnergyListener: (Int, Long) -> Unit

    val energy: LiveData<Int> get() = _energy
    val energyTimeLeft: LiveData<Long> get() = _energyTimeLeft
    val energyValue: Int get() = currentEnergy

    fun initialize(userData: UserData, onUpdateEnergy: (Int, Long) -> Unit) {
        if (userData == this.activeUserData) { return }

        this.activeUserData = userData
        currentEnergy = userData.energy
        energyLastAddTime = userData.energyLastAddTime

        onUpdateEnergyListener = onUpdateEnergy

        _energy.postValue(currentEnergy)
        _energyTimeLeft.postValue(1L)
        refreshEnergy()
    }

    fun destroy() {
        activeUserData = null
        onUpdateEnergyListener = { _: Int, _: Long -> }
        destroyEnergyTimer()
    }

    fun refreshEnergy() {
        if (currentEnergy >= CONFIG_ENERGY_MAX) {
            maxEnergy()
            return
        }
        val lastAddTime = energyLastAddTime
        var numberOfEnergyToAdd = ((currentTime - lastAddTime) / CONFIG_ENERGY_ADD_TIME_MS).toFloat()
        var nextAdd = CONFIG_ENERGY_ADD_TIME_MS
        numberOfEnergyToAdd = floor(numberOfEnergyToAdd).toInt().toFloat()

        if (numberOfEnergyToAdd > 0) {
            energyLastAddTime = currentTime
            nextAdd = CONFIG_ENERGY_ADD_TIME_MS - ((currentTime - lastAddTime) % CONFIG_ENERGY_ADD_TIME_MS)
        }
        if ((currentEnergy + numberOfEnergyToAdd) < CONFIG_ENERGY_MAX){
            addEnergy(numberOfEnergyToAdd.toInt())
            startEnergyTimer(nextAdd)
        }
        else { maxEnergy() }
    }

    fun useEnergy() {
        if (currentEnergy == CONFIG_ENERGY_MAX) { energyLastAddTime = System.currentTimeMillis() }
        // Energy Action Minus
        currentEnergy--
        if (currentEnergy < CONFIG_ENERGY_MAX) { startEnergyTimer(CONFIG_ENERGY_ADD_TIME_MS) }
        onUpdateEnergy()
    }

    fun addEnergy(count: Int) {
        // Energy Action Plus count
        currentEnergy += count
        if (currentEnergy >= CONFIG_ENERGY_MAX) { maxEnergy() }
        onUpdateEnergy()
    }

    private fun maxEnergy() {
        // Energy Action Max
        currentEnergy = CONFIG_ENERGY_MAX
        _energyTimeLeft.postValue(0L)
        destroyEnergyTimer()
        onUpdateEnergy()
    }

    /**
     * Starts energy timer at background thread.
     * onFinish(): if energy < max, repeat timer. else stop.
     */
    private fun startEnergyTimer(addTime: Long) {
        val oneSecond = 1000L
        val timeUntilNextEnergyAdd =
            (energyLastAddTime + addTime + 2L) - System.currentTimeMillis()

        if (timeUntilNextEnergyAdd < 0L)
            return

        destroyEnergyTimer()

        scope.launch {
            energyTimer = object : CountDownTimer(timeUntilNextEnergyAdd, oneSecond) {

                override fun onTick(millisUntilFinished: Long) {
                    _energyTimeLeft.postValue(millisUntilFinished / oneSecond)
                }
                override fun onFinish() {
                    _energyTimeLeft.postValue(0L)
                    refreshEnergy()
                }
            }
            energyTimer?.start()
        }
    }

    private fun destroyEnergyTimer() {
        energyTimer?.cancel()
        energyTimer = null
    }

    private fun onUpdateEnergy() {
        scope.launch {
            _energy.value = currentEnergy
            onUpdateEnergyListener(currentEnergy, energyLastAddTime)
        }
    }

}