package com.wemadefun.neurality.data.userdata.modules

import com.wemadefun.neurality.data.userdata.UserDataRepository
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import javax.inject.Inject

class SignOutModule @Inject constructor(private val repository: UserDataRepository) {

    fun signOut() {
        repository.ifActiveThenSaveAll {
            FireCrashlytics.log("User Sign Out")
        }
    }

}