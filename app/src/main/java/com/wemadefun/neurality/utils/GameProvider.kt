package com.wemadefun.neurality.utils

import android.content.res.Resources
import android.os.Parcelable
import com.wemadefun.neurality.R
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameProvider @Inject constructor(resources: Resources)
{

    companion object {
        private var instance: GameProvider? = null

        fun getInstance(): GameProvider {
            return instance!!
        }
    }

    @Parcelize
    data class GameData (
        val gameId: Int,
        val title: String,
        val drawableImgId: Int,
        val category: Int,
        val subCategory: Int,
        val description: String,
        val navigationId: Int,
        val tutorialUri: String,
        val tutorialText: String,
        val isPremiumOnly: Boolean
    ) : Parcelable

    // TODO: Add tutorial for every game.

    val gameData: List<GameData> = listOf(
        GameData(
            gameId = ID_GAME_MARTIALARTS,
            category = ID_CATEGORY_MEMORY,
            subCategory = ID_SUBCATEGORY_VISUAL_MEMORY,
            title = resources.getString(R.string.game_title_martialarts),
            description = resources.getString(R.string.desc_martialarts),
            navigationId = R.id.action_game_martialarts,
            drawableImgId = R.drawable.thumbnail_martialarts,
            tutorialUri = resources.getString(R.string.uri_mp4_tutorial_verbalfrenzy),
            tutorialText = "",
            isPremiumOnly = true
        ),
        GameData(
            gameId = ID_GAME_VERBALFRENZY,
            category = ID_CATEGORY_MEMORY,
            subCategory = ID_SUBCATEGORY_VERBAL_MEMORY,
            title = resources.getString(R.string.game_title_verbalfrenzy),
            description = resources.getString(R.string.desc_verbal_frenzy),
            navigationId = R.id.action_game_verbalfrenzy,
            drawableImgId = R.drawable.thumbnail_verbalfrenzy,
            tutorialUri = "assets:///tutorial_verbalfrenzy.mp4",
            tutorialText = resources.getString(R.string.tutorial_verbalfrenzy),
            isPremiumOnly = false
        ),
        GameData(
            gameId = ID_GAME_NUMBERRECALL,
            category = ID_CATEGORY_MEMORY,
            subCategory = ID_SUBCATEGORY_SHORTTERM_MEMORY,
            title = resources.getString(R.string.game_title_numberrecall),
            description = resources.getString(R.string.desc_numberrecall),
            navigationId = R.id.action_game_numberrecall,
            drawableImgId = R.drawable.thumbnail_numberrecall,
            tutorialUri = resources.getString(R.string.uri_mp4_tutorial_verbalfrenzy),
            tutorialText = "",
            isPremiumOnly = false
        ),
        GameData(
            gameId = ID_GAME_DIGITSPAN,
            category = ID_CATEGORY_MEMORY,
            subCategory = ID_SUBCATEGORY_WORKING_MEMORY,
            title = resources.getString(R.string.game_title_digitspan),
            description = resources.getString(R.string.desc_digitspan),
            navigationId = R.id.action_game_digitspan,
            drawableImgId = R.drawable.thumbnail_digitspan,
            tutorialUri = resources.getString(R.string.uri_mp4_tutorial_verbalfrenzy),
            tutorialText = "",
            isPremiumOnly = false
        ),
        GameData(
            gameId = ID_GAME_SALARY,
            category = ID_CATEGORY_CALCULATION,
            subCategory = ID_SUBCATEGORY_NUMERICAL_PROCESSING,
            title = resources.getString(R.string.game_title_salary),
            description = resources.getString(R.string.desc_salary),
            navigationId = R.id.action_game_salary,
            drawableImgId = R.drawable.thumbnail_salary,
            tutorialUri = resources.getString(R.string.uri_mp4_tutorial_verbalfrenzy),
            tutorialText = "",
            isPremiumOnly = true
        ),
        GameData(
            gameId = ID_GAME_HIGHESTNUMBER,
            category = ID_CATEGORY_CALCULATION,
            subCategory = ID_SUBCATEGORY_NUMERICAL_ESTIMATION,
            title = resources.getString(R.string.game_title_highestnumber),
            description = resources.getString(R.string.desc_highest_number),
            navigationId = R.id.action_game_highestnumber,
            drawableImgId = R.drawable.thumbnail_highestnumber,
            tutorialUri = resources.getString(R.string.uri_mp4_tutorial_verbalfrenzy),
            tutorialText = "",
            isPremiumOnly = false
        ),
        GameData(
            gameId = ID_GAME_TRUEFALSE,
            category = ID_CATEGORY_CALCULATION,
            subCategory = ID_SUBCATEGORY_NUMERICAL_PROCESSING,
            title = resources.getString(R.string.game_title_truefalse),
            description = resources.getString(R.string.desc_truefalse),
            navigationId = R.id.action_game_truefalse,
            drawableImgId = R.drawable.thumbnail_truefalse,
            tutorialUri = resources.getString(R.string.uri_mp4_tutorial_verbalfrenzy),
            tutorialText = "",
            isPremiumOnly = false
        ),
        GameData(
            gameId = ID_GAME_EXACTVALUE,
            category = ID_CATEGORY_CALCULATION,
            subCategory = ID_SUBCATEGORY_CALCULATION_ACCURACY,
            title = resources.getString(R.string.game_title_exactvalue),
            description = resources.getString(R.string.desc_exactvalue),
            navigationId = R.id.action_game_exactvalue,
            drawableImgId = R.drawable.thumbnail_exactvalue,
            tutorialUri = resources.getString(R.string.uri_mp4_tutorial_verbalfrenzy),
            tutorialText = "",
            isPremiumOnly = false
        ),
        GameData(
            gameId = ID_GAME_OBJECTFINDER,
            category = ID_CATEGORY_FOCUS,
            subCategory = ID_SUBCATEGORY_FOCUS_SPEED,
            title = resources.getString(R.string.game_title_objectfinder),
            description = resources.getString(R.string.desc_objectfinder),
            navigationId = R.id.action_game_objectfinder,
            drawableImgId = R.drawable.thumbnail_objectfinder,
            tutorialUri = resources.getString(R.string.uri_mp4_tutorial_verbalfrenzy),
            tutorialText = "",
            isPremiumOnly = true
        ),
        GameData(
            gameId = ID_GAME_DIFFERENTSHAPE,
            category = ID_CATEGORY_FOCUS,
            subCategory = ID_SUBCATEGORY_MENTAL_AGILITY,
            title = resources.getString(R.string.game_title_differentshape),
            description = resources.getString(R.string.desc_differentshape),
            navigationId = R.id.action_game_differentshape,
            drawableImgId = R.drawable.thumbnail_differentshape,
            tutorialUri = resources.getString(R.string.uri_mp4_tutorial_verbalfrenzy),
            tutorialText = "",
            isPremiumOnly = false
        ),
        GameData(
            gameId = ID_GAME_UNIQUEONE,
            category = ID_CATEGORY_FOCUS,
            subCategory = ID_SUBCATEGORY_COMPREHENSION,
            title = resources.getString(R.string.game_title_uniqueone),
            description = resources.getString(R.string.desc_uniqueone),
            navigationId = R.id.action_game_uniqueone,
            drawableImgId = R.drawable.thumbnail_uniqueone,
            tutorialUri = resources.getString(R.string.uri_mp4_tutorial_verbalfrenzy),
            tutorialText = "",
            isPremiumOnly = false
        ),
        GameData(
            gameId = ID_GAME_COLORMATCH,
            category = ID_CATEGORY_FOCUS,
            subCategory = ID_SUBCATEGORY_RESPONSE_CONTROL,
            title = resources.getString(R.string.game_title_colormatch),
            description = resources.getString(R.string.desc_colormatch),
            navigationId = R.id.action_game_colormatch,
            drawableImgId = R.drawable.thumbnail_colormatch,
            tutorialUri = resources.getString(R.string.uri_mp4_tutorial_verbalfrenzy),
            tutorialText = "",
            isPremiumOnly = false
        ),
        GameData(
            gameId = ID_GAME_SIMPLETROUBLE,
            category = ID_CATEGORY_LOGIC,
            subCategory = ID_SUBCATEGORY_THINKING_SPEED,
            title = resources.getString(R.string.game_title_simpletrouble),
            description = resources.getString(R.string.desc_simpletrouble),
            navigationId = R.id.action_game_simpletrouble,
            drawableImgId = R.drawable.thumbnail_taskswitch,
            tutorialUri = resources.getString(R.string.uri_mp4_tutorial_verbalfrenzy),
            tutorialText = "",
            isPremiumOnly = true
        ),
        GameData(
            gameId = ID_GAME_TEXTSWITCH,
            category = ID_CATEGORY_LOGIC,
            subCategory = ID_SUBCATEGORY_THINKING_SPEED,
            title = resources.getString(R.string.game_title_textswitch),
            description = resources.getString(R.string.desc_textswitch),
            navigationId = R.id.action_game_textswitch,
            drawableImgId = R.drawable.thumbnail_textswitch,
            tutorialUri = resources.getString(R.string.uri_mp4_tutorial_verbalfrenzy),
            tutorialText = "",
            isPremiumOnly = false
        ),
        GameData(
            gameId = ID_GAME_DAYOFWEEK,
            category = ID_CATEGORY_LOGIC,
            subCategory = ID_SUBCATEGORY_INFORMATION_PROCESSING,
            title = resources.getString(R.string.game_title_dayofweek),
            description = resources.getString(R.string.desc_dayofweek),
            navigationId = R.id.action_game_dayofweek,
            drawableImgId = R.drawable.thumbnail_dayofweek,
            tutorialUri = resources.getString(R.string.uri_mp4_tutorial_verbalfrenzy),
            tutorialText = "",
            isPremiumOnly = false
        ),
        GameData(
            gameId = ID_GAME_SEQUENCE,
            category = ID_CATEGORY_LOGIC,
            subCategory = ID_SUBCATEGORY_PATTERN_RECOGNITION,
            title = resources.getString(R.string.game_title_sequence),
            description = resources.getString(R.string.desc_sequence),
            navigationId = R.id.action_game_sequence,
            drawableImgId = R.drawable.thumbnail_sequence,
            tutorialUri = resources.getString(R.string.uri_mp4_tutorial_verbalfrenzy),
            tutorialText = "",
            isPremiumOnly = false
        ),
        GameData(
            gameId = ID_GAME_SPATIALARITHMETIC,
            category = ID_CATEGORY_SPATIAL,
            subCategory = ID_SUBCATEGORY_VISUALIZATION,
            title = resources.getString(R.string.game_title_spatialarithmetic),
            description = resources.getString(R.string.desc_spatialarithmetic),
            navigationId = R.id.action_game_spatialarithmetic,
            drawableImgId = R.drawable.thumbnail_spatialarithmetic,
            tutorialUri = resources.getString(R.string.uri_mp4_tutorial_verbalfrenzy),
            tutorialText = "",
            isPremiumOnly = true
        ),
        GameData(
            gameId = ID_GAME_FEATUREMATCH,
            category = ID_CATEGORY_SPATIAL,
            subCategory = ID_SUBCATEGORY_SPATIAL_AWARENESS,
            title = resources.getString(R.string.game_title_featurematch),
            description = resources.getString(R.string.desc_featurematch),
            navigationId = R.id.action_game_featurematch,
            drawableImgId = R.drawable.thumbnail_spatialmatch,
            tutorialUri = resources.getString(R.string.uri_mp4_tutorial_verbalfrenzy),
            tutorialText = "",
            isPremiumOnly = false
        ),
        GameData(
            gameId = ID_GAME_PARTIALSHAPE,
            category = ID_CATEGORY_SPATIAL,
            subCategory = ID_SUBCATEGORY_SPATIAL_ASSOCIATION,
            title = resources.getString(R.string.game_title_partialshape),
            description = resources.getString(R.string.desc_partialshape),
            navigationId = R.id.action_game_partialshape,
            drawableImgId = R.drawable.thumbnail_partialshape,
            tutorialUri = resources.getString(R.string.uri_mp4_tutorial_verbalfrenzy),
            tutorialText = "",
            isPremiumOnly = false
        ),
        GameData(
            gameId = ID_GAME_VISUALROTATION,
            category = ID_CATEGORY_SPATIAL,
            subCategory = ID_SUBCATEGORY_MENTAL_ROTATION,
            title = resources.getString(R.string.game_title_visualrotation),
            description = resources.getString(R.string.desc_visualrotation),
            navigationId = R.id.action_game_visualrotation,
            drawableImgId = R.drawable.thumbnail_rotationmatch,
            tutorialUri = resources.getString(R.string.uri_mp4_tutorial_verbalfrenzy),
            tutorialText = "",
            isPremiumOnly = false
        )
    )

    var trainItemData: Map<Int, List<GameData>>

    init {
        trainItemData = initTrainItemData()
        instance = this
    }

    private fun initTrainItemData(): MutableMap<Int, MutableList<GameData>> {
        // Initialize map categories
        val tempData: MutableMap<Int, MutableList<GameData>> = mutableMapOf()
        for (category in (0..4))
            tempData[category] = mutableListOf()
        // Filter games by category
        for (item in gameData)
            tempData[item.category]?.add(item)
        return tempData
    }
}
