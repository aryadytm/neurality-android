package com.wemadefun.neurality.ui.games.dayofweek

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wemadefun.neurality.ui.games.GameViewModel
import com.wemadefun.neurality.utils.CONFIG_TIME_LIMIT_90S
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DayOfWeekViewModel : GameViewModel() {

    private val _currentDay = MutableLiveData<Int>()
    private val _targetDay = MutableLiveData<Int>()
    private val _choiceOneDay = MutableLiveData<Int>()
    private val _choiceTwoDay = MutableLiveData<Int>()
    private val _choiceThreeDay = MutableLiveData<Int>()

    val currentDay: LiveData<Int> = _currentDay
    val targetDay: LiveData<Int> = _targetDay
    val choiceOneDay: LiveData<Int> = _choiceOneDay
    val choiceTwoDay: LiveData<Int> = _choiceTwoDay
    val choiceThreeDay: LiveData<Int> = _choiceThreeDay

    private lateinit var gameModel: DayOfWeekGameModel
    val clickable = MutableLiveData<Boolean>()

    init {
        if (!this::gameModel.isInitialized) {
            viewModelScope.launch {
                gameModel = DayOfWeekGameModel()
                clickable.postValue(false)
                useCountdownTimer(CONFIG_TIME_LIMIT_90S)
                onNext()
                onGameEventNext()
            }
        }
    }

    override suspend fun onNext() {
        delay(350)
        clickable.postValue(true)
        _currentDay.value = gameModel.currentDay
        _targetDay.value = gameModel.targetDay
        _choiceOneDay.value = gameModel.choiceOneDay
        _choiceTwoDay.value = gameModel.choiceTwoDay
        _choiceThreeDay.value = gameModel.choiceThreeDay
    }

    fun onChoiceOneClick() {
        if (clickable.value!!) {
            clickable.value = false
            gameModel.onChoiceOneClick()
            onUpdateScore(gameModel.scoreModel)
        }

    }

    fun onChoiceTwoClick() {
        if (clickable.value!!) {
            gameModel.onChoiceTwoClick()
            onUpdateScore(gameModel.scoreModel)
        }
    }

    fun onChoiceThreeClick() {
        if (clickable.value!!) {
            gameModel.onChoiceThreeClick()
            onUpdateScore(gameModel.scoreModel)
        }
    }

    override suspend fun onCorrect() {
        clickable.postValue(false)
    }

    override suspend fun onIncorrect() {
        clickable.postValue(false)
    }

}