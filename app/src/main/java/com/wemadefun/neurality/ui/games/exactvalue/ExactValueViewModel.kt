package com.wemadefun.neurality.ui.games.exactvalue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wemadefun.neurality.ui.games.GameViewModel
import com.wemadefun.neurality.utils.CONFIG_TIME_LIMIT_90S
import kotlinx.coroutines.launch

class ExactValueViewModel : GameViewModel() {

    private val _choiceOneText = MutableLiveData<String>()
    private val _choiceTwoText = MutableLiveData<String>()
    private val _choiceThreeText = MutableLiveData<String>()
    private val _questionText = MutableLiveData<String>()

    val choiceOneText: LiveData<String> = _choiceOneText
    val choiceTwoText: LiveData<String> = _choiceTwoText
    val choiceThreeText: LiveData<String> = _choiceThreeText
    val questionText: LiveData<String> = _questionText

    private lateinit var gameModel: ExactValueGameModel

    init {
        if (!this::gameModel.isInitialized) {
            viewModelScope.launch{
                gameModel = ExactValueGameModel()
                useCountdownTimer(CONFIG_TIME_LIMIT_90S)
                onNext()
            }
        }
    }

    override suspend fun onNext() {
        _choiceOneText.value = gameModel.choiceOneText
        _choiceTwoText.value = gameModel.choiceTwoText
        _choiceThreeText.value = gameModel.choiceThreeText
        _questionText.value = gameModel.questionText
    }

    fun onChoiceOneClick() {
        gameModel.onChoiceOneClick()
        onUpdateScore(gameModel.scoreModel)
    }

    fun onChoiceTwoClick() {
        gameModel.onChoiceTwoClick()
        onUpdateScore(gameModel.scoreModel)
    }

    fun onChoiceThreeClick() {
        gameModel.onChoiceThreeClick()
        onUpdateScore(gameModel.scoreModel)
    }
}