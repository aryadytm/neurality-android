package com.wemadefun.neurality.firebaseutils

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.wemadefun.neurality.authentication.Authentication
import com.wemadefun.neurality.utils.appTrace
import com.wemadefun.neurality.utils.apptraceError

object FireCrashlytics {

    private val firebaseCrashlytics = FirebaseCrashlytics.getInstance()

    fun log(msg: String) {
        if (Authentication.isAuthenticated()) { firebaseCrashlytics.setUserId(Authentication.getAuthUid()) }
        appTrace(msg)
        firebaseCrashlytics.log(msg)
    }

    fun report(error: Exception) {
        log(error.toString())
        apptraceError(error.toString())
        firebaseCrashlytics.recordException(error)
    }

}