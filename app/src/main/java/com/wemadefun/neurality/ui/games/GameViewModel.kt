package com.wemadefun.neurality.ui.games

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.*
import com.wemadefun.neurality.utils.CountDownTask
import com.wemadefun.neurality.utils.OnCountdownFinish
import com.wemadefun.neurality.utils.OnCountdownTick
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * Available Tools:
 *
 * Call -> [onGameOver] when game is over.
 *
 * Call -> [onUpdateScore] with updated [GameScoreModel].
 *
 * Bind onClick -> [onPauseButtonClick] at Pause Button at game layout xml.
 *
 * Bind View -> [scoreText], [scoreBonusText], [timeLeftText]
 *
 * Hint 1: If you want to use [CountDownTimer], call [useCountdownTimer] at child [ViewModel]'s init,
 * Then bind [timeLeftText].
 *
 * Hint 2: If you want to use Lives, call [useLives] at child [ViewModel]'s init,
 * Then bind [livesText]. Do Not Use Both!
 *
 * Hint 3: [onGameEventCorrect] and [onGameEventIncorrect] invocated when whether [score] is increased or not.
 * These correction events interfaced at [GameFragment] and [GameViewModel]'s [onCorrect] and [onIncorrect]
 *
 * Game Event Lifecycle: [onUpdateScore] -> [onCorrect] or [onIncorrect] -> [onNext]
 *
 */
abstract class GameViewModel : ViewModel() {

    private val _eventGameOver = MutableLiveData<Boolean>()
    private val _eventGamePause = MutableLiveData<Boolean>()
    private val _eventGameCorrect = MutableLiveData<Boolean>()
    private val _eventGameIncorrect = MutableLiveData<Boolean>()
    private val _eventGameNext = MutableLiveData<Boolean>()
    private val _scoreBonus = MutableLiveData<Int>()
    private val _score = MutableLiveData<Int>()

    val eventGameOver: LiveData<Boolean> = _eventGameOver
    val eventGamePause: LiveData<Boolean> = _eventGamePause
    val eventGameCorrect: LiveData<Boolean> = _eventGameCorrect
    val eventGameIncorrect: LiveData<Boolean> = _eventGameIncorrect
    val eventGameNext: LiveData<Boolean> = _eventGameNext
    val scoreBonus: LiveData<Int> = _scoreBonus
    val score: LiveData<Int> = _score

    private var useTimer = false
    private var useLives = false
    private var isGameOver = false

    private var timerCountDownTask: CountDownTask? = null
    private val _timeLeft = MutableLiveData<Long>()
    private val timeLeft: LiveData<Long> = _timeLeft

    private val _lives = MutableLiveData<Int>()
    protected val lives: LiveData<Int> = _lives

    val timeLeftText = Transformations.map(timeLeft) { DateUtils.formatElapsedTime(it/1000) }
    val livesText = Transformations.map(lives) { "$it" }
    val scoreText = Transformations.map(score) { "$it" }
    val scoreBonusText = Transformations.map(scoreBonus) { "+$it" }

    init {
        _score.value = 0
        _scoreBonus.value = 0
    }

    private fun updateScore(scoreModel: GameScoreModel) {
        _score.value = scoreModel.score
        _scoreBonus.value = scoreModel.scoreBonus
    }

    private fun checkLives() {
        viewModelScope.launch {
            _lives.value = _lives.value?.minus(1)
            onCheckLives(_lives.value!!)

            if (_lives.value!! < 1) {
                viewModelScope.launch {
                    onGameOver()
                }
            }
        }

    }

    open suspend fun onCheckLives(lives: Int) {}


    private fun onGameEventCorrect() {
        _eventGameCorrect.value = true
    }

    private fun onGameEventIncorrect() {
        _eventGameIncorrect.value = true

        if (useLives) {
            checkLives()
        }
    }

    protected fun onGameEventNext() {
        _eventGameNext.value = true
    }

    fun eventGameOverDone() {
        _eventGameOver.value = false
    }

    fun eventGamePauseDone() {
        _eventGamePause.value = false
    }

    fun eventGameCorrectDone() {
        _eventGameCorrect.value = false
    }

    fun eventGameIncorrectDone() {
        _eventGameIncorrect.value = false
    }

    fun eventGameNextDone() {
        _eventGameNext.value = false
    }

    fun onPauseButtonClick() {
        _eventGamePause.value = true
    }

    fun onGameOver() {
        viewModelScope.launch {
            isGameOver = true
            delay(1000)
            _eventGameOver.value = true
        }
    }

    fun useLives(lives: Int) {
        useLives = true
        if (useLives && useTimer) { throw Exception("Cannot use Timer & Lives at same time!") }

        _lives.value = lives
    }

    private fun useCountdownTask(countdownInMillis: Long) {
        useTimer = true
        if (useLives && useTimer) { throw Exception("Cannot use Timer & Lives at same time!") }

        val onTick = object : OnCountdownTick {
            override fun onTick(timeLeft: Long) { _timeLeft.postValue(timeLeft) }
        }
        val onFinish = object : OnCountdownFinish {
            override fun onFinish() { onGameOver() }
        }

        val second = 1000L
        val timeLeft = (countdownInMillis / second).toFloat()
        timerCountDownTask?.clear()
        timerCountDownTask = CountDownTask(timeLeft, second)
        timerCountDownTask?.start(onTick, onFinish)
    }

    fun useCountdownTimer(countdownInMillis: Long) {
        useCountdownTask(countdownInMillis)
    }

    private fun destroyTimer() {
        timerCountDownTask?.clear()
    }

    fun pauseTimer() {
        timerCountDownTask?.pause()
    }

    fun resumeTimer() {
        timerCountDownTask?.resume()
    }


    /**
    * Calls [onCorrect] if new score is higher than old, else [onIncorrect]
    */
    fun onUpdateScore(scoreModel: GameScoreModel) {
        if (!isGameOver) {
            viewModelScope.launch {
                val difference = scoreModel.score - score.value!!

                if (scoreModel.score > score.value!!) {
                    onGameEventCorrect()
                    updateScore(scoreModel)
                    onCorrect()
                } else {
                    onGameEventIncorrect()
                    updateScore(scoreModel)
                    onIncorrect()
                }
                onNext()
                onGameEventNext()
            }
        }
    }

    /**
     * [onCorrect] is open function that called after [onUpdateScore] if [score] is increased.
     *
     * [onCorrect] is running under [viewModelScope]
     */
    open suspend fun onCorrect() {}

    /**
     * [onIncorrect] is open function that called after [onUpdateScore] if [score] is decreased.
     *
     * [onIncorrect] is running under [viewModelScope]
     */
    open suspend fun onIncorrect() {}

    /**
     * [onNext] at [GameViewModel] is abstract function that called after [onCorrect] or [onIncorrect].
     *
     * [onNext] is MUST called at init if you update ui related elements at [onNext].
     *
     * [onNext] is running under [viewModelScope]
     *
     * IMPORTANT: Do not call [onUpdateScore] inside [onNext]!!
     */
    open suspend fun onNext() {
    }

    override fun onCleared() {
        super.onCleared()
        destroyTimer()
    }
}