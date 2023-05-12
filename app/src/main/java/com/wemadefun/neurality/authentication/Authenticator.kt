package com.wemadefun.neurality.authentication

import android.content.Intent
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.wemadefun.neurality.authentication.source.FacebookAuthSource
import com.wemadefun.neurality.authentication.source.GoogleAuthSource
import com.wemadefun.neurality.data.userdata.modules.SignOutModule
import com.wemadefun.neurality.data.userdata.modules.energy.EnergyModule
import com.wemadefun.neurality.firebaseutils.firedynamiclinks.DynamicLinksCreator
import com.wemadefun.neurality.ui.signin.SignInFragment
import com.wemadefun.neurality.firebaseutils.FireAnalytics
import javax.inject.Inject

class Authenticator @Inject constructor(
    private val googleAuthSource: GoogleAuthSource,
    private val facebookAuthSource: FacebookAuthSource,
    private val signOutModule: SignOutModule,
    private val energyModule: EnergyModule
) {
    fun signOut() {
        energyModule.onDestroy()
        signOutModule.signOut()
        DynamicLinksCreator.destroyMyLink()
        Authentication.auth.signOut()
        googleAuthSource.deauthenticate()
        facebookAuthSource.deauthenticate()
    }

    fun getSignInIntent(authSource: String): Intent {
        return when (authSource) {
            AUTH_PROVIDER_GOOGLE -> googleAuthSource.getSignInIntent()
            AUTH_PROVIDER_FACEBOOK -> TODO("Facebook Sign Out")
            AUTH_PROVIDER_TWITTER -> TODO("Twitter Sign Out")
            else -> throw Exception("Illegal sign in provider!")
        }
    }

    fun signInWithGoogle(acct: GoogleSignInAccount, fragment: SignInFragment) {
        FireAnalytics.eventSignIn(AUTH_PROVIDER_GOOGLE)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        googleAuthSource.authenticate(fragment, credential)
    }

    fun signInWithFacebook(accessToken: AccessToken, fragment: SignInFragment) {
        FireAnalytics.eventSignIn(AUTH_PROVIDER_FACEBOOK)
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        facebookAuthSource.authenticate(fragment, credential)
    }
}



