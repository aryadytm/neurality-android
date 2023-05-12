package com.wemadefun.neurality.authentication.source

import android.content.Intent
import com.wemadefun.neurality.ui.signin.SignInFragment
import com.google.firebase.auth.AuthCredential

interface AuthSource {

    fun authenticate(fragment: SignInFragment, credential: AuthCredential)

    fun deauthenticate()

    fun getSignInIntent(): Intent

}