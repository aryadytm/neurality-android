package com.wemadefun.neurality.data.userdata.modules.loader

import com.wemadefun.neurality.data.userdata.UserDataRepository
import com.wemadefun.neurality.data.userdata.modules.loader.utils.FreshUserDataFactory
import com.wemadefun.neurality.data.userdata.modules.source.local.UserDataLocalDataSource
import com.wemadefun.neurality.data.userdata.modules.source.remote.Status
import com.wemadefun.neurality.firebaseutils.firedynamiclinks.DynamicLinksReceiver
import com.wemadefun.neurality.utils.CONFIG_ANONYMOUS_UID
import javax.inject.Inject

class AnonymousLoader @Inject constructor(
    private val localDataSource: UserDataLocalDataSource,
    private val repository: UserDataRepository) : FreshUserDataFactory() {

    suspend fun load() {
        val isExists = localDataSource.isUserDataExists(CONFIG_ANONYMOUS_UID) == Status.EXISTS

        if (isExists) {
            repository.load(localDataSource.getUserData(CONFIG_ANONYMOUS_UID))
        } else {
            val newAnonUserData = getFreshUserData(CONFIG_ANONYMOUS_UID)
            // Check if user is referred or not. If yes, tag the referrer.
            if (DynamicLinksReceiver.isReferredBySomeone()) {
                newAnonUserData.referredBy = DynamicLinksReceiver.referrer
            }
            repository.load(newAnonUserData)
            repository.saveLocal()
        }
    }

}