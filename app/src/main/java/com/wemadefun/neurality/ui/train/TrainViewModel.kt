package com.wemadefun.neurality.ui.train

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wemadefun.neurality.utils.GameProvider
import javax.inject.Inject

class TrainViewModel @Inject constructor(gameProvider: GameProvider) : ViewModel() {

    private val _trainUiData = MutableLiveData<Map<Int, List<GameProvider.GameData>>>()
    val trainUiDataData: LiveData<Map<Int, List<GameProvider.GameData>>>
        get() = _trainUiData

    init {
        _trainUiData.value = gameProvider.trainItemData
    }
}