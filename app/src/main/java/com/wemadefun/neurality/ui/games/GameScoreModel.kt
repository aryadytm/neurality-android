package com.wemadefun.neurality.ui.games

import com.wemadefun.neurality.utils.CONFIG_SCORE_BASE_CORRECT
import com.wemadefun.neurality.utils.CONFIG_SCORE_STREAK_BONUS

class GameScoreModel {

    private var _streakBonus: Int = 0
    private var _score: Int = 0
    private var _scoreIncrement: Int = CONFIG_SCORE_BASE_CORRECT
    private var _scoreDecrement: Int = 100
    private var _streakBonusIncrement: Int = CONFIG_SCORE_STREAK_BONUS
    private var _numCorrects = 0
    private var _numQuestions = 0

    val score: Int
        get() = _score
    val scoreBonus: Int
        get() = _streakBonus
    val numCorrects: Int
        get() = _numCorrects
    val numQuestions: Int
        get() = _numQuestions

    private fun addScore() {
        _score += _scoreIncrement + _streakBonus
        _streakBonus += _streakBonusIncrement  // Score adder is added by score streak bonus.
    }

    private fun minusScore() {
        if (score > _scoreDecrement)  { _score -= _scoreDecrement }
    }

    private fun destroyStreak() {
        _streakBonus = 0
    }
    
    fun setScoreIncrement(increment: Int) {
        _scoreIncrement = increment
    }

    fun setScoreDecrement(decrement: Int) {
        _scoreDecrement = decrement
    }

    fun setScoreBonusIncrement(increment: Int) {
        _streakBonusIncrement = increment
    }

    fun onCorrect() {
        addScore()
        _numCorrects += 1
        _numQuestions += 1
    }

    fun onIncorrect(minusScore: Boolean) {
        destroyStreak()
        if (minusScore) {
            minusScore()
        }
        _numQuestions += 1
    }

}