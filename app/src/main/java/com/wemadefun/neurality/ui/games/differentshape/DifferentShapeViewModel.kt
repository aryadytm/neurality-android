package com.wemadefun.neurality.ui.games.differentshape

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wemadefun.neurality.ui.games.GameViewModel
import com.wemadefun.neurality.ui.games.differentshape.DifferentShapeGameModel.Shape
import com.wemadefun.neurality.utils.CONFIG_TIME_LIMIT_40S
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DifferentShapeViewModel : GameViewModel() {

    private lateinit var gameModel: DifferentShapeGameModel

    private val _clickable = MutableLiveData<Boolean>()
    val clickable: LiveData<Boolean> = _clickable

    private val _shape = MutableLiveData<Shape>()
    val shapeId: LiveData<Shape> = _shape

    fun start(model: DifferentShapeGameModel) {

        if (!this::gameModel.isInitialized) {

            viewModelScope.launch {
                _clickable.value = false
                gameModel = model
                changeShape()
                delay(3000L)  // Time for player to remember first shape.
                changeShape()
                useCountdownTimer(CONFIG_TIME_LIMIT_40S)
                _clickable.value = true
            }

        }
    }

    private suspend fun changeShape() {
        gameModel.onNextShape()
        onNext()
        onGameEventNext()
    }

    override suspend fun onNext() {
        _shape.value = gameModel.currentShape
    }

    fun onYesClick() {
        if (clickable.value!!) {
            gameModel.onYes()
            onUpdateScore(gameModel.scoreModel)
        }
    }

    fun onNoClick() {
        if (clickable.value!!) {
            gameModel.onNo()
            onUpdateScore(gameModel.scoreModel)
        }
    }

}