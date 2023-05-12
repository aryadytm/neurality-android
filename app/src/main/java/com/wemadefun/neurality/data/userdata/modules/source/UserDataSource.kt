package com.wemadefun.neurality.data.userdata.modules.source

import com.wemadefun.neurality.data.userdata.UserData
import com.wemadefun.neurality.data.userdata.modules.source.remote.Status

interface UserDataSource {

    suspend fun getUserData(uid: String): UserData?

    suspend fun isUserDataExists(uid: String): Status

    suspend fun updateUserData(uid: String, userData: UserData)


}