package com.wemadefun.neurality.ui.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wemadefun.neurality.data.userdata.modules.ScoreModule
import javax.inject.Inject

class StatsViewModel @Inject constructor(scoreModule: ScoreModule) : ViewModel() {

    private val _state = MutableLiveData<StatsState>()
    val state: LiveData<StatsState> = _state

    private val _scores = MutableLiveData<List<Int>>()
    val scores: LiveData<List<Int>> = _scores

    private val _neuralityScore = MutableLiveData<Int>()
    val neuralityScore: LiveData<Int> = _neuralityScore

    init {
        _scores.value = scoreModule.getCategoricalScore()
        _neuralityScore.value = scoreModule.getNeuralityScore()
        _state.value = StatsState.STATE_BY_CATEGORY
    }

    fun onByGameButtonClick() {
        _state.value = StatsState.STATE_BY_GAME
    }

    fun onByCategoryButtonClick() {
        _state.value = StatsState.STATE_BY_CATEGORY
    }
}