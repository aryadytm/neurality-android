package com.wemadefun.neurality.data.userdata.modules

import com.wemadefun.neurality.data.userdata.UserDataRepository
import com.wemadefun.neurality.utils.GameProvider
import javax.inject.Inject

class ScoreModule @Inject constructor(private val repository: UserDataRepository) {
    /**
     * Returns list of top five sorted score.
     */
    fun getTopFiveScore(id: Int): MutableList<Int> {
        return repository.userData.gameScores[id.toString()]!!
    }

    fun getNeuralityScore(): Int {
        for (i in getCategoricalScore()) { if (i == 0) { return 0 } }
        val score = getCategoricalScore().average().toInt() + 3
        return if (score > 160) { 160 } else { score }
    }

    /**
     * Returns list of categorical score.
     */
    fun getCategoricalScore(): List<Int> {
        val categoricalScoreList = mutableListOf<Int>()
        val numCategories = GameProvider.getInstance().trainItemData.keys

        for (category in numCategories) {
            val score = repository.userData.neuralityScores[category.toString()]!!
            categoricalScoreList.add(score)
        }
        return categoricalScoreList
    }

    /**
     * Inserts game score and then save it.
     */
    fun insertGameScore(id: Int, score: Int) {
        val oldScoreList = getTopFiveScore(id)
        oldScoreList.add(score)
        val newScoreList = oldScoreList.sortedByDescending { it }.slice(0..4).toMutableList()

        repository.ifActive {
            it.gameScores[id.toString()] = newScoreList
        }
        updateCategoricalScore()
    }

    /**
     * Updates categorical score by measuring every game scores.
     */
    private fun updateCategoricalScore() {
        val gamesData = GameProvider.getInstance()
        val keys = gamesData.trainItemData.keys
        val highestScoreWeight = 3

        for (cat in keys) {
            val category = cat.toString()
            val bestScoreOfGame = mutableListOf<Int>()

            for (gameInCategory in gamesData.trainItemData[category.toInt()] ?: throw Exception("activeGameData is null!")) {
                if (getTopFiveScore(gameInCategory.gameId).max() == 0) { continue }
                bestScoreOfGame.add(getTopFiveScore(gameInCategory.gameId).max()?: 0)
            }
            if (bestScoreOfGame.max() == null) { continue }

            val highestScore = bestScoreOfGame.max()!!
            val scoreWithoutHighest = bestScoreOfGame.subtract(listOf(highestScore)).toMutableList()
            var categoryScore =
                (highestScoreWeight * highestScore + scoreWithoutHighest.sum()) / (highestScoreWeight + bestScoreOfGame.size-1)
            categoryScore = 50 + (categoryScore / 100)

            repository.ifActiveThenSaveAll {
                when {
                    categoryScore <= 55 -> it.neuralityScores[category] = 0
                    categoryScore > 135 -> it.neuralityScores[category] = 135 + ((categoryScore - 135) / 2)
                    categoryScore > 180 -> it.neuralityScores[category] = 180
                    else -> it.neuralityScores[category] = categoryScore
                }
            }
        }
    }
}