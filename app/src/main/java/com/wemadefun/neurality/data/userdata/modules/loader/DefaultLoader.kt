package com.wemadefun.neurality.data.userdata.modules.loader

import com.wemadefun.neurality.data.userdata.UserData
import com.wemadefun.neurality.data.userdata.UserDataRepository
import com.wemadefun.neurality.data.userdata.modules.loader.utils.FreshUserDataFactory
import com.wemadefun.neurality.data.userdata.modules.source.local.UserDataLocalDataSource
import com.wemadefun.neurality.data.userdata.modules.source.remote.Status
import com.wemadefun.neurality.data.userdata.modules.source.remote.UserDataRemoteDataSource
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultLoader @Inject constructor(
    private val localDataSource: UserDataLocalDataSource,
    private val remoteDataSource: UserDataRemoteDataSource,
    private val repository: UserDataRepository
) : FreshUserDataFactory()
{
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    /**
     * Only callable at SignInViewModel!
     */
    suspend fun load(uid: String): Boolean {
        return withContext(ioDispatcher) {
            val userData: UserData? = loadUserData(uid)
            if (userData != null) { onUserDataOk(userData) }
            return@withContext userData != null
        }
    }

    private fun onUserDataOk(userData: UserData) {
        val formattedUserData = getNewestFormatUserData(userData)
        repository.load(formattedUserData)
        FireCrashlytics.log("[Logged In Info] " +
                "Logged in email: ${userData.email} | " +
                "Games played: ${userData.playCount}| ")
    }

    private suspend fun loadUserData(uid: String): UserData? {
        return if (isLocalUserDataExists(uid)) {
            onLocalUserDataExists(uid)
        }
        else {
            onLocalUserDataNotExists(uid)
        }
    }

    private suspend fun onLocalUserDataExists(uid: String): UserData {
        when (remoteDataSource.isUserDataExists(uid)) {
            Status.EXISTS -> {
                val remoteUserData = remoteDataSource.getUserData(uid)
                val localUserData = localDataSource.getUserData(uid)
                // Prioritize UserData Load by Play Count.
                return if (remoteUserData.playCount >= localUserData.playCount) {
                    FireCrashlytics.log("Using Remote UserData: Remote playCount is higher than local.")
                    remoteUserData
                } else {
                    FireCrashlytics.log("Using Local UserData: Local playCount is higher than remote.")
                    localUserData
                }
            }
            Status.NOT_EXISTS -> {
                FireCrashlytics.log("Using Local UserData: Remote UserData does not exist.")
                return localDataSource.getUserData(uid)
            }
            Status.ERROR -> {
                FireCrashlytics.log("Using Local UserData: Remote UserData ERROR.")
                return localDataSource.getUserData(uid)
            }
            else -> {
                FireCrashlytics.log("NOT Using Any UserData: Data is still fetching. WTF is this?")
                return localDataSource.getUserData(uid)
            }
        }
    }

    private suspend fun onLocalUserDataNotExists(uid: String): UserData? {
        return when (remoteDataSource.isUserDataExists(uid)) {
            Status.EXISTS -> {
                FireCrashlytics.log("Using Remote UserData: Local UserData does not exist.")
                remoteDataSource.getUserData(uid)
            }
            Status.NOT_EXISTS -> {
                FireCrashlytics.log("Using Fresh Local UserData: Remote and Local UserData does not exist.")
                getFreshUserData(uid)
            }
            Status.ERROR -> {
                FireCrashlytics.log("NOT Using Any UserData: Remote and Local UserData does not exist. " +
                        "PROMPT INTERNET ERROR.")
                null
            }
            else -> {
                FireCrashlytics.log("NOT Using Any UserData: Data is still fetching. WTF is this?")
                null
            }
        }
    }

    private suspend fun isLocalUserDataExists(uid: String): Boolean {
        return localDataSource.isUserDataExists(uid) == Status.EXISTS
    }

}