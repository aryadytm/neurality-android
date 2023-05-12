package com.wemadefun.neurality.authentication.source

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.wemadefun.neurality.R
import com.wemadefun.neurality.authentication.Authentication
import com.wemadefun.neurality.ui.signin.SignInFragment
import javax.inject.Inject

class GoogleAuthSource @Inject constructor(activity: Activity) : AuthSource {
    private var googleSignInClient: GoogleSignInClient

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.resources.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

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
        googleSignInClient.signOut()
    }

    override fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

}