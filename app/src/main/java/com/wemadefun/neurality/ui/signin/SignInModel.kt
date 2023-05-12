package com.wemadefun.neurality.ui.signin

import android.content.Intent
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

const val RC_SIGN_IN_GOOGLE = 1
const val RC_SIGN_IN_FACEBOOK = 2
const val RC_SIGN_IN_TWITTER = 3

class SignInModel(private val signInFragment: SignInFragment) {

    private lateinit var facebookCallbackManager: CallbackManager

    fun onStartFacebookSignIn() {
        facebookCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(signInFragment, setOf("email"))
        LoginManager.getInstance().registerCallback(facebookCallbackManager, object : FacebookCallback<LoginResult> {

            override fun onSuccess(result: LoginResult?) {
                result?.let { signInFragment.authenticator.signInWithFacebook(it.accessToken, signInFragment) }
            }
            override fun onCancel() {
                signInFragment.onDestroyLoadingDialog()
            }
            override fun onError(error: FacebookException) {
                signInFragment.onSignInError(error)
                Toast.makeText(signInFragment.requireContext(), "Facebook login error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun onSignInActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RC_SIGN_IN_GOOGLE -> onGoogleSignInActivityResult(data)
            else -> onFacebookActivityResult(requestCode, resultCode, data)
        }
    }

    private fun onGoogleSignInActivityResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)
            signInFragment.authenticator.signInWithGoogle(account!!, signInFragment)
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            if (e.statusCode.toString() == "12501") {
                signInFragment.onDestroyLoadingDialog()
                return
            }
            signInFragment.onSignInError(e)
            Toast.makeText(signInFragment.requireContext(), "Google login error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onFacebookActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data)
    }

}