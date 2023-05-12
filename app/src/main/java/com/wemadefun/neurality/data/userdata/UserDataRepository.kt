package com.wemadefun.neurality.data.userdata

import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserDataRepository @Inject constructor(private val saver: Saver)
{
    private lateinit var activeUserData: UserData

    private val job = Job()
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + job)
    private val isActive: Boolean get() = this::activeUserData.isInitialized

    val userData: UserData get() = activeUserData

    fun load(userData: UserData) {
        activeUserData = userData
        saveLocalAndRemote()
    }

    fun saveLocalAndRemote() {
        if (!isActive) { return }
        scope.launch {
            FireCrashlytics.log("Save UserData All called.")
            saver.saveUserDataLocal(activeUserData)
            saver.saveUserDataRemotely(activeUserData)
        }
    }

    fun saveLocal() {
        if (!isActive) { return }
        scope.launch {
            saver.saveUserDataLocal(activeUserData)
        }
    }

    fun saveRemote() {
        if (!isActive) { return }
        scope.launch {
            saver.saveUserDataRemotely(activeUserData)
        }
    }

    fun ifActive(func: (UserData) -> Unit) {
        if (!isActive) { return }
        func(activeUserData)
    }

    fun ifActiveThenSaveLocal(func: (UserData) -> Unit) {
        if (!isActive) { return }
        func(activeUserData)
        saveLocal()
    }

    fun ifActiveThenSaveAll(func: (UserData) -> Unit) {
        if (!isActive) { return }
        func(activeUserData)
        saveLocalAndRemote()
    }
}