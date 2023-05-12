package com.wemadefun.neurality.utils
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wemadefun.neurality.R
import com.wemadefun.neurality.data.userdata.modules.IapModule
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import java.lang.reflect.Type

const val ARG_GAME_DATA = "argGameData"
val currentTime get() = System.currentTimeMillis()

fun getCategoryString(id: Int, resources: Resources): String {
    return when (id) {
        ID_CATEGORY_MEMORY -> resources.getString(R.string.category_memory)
        ID_CATEGORY_CALCULATION -> resources.getString(R.string.category_calculation)
        ID_CATEGORY_FOCUS -> resources.getString(R.string.category_focus)
        ID_CATEGORY_LOGIC -> resources.getString(R.string.category_logic)
        ID_CATEGORY_SPATIAL -> resources.getString(R.string.category_spatial)
        else -> "Error getting category string."
    }
}

fun getSubcategoryString(id: Int, resources: Resources): String {
    return when (id) {
        ID_SUBCATEGORY_VERBAL_MEMORY -> resources.getString(R.string.subcategory_verbal_memory)
        ID_SUBCATEGORY_NUMERICAL_ESTIMATION -> resources.getString(R.string.subcategory_numerical_estimation)
        ID_SUBCATEGORY_MENTAL_AGILITY -> resources.getString(R.string.subcategory_mental_agility)
        ID_SUBCATEGORY_THINKING_SPEED -> resources.getString(R.string.subcategory_thinking_speed)
        ID_SUBCATEGORY_SPATIAL_AWARENESS -> resources.getString(R.string.subcategory_spatial_awareness)
        ID_SUBCATEGORY_SHORTTERM_MEMORY -> resources.getString(R.string.subcategory_shortterm_memory)
        ID_SUBCATEGORY_WORKING_MEMORY -> resources.getString(R.string.subcategory_working_memory)
        ID_SUBCATEGORY_VISUAL_MEMORY -> resources.getString(R.string.subcategory_visual_memory)
        ID_SUBCATEGORY_CALCULATION_ACCURACY -> resources.getString(R.string.subcategory_calculation_accuracy)
        ID_SUBCATEGORY_NUMERICAL_PROCESSING -> resources.getString(R.string.subcategory_numerical_processing)
        ID_SUBCATEGORY_RESPONSE_CONTROL -> resources.getString(R.string.subcategory_response_control)
        ID_SUBCATEGORY_FOCUS_SPEED -> resources.getString(R.string.subcategory_focus_speed)
        ID_SUBCATEGORY_COMPREHENSION -> resources.getString(R.string.subcategory_comprehension)
        ID_SUBCATEGORY_INFORMATION_PROCESSING -> resources.getString(R.string.subcategory_information_processing)
        ID_SUBCATEGORY_PATTERN_RECOGNITION -> resources.getString(R.string.subcategory_pattern_recognition)
        ID_SUBCATEGORY_MENTAL_ROTATION -> resources.getString(R.string.subcategory_mental_rotation)
        ID_SUBCATEGORY_SPATIAL_ASSOCIATION -> resources.getString(R.string.subcategory_spatial_association)
        ID_SUBCATEGORY_VISUALIZATION -> resources.getString(R.string.subcategory_visualization)
        else -> "Error getting subcategory string."
    }
}

fun getGameName(id: Int): String {
    return when (id) {
        ID_GAME_VERBALFRENZY -> "memory_wordfrenzy"
        ID_GAME_MARTIALARTS -> "memory_martialarts"
        ID_GAME_DIGITSPAN -> "memory_digitspan"
        ID_GAME_NUMBERRECALL -> "memory_numberrecall"
        ID_GAME_HIGHESTNUMBER -> "calculation_highestnumber"
        ID_GAME_SALARY -> "calculation_salary"
        ID_GAME_TRUEFALSE -> "calculation_truefalse"
        ID_GAME_EXACTVALUE -> "calculation_exactvalue"
        ID_GAME_DIFFERENTSHAPE -> "focus_shapematch"
        ID_GAME_COLORMATCH -> "focus_colormatch"
        ID_GAME_UNIQUEONE -> "focus_uniqueone"
        ID_GAME_OBJECTFINDER -> "focus_objectfinder"
        ID_GAME_SIMPLETROUBLE -> "logic_taskswitch"
        ID_GAME_TEXTSWITCH -> "logic_textswitch"
        ID_GAME_DAYOFWEEK -> "logic_dayofweek"
        ID_GAME_SEQUENCE -> "logic_sequenceproblem"
        ID_GAME_FEATUREMATCH -> "spatial_featurematch"
        ID_GAME_SPATIALARITHMETIC -> "spatial_spatialarithmetic"
        ID_GAME_PARTIALSHAPE -> "spatial_partialmatch"
        ID_GAME_VISUALROTATION -> "spatial_visualrotation"
        else -> "unknown_game"
    }
}


fun getCategoryName(id: Int): String {
    return when (id) {
        ID_CATEGORY_MEMORY -> "category_memory"
        ID_CATEGORY_CALCULATION -> "category_calculation"
        ID_CATEGORY_FOCUS -> "category_focus"
        ID_CATEGORY_LOGIC -> "category_logic"
        ID_CATEGORY_SPATIAL -> "category_spatial"
        else -> "unknown_category"
    }
}

fun jsonStringToHashMap(str: String): HashMap<String, Any> {
    var returnMap = hashMapOf<String, Any>()
    try {
        val type: Type = object : TypeToken<HashMap<String, Any>>(){}.type
        returnMap = Gson().fromJson(str, type)
    } catch (e: Exception) { }
    return returnMap
}

fun navigateWithGameData(navId: Int, gameData: GameProvider.GameData, navController: NavController) {
    val args = bundleOf(ARG_GAME_DATA to gameData)
    navController.navigate(navId, args)
}

fun navigateToGame(gameData: GameProvider.GameData, navController: NavController) {
    navigateWithGameData(R.id.action_get_ready, gameData, navController)
}

fun loggyDebug(text: Any) {
    FireCrashlytics.log(text.toString())
    Log.d("loggy", text.toString())
}

fun apptraceError(text: String) {
    Log.e("apptraceError", text)
}

fun appTrace(text: String) {
//    Log.d("apptrace", text)
}

fun isIapMode(): Boolean {
    return IapModule.IS_IAP_ENABLED
}

fun _glideLoadImageAlt(context: Context, imageId: Int, into: ImageView) {
    try {
        Glide.with(context).load(imageId).into(into)
    } catch (e: Exception) {
        FireCrashlytics.report(e)
    }
}

fun _glideLoadImage(imageId: Int, into: ImageView) {
    try {
        Glide.with(into.context).load(imageId).into(into)
    } catch (e: Exception) {
        FireCrashlytics.report(e)
    }
}

fun _glideLoadImage(imageId: Drawable, into: ImageView) {
    try {
        Glide.with(into.context).load(imageId).into(into)
    } catch (e: Exception) {
        FireCrashlytics.report(e)
    }
}