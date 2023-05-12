package com.wemadefun.neurality.ui.games.exactvalue

import com.wemadefun.neurality.ui.games.GameScoreModel

class ExactValueGameModel {

    private var _questionText = ""
    private var _questionAnswer = ""
    private var _choiceOneText = ""
    private var _choiceTwoText = ""
    private var _choiceThreeText = ""

    val questionText: String
        get() = _questionText
    val choiceOneText: String
        get() = _choiceOneText
    val choiceTwoText: String
        get() = _choiceTwoText
    val choiceThreeText: String
        get() = _choiceThreeText
    val scoreModel = GameScoreModel()

    init {
        onNext()
    }

    private fun onNext() {
        // TODO: Generate harder questions.
        when {
            (scoreModel.score < 3000) -> generateEasyQuestion()
            (scoreModel.score < 6000) -> generateEasyQuestion()
            else -> generateEasyQuestion()
        }
        randomizeAnswer()
    }

    private fun randomizeAnswer() {
        val answerInt = _questionAnswer.toInt()
        _choiceOneText = ((answerInt - 5) + (1..10).random()).toString()
        _choiceTwoText = ((answerInt - 5) + (1..10).random()).toString()
        _choiceThreeText = ((answerInt - 5) + (1..10).random()).toString()

        while (_choiceOneText == _questionAnswer) {
            _choiceOneText = ((answerInt - 5) + (1..10).random()).toString()
        }
        while (_choiceTwoText == _questionAnswer) {
            _choiceTwoText = ((answerInt - 5) + (1..10).random()).toString()
        }
        while (_choiceThreeText == _questionAnswer) {
            _choiceThreeText = ((answerInt - 5) + (1..10).random()).toString()
        }

        when ((1..3).random()) {
            1 -> _choiceOneText = _questionAnswer
            2 -> _choiceTwoText = _questionAnswer
            3 -> _choiceThreeText = _questionAnswer
        }
    }

    private fun generateEasyQuestion(){
        val num1 = (30..50).random()
        val num2 = (10..30).random()
        when ((0..1).random()) {
            0 -> {
                _questionAnswer = "${num1 + num2}"
                _questionText = "$num1 + $num2 = ?"
            }
            1 -> {
                _questionAnswer = "${num1 - num2}"
                _questionText = "$num1 - $num2 = ?"
            }
        }
    }

    fun onChoiceOneClick() {
        if (_questionAnswer == _choiceOneText) {
            scoreModel.onCorrect()
        } else {
            scoreModel.onIncorrect(true)
        }
        onNext()
    }

    fun onChoiceTwoClick() {
        if (_questionAnswer == _choiceTwoText) {
            scoreModel.onCorrect()
        } else {
            scoreModel.onIncorrect(true)
        }
        onNext()
    }

    fun onChoiceThreeClick() {
        if (_questionAnswer == _choiceThreeText) {
            scoreModel.onCorrect()
        } else {
            scoreModel.onIncorrect(true)
        }
        onNext()
    }

}