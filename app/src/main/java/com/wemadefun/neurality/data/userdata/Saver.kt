package com.wemadefun.neurality.data.userdata

import com.wemadefun.neurality.data.userdata.modules.source.local.UserDataLocalDataSource
import com.wemadefun.neurality.data.userdata.modules.source.remote.Status
import com.wemadefun.neurality.data.userdata.modules.source.remote.UserDataRemoteDataSource
import com.wemadefun.neurality.utils.CONFIG_ANONYMOUS_UID
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Saver @Inject constructor(
    private val localDataSource: UserDataLocalDataSource,
    private val remoteDataSource: UserDataRemoteDataSource)
{
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun saveUserDataLocal(activeUserData: UserData) {
        FireCrashlytics.log("Save UserData Local.")

        withContext(ioDispatcher) {
            val toSaveUserData = checkDifferences(activeUserData)
            localDataSource.updateUserData(toSaveUserData.uid, toSaveUserData)
        }
    }

    suspend fun saveUserDataRemotely(activeUserData: UserData) {
        // Reject anonymous remote saves.
        if (activeUserData.uid == CONFIG_ANONYMOUS_UID) { return }

        FireCrashlytics.log("Save UserData Remote.")
        val toSaveUserData = checkDifferences(activeUserData)
        remoteDataSource.updateUserData(toSaveUserData.uid, toSaveUserData)
    }

    private suspend fun checkDifferences(activeUserData: UserData): UserData {
        // Reject anonymous checks.
        if (activeUserData.uid == CONFIG_ANONYMOUS_UID) { return activeUserData }

        return if (remoteDataSource.isUserDataExists(activeUserData.uid) == Status.EXISTS) {
            val remoteUserData = remoteDataSource.getUserData(activeUserData.uid)
            onModifyDifferences(activeUserData, remoteUserData)
        } else { activeUserData }
    }

    private fun onModifyDifferences(activeUserData: UserData, remoteUserData: UserData): UserData {
        when {
            (activeUserData.tempIap < remoteUserData.tempIap) -> activeUserData.tempIap = remoteUserData.tempIap
        }
        return activeUserData
    }
}