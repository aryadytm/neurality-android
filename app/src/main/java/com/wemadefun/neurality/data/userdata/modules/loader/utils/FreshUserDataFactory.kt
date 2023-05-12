package com.wemadefun.neurality.data.userdata.modules.loader.utils

import com.wemadefun.neurality.authentication.Authentication
import com.wemadefun.neurality.data.userdata.UserData
import com.wemadefun.neurality.data.userdata.modules.source.UserDataProcessor
import com.wemadefun.neurality.utils.CONFIG_ENERGY_MAX
import com.wemadefun.neurality.firebaseutils.FireCrashlytics

abstract class FreshUserDataFactory {

    /**
     * Returns template UserData object that contains zero scores for every game.
     */
    protected fun getFreshUserData(uid: String): UserData {
        val email = Authentication.getAuthEmail()
        val initNeuralityScores = hashMapOf<String, Int>()
        val initGameScores = hashMapOf<String, MutableList<Int>>()

        for (category in (0..10)) { initNeuralityScores[category.toString()] = 0 }
        for (gameId in (0..100)) { initGameScores[gameId.toString()] = mutableListOf(0,0,0,0,0) }

        FireCrashlytics.log("New User: ${email}. Initializing new UserData...")

        return UserData(
            email = email,
            gameScores = initGameScores,
            neuralityScores = initNeuralityScores,
            uid = uid,
            provider = Authentication.getAuthProvider(),
            energy = CONFIG_ENERGY_MAX,
            energyLastAddTime = System.currentTimeMillis(),
            registrationTime = System.currentTimeMillis(),
            isHasRated = false,
            isSynced = true,
            isPremiumGamesEnabled = false,
            playCount = 0,
            tempIap = 0,
            iapToken = "",
            iapSku = "",
            referredBy = ""
        )
    }

    protected fun getNewestFormatUserData(userData: UserData): UserData {
        val newestUserDataJson = UserDataProcessor.getUserDataJson(getFreshUserData(userData.uid))
        val newestUserDataHashMap = UserDataProcessor.getHashMapFromJson(newestUserDataJson)
        val newestUserDataKeys = newestUserDataHashMap.keys.toList()
        val currentUserDataJson = UserDataProcessor.getUserDataJson(userData)
        val currentUserDataHashMap = UserDataProcessor.getHashMapFromJson(currentUserDataJson) as HashMap<String, Any?>
        val currentUserDataKeys = currentUserDataHashMap.keys.toList()

        val isIdentic = (newestUserDataKeys == currentUserDataKeys)

        if (!isIdentic) {
            val keysToAdd = newestUserDataKeys.subtract(currentUserDataKeys).toList()
            for (newKey in keysToAdd) {
                currentUserDataHashMap[newKey] = newestUserDataHashMap[newKey]
            }
            val newJson = UserDataProcessor.getUserDataJson(currentUserDataHashMap as HashMap<String, Any>)
            return UserDataProcessor.getUserDataFromJson(newJson)
        }

        return userData
    }

}