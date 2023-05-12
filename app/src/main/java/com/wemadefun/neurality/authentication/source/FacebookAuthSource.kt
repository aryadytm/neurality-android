package com.wemadefun.neurality.authentication.source

import android.content.Intent
import com.facebook.login.LoginManager
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.wemadefun.neurality.authentication.Authentication
import com.wemadefun.neurality.ui.signin.SignInFragment
import javax.inject.Inject

class FacebookAuthSource @Inject constructor() : AuthSource {


    override fun authenticate(fragment: SignInFragment, credential: AuthCredential) {
        Authentication.auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    fragment.onSignInSuccess()
                }
            }
            .addOnFailureListener {
                if (it is FirebaseAuthUserCollisionException) {
                    fragment.onDuplicateEmailError()
                } else {
                    fragment.onSignInError(it)
                }
            }
    }

    override fun deauthenticate() {
        LoginManager.getInstance().logOut()
    }

    override fun getSignInIntent(): Intent {
        TODO("Not yet implemented")
    }


}
