package com.wemadefun.neurality.data.userdata.modules

import com.wemadefun.neurality.data.userdata.UserDataRepository
import com.wemadefun.neurality.utils.CONFIG_ANONYMOUS_UID
import javax.inject.Inject

class DataModule @Inject constructor(private val repository: UserDataRepository) {

    val isUserHasRatedApp: Boolean get() = repository.userData.isHasRated
    val isPremiumGamesEnabled: Boolean get() = repository.userData.isPremiumGamesEnabled
    val gamesPlayed: Int get() = repository.userData.playCount
    val uid: String get() = repository.userData.uid
    val registrationTime: Long get() = repository.userData.registrationTime
    val isAnonymous: Boolean get() = repository.userData.uid == CONFIG_ANONYMOUS_UID

}