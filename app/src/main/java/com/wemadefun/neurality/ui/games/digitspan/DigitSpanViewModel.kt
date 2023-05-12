package com.wemadefun.neurality.ui.games.digitspan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wemadefun.neurality.ui.games.GameScoreModel
import com.wemadefun.neurality.ui.games.GameViewModel
import com.wemadefun.neurality.utils.CountDownTask
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DigitSpanViewModel : GameViewModel() {

    private var digits = 3
    private val scoreModel = GameScoreModel()
    private val numbersList = listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    var numbersSequence = ""
    var paused = false

    private var isRunning = false
    private var numbers = mutableListOf<Char>()
    private lateinit var _countDownTask: CountDownTask

    private var _clickable = MutableLiveData<Boolean>()
    private var _displayNumber = MutableLiveData<String>()
    private var _eventSubmit = MutableLiveData<Boolean>()
    private var _eventShowCorrect = MutableLiveData<Boolean>()

    val clickable: LiveData<Boolean> = _clickable
    val displayNumber: LiveData<String> = _displayNumber
    val eventSubmit: LiveData<Boolean> = _eventSubmit
    val eventShowCorrect: LiveData<Boolean> = _eventShowCorrect

    init {
        viewModelScope.launch {
            _clickable.postValue(false)
            scoreModel.setScoreIncrement(1000)
            scoreModel.setScoreBonusIncrement(100)
            useLives(3)
            onNext()
        }
    }

    override suspend fun onNext() {
        _clickable.postValue(false)
        _eventShowCorrect.postValue(false)
        numbersSequence = randomNumbers()
        numbers = numbersSequence.toMutableList()
        showNumbersInSequence()
    }

    override suspend fun onCorrect() {
        _clickable.postValue(false)

        digits++
        delay(1500)
    }

    override suspend fun onIncorrect() {
        _clickable.postValue(false)
        _eventShowCorrect.postValue(true)

        if (digits > 3) { digits-- }
        delay(1500)
    }

    private fun randomNumbers(): String {
        val listOfNumbers = numbersList.toMutableList()
        listOfNumbers.shuffle()

        var numbers = ""
        while (numbers.length < digits) {
            if (listOfNumbers.isNotEmpty()) { numbers += listOfNumbers.removeAt(0) }
            else {
                var newNumber = numbersList.random()
                if (newNumber == numbers.last()) {
                    newNumber = (newNumber.toInt() + 1).toChar()
                }
                numbers += newNumber
            }
        }
        return numbers
    }

    fun showNumbersInSequence() {
        if (isRunning) { return }

        viewModelScope.launch {
            isRunning = true

            while (numbers.isNotEmpty()) {
                if (!paused) {
                    _displayNumber.postValue(numbers.removeAt(0).toString())
                    delay(1000)
                } else {
                    isRunning = false
                    return@launch
                }
            }
            isRunning = false
            _clickable.postValue(true)
        }
    }

    fun onSubmitClick() {
        if (clickable.value!!) { _eventSubmit.value = true }
    }

    fun onSubmit(numberSubmitted: String) {
        _eventSubmit.value = false
        if (numberSubmitted == numbersSequence) {
            scoreModel.onCorrect()
        } else {
            scoreModel.onIncorrect(false)
        }
        onUpdateScore(scoreModel)
    }
}