package com.wemadefun.neurality.ui.eventgameover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wemadefun.neurality.data.userdata.modules.DataModule
import com.wemadefun.neurality.data.userdata.modules.ScoreModule
import com.wemadefun.neurality.utils.GameProvider
import javax.inject.Inject

class GameOverViewModel @Inject constructor(
    private val scoreModule: ScoreModule,
    private val dataModule: DataModule
) : ViewModel() {

    private val _score = MutableLiveData<String>()
    private val _previousGame = MutableLiveData<Int>()
    private val _eventGameReplay = MutableLiveData<Boolean>()
    private val _eventOtherWorkout = MutableLiveData<Boolean>()
    private val _topScore = MutableLiveData<List<Int>>()
    private var _eventLowEnergy = MutableLiveData<Boolean>()

    val score: LiveData<String>
        get() = _score
    val topScore: LiveData<List<Int>>
        get() = _topScore
    val eventGameReplay: LiveData<Boolean>
        get() = _eventGameReplay
    val eventOtherWorkout: LiveData<Boolean>
        get() = _eventOtherWorkout

    private var buttonClickable = true

    fun start(argScore: String, gameData: GameProvider.GameData) {
        _score.value = argScore
        _previousGame.value = gameData.navigationId
        _eventGameReplay.value = false
        _eventOtherWorkout.value = false
        _topScore.value = scoreModule.getTopFiveScore(gameData.gameId)
    }

    fun onOtherWorkoutButtonClick() {
        if (buttonClickable) { _eventOtherWorkout.value = true }
    }

    fun onButtonClickable() {
        buttonClickable = true
    }

    fun onButtonNotClickable() {
        buttonClickable = false
    }

    fun doneNavigating() {
        _eventGameReplay.value = false
        _eventOtherWorkout.value = false
        _eventLowEnergy.value = false
    }

    fun isUserNotRated(): Boolean {
        return !dataModule.isUserHasRatedApp
    }

    fun getUserGamesPlayed(): Int {
        return dataModule.gamesPlayed
    }

}
