package com.wemadefun.neurality.data.userdata.modules.loader

import android.content.Context
import com.wemadefun.neurality.R
import com.wemadefun.neurality.data.userdata.UserDataRepository
import com.wemadefun.neurality.data.userdata.modules.loader.utils.FreshUserDataFactory
import com.wemadefun.neurality.data.userdata.modules.source.local.UserDataLocalDataSource
import com.wemadefun.neurality.data.userdata.modules.source.remote.Status
import com.wemadefun.neurality.data.userdata.modules.source.remote.UserDataRemoteDataSource
import com.wemadefun.neurality.utils.CONFIG_REFERRER_REWARD_TIME
import com.wemadefun.neurality.utils.currentTime
import java.io.File
import javax.inject.Inject

class ReferralLoader @Inject constructor(
    private val localDataSource: UserDataLocalDataSource,
    private val remoteDataSource: UserDataRemoteDataSource,
    private val repository: UserDataRepository,
    context: Context) : FreshUserDataFactory()
{
    private val prefDir = context.applicationContext.applicationInfo.dataDir +
            "/shared_prefs/" + context.getString(R.string.file_pref_userdata) + ".xml"

    suspend fun isFirstTime(uid: String): Boolean {
        if (localDataSource.isUserDataExists(uid) != Status.NOT_EXISTS) { return false }
        if (remoteDataSource.isUserDataExists(uid) != Status.NOT_EXISTS) { return false }
        return true
    }

    fun load(uid: String, referrer: String) {
        // Creates a free 3-day premium then load it to UserData.
        val refUserData = getFreshUserData(uid)
        refUserData.tempIap = currentTime + CONFIG_REFERRER_REWARD_TIME
        refUserData.referredBy = referrer
        repository.load(refUserData)
        repository.saveLocalAndRemote()
    }

    private fun isPrefExists(): Boolean {
        val file = File(prefDir)
        return file.exists()
    }
}