package com.wemadefun.neurality.data.userdata.modules

import com.wemadefun.neurality.data.userdata.UserDataRepository
import javax.inject.Inject

class SaverModule @Inject constructor(private val repository: UserDataRepository) {

    fun saveLocalAndRemote() {
        repository.saveLocalAndRemote()
    }

    fun saveLocal() {
        repository.saveLocal()
    }

    fun saveRemote() {
        repository.saveRemote()
    }

}