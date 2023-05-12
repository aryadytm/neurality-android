package com.wemadefun.neurality.authentication

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

const val AUTH_PROVIDER_GOOGLE = "google.com"
const val AUTH_PROVIDER_FACEBOOK = "facebook.com"
const val AUTH_PROVIDER_TWITTER = "twitter.com"

object Authentication {

    val auth = Firebase.auth

    fun getAuthEmail(): String {
        val email = auth.currentUser?.email
        email?.let { return it }
        return "empty"
    }

    fun getAuthProvider(): String {
        var provider = "empty"
        auth.currentUser?.let {
            for (user in it.providerData) { provider = user.providerId }
        }
        return provider
    }

    fun getAuthUid(): String {
        return auth.currentUser!!.uid
    }

    fun getAuthName(): String {
        var userName = "Neurality user"
        auth.currentUser?.let { userName = it.displayName.toString() }
        return userName
    }

    fun isAuthenticated(): Boolean {
        if (auth.currentUser?.isAnonymous == true) { return false }
        return (auth.currentUser != null) && (auth.currentUser?.uid != null)
    }
}

