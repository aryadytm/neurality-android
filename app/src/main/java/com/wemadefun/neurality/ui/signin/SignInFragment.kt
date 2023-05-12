package com.wemadefun.neurality.ui.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import com.wemadefun.neurality.R
import com.wemadefun.neurality.authentication.AUTH_PROVIDER_GOOGLE
import com.wemadefun.neurality.authentication.Authentication
import com.wemadefun.neurality.databinding.FragmentSignInBinding
import com.wemadefun.neurality.ext.easyToastLong
import com.wemadefun.neurality.ext.openBrowser
import com.wemadefun.neurality.firebaseutils.FireAnalytics
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import com.wemadefun.neurality.firebaseutils.firedynamiclinks.DynamicLinksReceiver
import com.wemadefun.neurality.firebaseutils.fireremote.FireRemote
import com.wemadefun.neurality.ui.dialogs.AgeDialog
import com.wemadefun.neurality.ui.dialogs.AnonymousSigninDialog
import com.wemadefun.neurality.ui.dialogs.ExitDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInFragment : SignInFlowFragment() {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var signInModel: SignInModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        observeFetchUserData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        FireCrashlytics.log("Navigate to SignInFragment")
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        requireActivity().onBackPressedDispatcher.addCallback(this)
        {
            ExitDialog().show(requireActivity().supportFragmentManager, "DIALOG_EXIT")
        }
        onCheckAuthentication()
        return binding.root
    }

    private fun onCheckAuthentication() {
        val isAuthenticated = Authentication.isAuthenticated() && signInUtils.isAgeSelected()

        if (isAuthenticated) {
            onSetAuthenticatedUi()
            CoroutineScope(Dispatchers.Main).launch {
                showLoadingDialog = false
                onAuthenticated()
            }
        } else {
            signInModel = SignInModel(this)
            authenticator.signOut()
            onSetUnauthenticatedUi()
        }
    }

    private fun onSetAuthenticatedUi() {
        binding.unauthenticated.visibility = View.GONE
        binding.authenticated.visibility = View.VISIBLE
    }

    private fun onSetUnauthenticatedUi() {
        observeSignInButton()
        binding.unauthenticated.visibility = View.VISIBLE
        binding.authenticated.visibility = View.GONE
        binding.textBottom.setOnClickListener { openBrowser(FireRemote.stringUrlLegal) }
        binding.btnLater.setOnClickListener { onUseAnonymousAccount() }

        when (signInUtils.getUserAboveEighteen()) {
            SignInUtils.AgeStatus.NULL -> showAgeDialog()
            SignInUtils.AgeStatus.ABOVE_EIGHTEEN -> binding.btnLater.visibility = View.GONE
            SignInUtils.AgeStatus.BELOW_EIGHTEEN -> binding.btnLater.visibility = View.VISIBLE
        }
    }

    private fun onGoogleButtonClick() {
        val intent = authenticator.getSignInIntent(AUTH_PROVIDER_GOOGLE)
        startActivityForResult(intent, RC_SIGN_IN_GOOGLE)
    }

    private fun onFacebookButtonClick() {
        signInModel.onStartFacebookSignIn()
    }

    private fun onUseAnonymousAccount() {
        val anonDialog = AnonymousSigninDialog()
        anonDialog.setOnPositiveClick { viewModel.onUseAnonymousAccount() }
        anonDialog.show(requireActivity().supportFragmentManager, "AnonymousSigninDalog")
    }

    private fun observeSignInButton() {
        binding.buttonSignIn.setOnClickListener { onGoogleButtonClick() }
        binding.buttonFacebook.setOnClickListener { onFacebookButtonClick() }
    }

    private fun observeFetchUserData() {
        viewModel.eventFetchingUserData.observe(this, Observer {
            when (it) {
                true -> onFetchUserData()
                false -> onFetchUserDataFinish()
            }
        })
        viewModel.eventFetchUserDataSuccess.observe(this, Observer {
            when (it) {
                true -> onFetchUserDataSuccess()
            }
        })
        viewModel.eventFetchUserDataError.observe(this, Observer {
            when (it) {
                true -> onFetchUserDataError { onSetUnauthenticatedUi() }
            }
        })
        viewModel.eventReferralInvalid.observe(this, Observer {
            when (it) {
                true -> {
                    easyToastLong(getString(R.string.signin_referral_invalid))
                    viewModel.onReferralInvalidDelivered()
                }
            }
        })
        viewModel.eventReferralValid.observe(this, Observer {
            when (it) {
                true -> {
                    easyToastLong(getString(R.string.signin_referral_valid))
                    viewModel.onReferralValidDelivered()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        onSignIn()
        signInModel.onSignInActivityResult(requestCode, resultCode, data)
    }

    private fun showAgeDialog() {
        val ageDialog = AgeDialog()
        ageDialog.isCancelable = false

        ageDialog.setOnAgeOverEighteen {
            binding.btnLater.visibility = View.GONE
            signInUtils.setUserAboveEighteen(true)
            FireAnalytics.eventEnterAge(true)

            if (DynamicLinksReceiver.isReferredBySomeone()) {
                easyToastLong(getString(R.string.signin_referral_using_invitation))
            }
        }
        ageDialog.setOnAgeUnderEighteen {
            binding.btnLater.visibility = View.VISIBLE
            signInUtils.setUserAboveEighteen(false)
            FireAnalytics.eventEnterAge(false)

            if (DynamicLinksReceiver.isReferredBySomeone()) {
                easyToastLong(getString(R.string.signin_referral_using_invitation))
            }
        }
        ageDialog.show(requireActivity().supportFragmentManager, "SignInAgeDialog")
    }

}
