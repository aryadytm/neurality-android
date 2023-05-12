package com.wemadefun.neurality.ui.games.colormatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wemadefun.neurality.ui.games.GameViewModel
import com.wemadefun.neurality.utils.CONFIG_TIME_LIMIT_40S
import kotlinx.coroutines.launch

class ColorMatchViewModel : GameViewModel() {

    private lateinit var gameModel: ColorMatchGameModel
    private val _topText = MutableLiveData<ColorMatchGameModel.Color>()
    private val _bottomText = MutableLiveData<ColorMatchGameModel.Color>()
    private val _bottomColor = MutableLiveData<ColorMatchGameModel.Color>()

    val topText: LiveData<ColorMatchGameModel.Color> = _topText
    val bottomText: LiveData<ColorMatchGameModel.Color> = _bottomText
    val bottomColor: LiveData<ColorMatchGameModel.Color> = _bottomColor

    init {
        if (!this::gameModel.isInitialized) {
            viewModelScope.launch {
                gameModel = ColorMatchGameModel()
                useCountdownTimer(CONFIG_TIME_LIMIT_40S)
                onNext()
                onGameEventNext()
            }
        }
    }

    override suspend fun onNext() {
        _topText.value = gameModel.topText
        _bottomText.value = gameModel.bottomText
        _bottomColor.value = gameModel.bottomTextColor
    }

    fun onYesClick() {
        gameModel.onYesClick()
        onUpdateScore(gameModel.scoreModel)
    }

    fun onNoClick() {
        gameModel.onNoClick()
        onUpdateScore(gameModel.scoreModel)
    }

}