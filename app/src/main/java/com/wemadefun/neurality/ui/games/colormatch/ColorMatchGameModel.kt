package com.wemadefun.neurality.ui.games.colormatch

import com.wemadefun.neurality.ui.games.GameScoreModel

class ColorMatchGameModel {

    enum class Color {
        RED, GREEN, BLUE, YELLOW, BLACK
    }

    private lateinit var _topMeaningText: Color
    private lateinit var _bottomText: Color
    private lateinit var _bottomTextColor: Color
    private var _isMatch = false

    val scoreModel = GameScoreModel()
    val topText: Color get() = _topMeaningText
    val bottomText: Color get() = _bottomText
    val bottomTextColor: Color get() = _bottomTextColor

    init {
        scoreModel.setScoreDecrement(200)
        onNext()
    }

    private fun onNext() {
        _isMatch = (0..1).random() == 1
        if (_isMatch) { generateMatching() }
        else { generateNonMatching() }
    }

    private fun generateMatching() {
        _topMeaningText = Color.values().random()
        _bottomText = Color.values().random() // Bottom text always random
        _bottomTextColor = _topMeaningText // Bottom color must same with top meaning text
    }

    private fun generateNonMatching() {
        _topMeaningText = Color.values().random()
        _bottomText = Color.values().random() // Bottom text always random
        do {
            _bottomTextColor = Color.values().random() // Bottom color must different from top meaning text
        } while (_bottomTextColor == _topMeaningText)
    }

    fun onYesClick() {
        if (_bottomTextColor == _topMeaningText) {
            scoreModel.onCorrect()
        } else {
            scoreModel.onIncorrect(true)
        }
        onNext()
    }

    fun onNoClick() {
        if (_bottomTextColor != _topMeaningText) {
            scoreModel.onCorrect()
        } else {
            scoreModel.onIncorrect(true)
        }
        onNext()
    }

}