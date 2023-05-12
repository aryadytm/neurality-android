package com.wemadefun.neurality.ui.games.differentshape

import com.wemadefun.neurality.ui.games.GameScoreModel

class DifferentShapeGameModel {

    enum class Shape {
        SHAPE_1, SHAPE_2, SHAPE_3, SHAPE_4
    }

    val scoreModel = GameScoreModel()
    var currentShape: Shape = Shape.values().random()
    private lateinit var lastShape: Shape

    init {
        scoreModel.setScoreDecrement(200)
        onNextShape()
    }

    private fun getRandomShape(): Shape {
        return Shape.values().random()
    }

    private fun getSameShape(): Shape {
        return lastShape
    }

    fun onNextShape() {
        // Current shape exists, move it to last shape
        lastShape = currentShape
        val chance = (0..1).random()
        currentShape = if (chance == 0){
            getRandomShape()
        } else {
            getSameShape()
        }
    }

    fun onYes() {
        if (currentShape == lastShape) {
            scoreModel.onCorrect()
        } else {
            scoreModel.onIncorrect(true)
        }
        onNextShape()
    }

    fun onNo() {
        if (currentShape != lastShape) {
            scoreModel.onCorrect()
        } else {
            scoreModel.onIncorrect(true)
        }
        onNextShape()
    }

}