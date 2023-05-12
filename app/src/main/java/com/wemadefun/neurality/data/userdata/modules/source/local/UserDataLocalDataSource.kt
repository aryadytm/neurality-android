package com.wemadefun.neurality.data.userdata.modules.source.local

import com.wemadefun.neurality.data.userdata.modules.source.UserPreferencesWrapper
import com.wemadefun.neurality.data.userdata.UserData
import com.wemadefun.neurality.data.userdata.modules.source.UserDataProcessor
import com.wemadefun.neurality.data.userdata.modules.source.UserDataSource
import com.wemadefun.neurality.data.userdata.modules.source.remote.Status
import javax.inject.Inject

class UserDataLocalDataSource @Inject constructor(
    private val userPreferences: UserPreferencesWrapper
) : UserDataSource
{
    override suspend fun getUserData(uid: String): UserData {
        val userDataJson = userPreferences.getString(uid, "")
        return UserDataProcessor.getUserDataFromJson(userDataJson)
    }

    override suspend fun updateUserData(uid: String, userData: UserData) {
        val userDataJson = UserDataProcessor.getUserDataJson(userData)
        userPreferences.putString(uid, userDataJson)
    }

    override suspend fun isUserDataExists(uid: String): Status {
        return when (userPreferences.isKeyExists(uid)) {
            true -> Status.EXISTS
            false -> Status.NOT_EXISTS
        }
    }

}