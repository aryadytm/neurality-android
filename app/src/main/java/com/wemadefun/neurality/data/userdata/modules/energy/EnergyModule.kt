package com.wemadefun.neurality.data.userdata.modules.energy

import androidx.lifecycle.LiveData
import com.wemadefun.neurality.data.userdata.UserDataRepository
import com.wemadefun.neurality.data.userdata.modules.energy.model.EnergyModel
import com.wemadefun.neurality.utils.isIapMode
import javax.inject.Inject

class EnergyModule @Inject constructor(private val repository: UserDataRepository) {

    init {
        repository.ifActive {
            EnergyModel.initialize(repository.userData) { energy: Int, lastAddTime: Long ->
                // On Energy Changed
                repository.ifActiveThenSaveLocal {
                    it.energy = energy
                    it.energyLastAddTime = lastAddTime
                }
            }
        }
    }

    val energyValue: Int get() = EnergyModel.energyValue
    val energyLiveData: LiveData<Int> get() = EnergyModel.energy
    val energyTimeLeftLiveData: LiveData<Long> get() = EnergyModel.energyTimeLeft

    fun onDestroy() {
        EnergyModel.destroy()
    }

    fun onUserRewardedFromVideoAd() {
        EnergyModel.addEnergy(1)
    }

    fun onPlayGame() {
        repository.ifActiveThenSaveAll {
            it.playCount++
            if (!isIapMode()) {
                EnergyModel.useEnergy()
            }
        }
    }

}