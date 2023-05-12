package com.wemadefun.neurality.data.userdata.modules

import com.wemadefun.neurality.data.userdata.UserDataRepository
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import javax.inject.Inject

class ConfigModule @Inject constructor(private val repository: UserDataRepository) {

    fun onEnablePremiumOnlyGames() {
        repository.ifActiveThenSaveAll {
            FireCrashlytics.log("Enabling Premium Only Games.")
            it.isPremiumGamesEnabled = true
        }
    }

    fun onUserHasRated() {
        repository.ifActiveThenSaveAll {
            FireCrashlytics.log("User has just rated.")
            it.isHasRated = true
        }
    }

}