package com.wemadefun.neurality.ui.traindetails

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wemadefun.neurality.data.userdata.modules.DataModule
import com.wemadefun.neurality.data.userdata.modules.ScoreModule
import com.wemadefun.neurality.utils.GameProvider
import com.wemadefun.neurality.utils.getCategoryString
import com.wemadefun.neurality.utils.getSubcategoryString
import javax.inject.Inject

class TrainDetailsViewModel @Inject constructor(
    private val resources: Resources,
    private val scoreModule: ScoreModule,
    private val dataModule: DataModule
    ) : ViewModel() {

    private var _title = MutableLiveData<String>()
    private var _category = MutableLiveData<String>()
    private var _subCategory = MutableLiveData<String>()
    private var _description = MutableLiveData<String>()
    private var _imageResourceId = MutableLiveData<Int>()
    private var _navigationId = MutableLiveData<Int>()
    private var _highScore = MutableLiveData<String>()
    private var _eventNavigateToGame = MutableLiveData<Boolean>()
    private var _eventNavigateToTutorial = MutableLiveData<Boolean>()
    private var _eventLowEnergy = MutableLiveData<Boolean>()

    val title: LiveData<String>
        get() = _title
    val category: LiveData<String>
        get() = _category
    val subCategory: LiveData<String>
        get() = _subCategory
    val description: LiveData<String>
        get() = _description
    val imageResourceId: LiveData<Int>
        get() = _imageResourceId
    val eventNavigateToTutorial: LiveData<Boolean>
        get() = _eventNavigateToTutorial
    val highScore = Transformations.map(_highScore) {
        if (it.toInt() == 0) { "-" }
        else { it.toString() }
    }
    val isPremiumGamesEnabled: Boolean get() = dataModule.isPremiumGamesEnabled

    fun start(gameData: GameProvider.GameData) {
        _title.value = gameData.title
        _category.value = getCategoryString(gameData.category, resources)
        _subCategory.value = getSubcategoryString(gameData.subCategory, resources)
        _description.value = gameData.description
        _imageResourceId.value = gameData.drawableImgId
        _navigationId.value = gameData.navigationId
        _highScore.value = getHighScore(gameData)
    }

    fun onButtonTutorialClick() {
        _eventNavigateToTutorial.value = true
    }

    fun doneNavigating() {
        _eventNavigateToGame.value = false
        _eventNavigateToTutorial.value = false
        _eventLowEnergy.value = false
    }

    private fun getHighScore(gameData: GameProvider.GameData): String {
        return scoreModule.getTopFiveScore(gameData.gameId).max().toString()
    }
}
