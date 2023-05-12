package com.wemadefun.neurality.ui.games.dayofweek

import com.wemadefun.neurality.ui.games.GameScoreModel

// TODO: Target is X days will/ago which is very big value.
class DayOfWeekGameModel {

    private val days = (1..7)

    private var _currentDay = 1
    private var _targetDay = 1
    private var _choiceOneDay = 1
    private var _choiceTwoDay = 1
    private var _choiceThreeDay = 1

    val scoreModel = GameScoreModel()
    val currentDay: Int get() = _currentDay
    val targetDay: Int get() = _targetDay
    val choiceOneDay: Int get() = _choiceOneDay
    val choiceTwoDay: Int get() = _choiceTwoDay
    val choiceThreeDay: Int get() = _choiceThreeDay

    init {
        scoreModel.setScoreIncrement(300)
        onNext()
    }

    private fun onNext() {
        _currentDay = days.random()
        _targetDay = days.subtract(mutableListOf(_currentDay)).random()
        randomizeChoice()
    }

    private fun randomizeChoice() {
        val ignored = mutableListOf(_currentDay, _targetDay)
        var choiceOne = days.subtract(ignored).random()
        ignored.add(choiceOne)
        var choiceTwo = days.subtract(ignored).random()
        ignored.add(choiceTwo)
        var choiceThree = days.subtract(ignored).random()

        when ((1..3).random()) {
            1 -> choiceOne = Integer.valueOf(_targetDay) // Prevent same reference id.
            2 -> choiceTwo = Integer.valueOf(_targetDay)
            3 -> choiceThree = Integer.valueOf(_targetDay)
        }
        _choiceOneDay = choiceOne
        _choiceTwoDay = choiceTwo
        _choiceThreeDay = choiceThree
    }

    fun onChoiceOneClick() {
        if (_choiceOneDay == _targetDay) {
            scoreModel.onCorrect()
        } else {
            scoreModel.onIncorrect(false)
        }
        onNext()
    }

    fun onChoiceTwoClick() {
        if (_choiceTwoDay == _targetDay) {
            scoreModel.onCorrect()
        } else {
            scoreModel.onIncorrect(false)
        }
        onNext()
    }

    fun onChoiceThreeClick() {
        if (_choiceThreeDay == _targetDay) {
            scoreModel.onCorrect()
        } else {
            scoreModel.onIncorrect(false)
        }
        onNext()
    }

}