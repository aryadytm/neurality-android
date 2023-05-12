package com.wemadefun.neurality.ui.signin

import android.content.Context
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wemadefun.neurality.BrainApplication
import com.wemadefun.neurality.R
import com.wemadefun.neurality.authentication.Authenticator
import com.wemadefun.neurality.data.iapdata.IapDataRepository
import com.wemadefun.neurality.ext.easyToastShort
import com.wemadefun.neurality.ext.getConnectionErrorDialog
import com.wemadefun.neurality.ext.getLoadingDialog
import com.wemadefun.neurality.firebaseutils.FireAnalytics
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import com.wemadefun.neurality.firebaseutils.firedynamiclinks.DynamicLinksReceiver
import com.wemadefun.neurality.ui.dialogs.LoadingDialog
import com.wemadefun.neurality.utils.isIapMode
import javax.inject.Inject

abstract class SignInFlowFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var authenticator: Authenticator
    @Inject lateinit var billing: IapDataRepository
    @Inject lateinit var signInUtils: SignInUtils

    protected lateinit var viewModel: SignInViewModel
    protected var showLoadingDialog = true
    private var loadingDialog: LoadingDialog? = null
    private var isDialogShown = false

    @CallSuper
    override fun onAttach(context: Context) {
        (requireActivity().application as BrainApplication).appComponent.inject(this)
        super.onAttach(context)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SignInViewModel::class.java)
    }

    /**
     * Sign In Flow:
     * onSignIn -> onSignInSuccess -> onAuthenticated -> onFetchUserData -> onFetchUserDataFinish -> onFetchUserDataSuccess
     *     |                                  |                                        |
     *     V                       ( vm.onFetchUserData )                              V
     * onSignInError                                                         onFetchUserDataError
     */

    protected fun onSignIn() {
        showLoadingDialog(getString(R.string.signing_in))
        FireCrashlytics.log("Signing In...")
    }

    fun onSignInSuccess() {
        onDestroyLoadingDialog()
        onAuthenticated()
        FireCrashlytics.log("Signed In.")
    }

    protected fun onAuthenticated() {
        viewModel.onFetchUserData()
        FireCrashlytics.log("Authenticated.")
    }

    protected fun onFetchUserData() {
        if (showLoadingDialog) { showLoadingDialog(getString(R.string.syncing_neurality)) }
        FireCrashlytics.log("Fetching UserData...")
    }

    protected fun onFetchUserDataFinish() {
        onDestroyLoadingDialog()
        FireCrashlytics.log("Fetching UserData Finished.")
    }

    protected fun onFetchUserDataSuccess() {
        billing.queryPurchases()
        viewModel.onFetchUserDataSuccessDelivered()
        viewModel.onCreateDynamicLink(requireContext())
        if (isIapMode()) { easyToastShort(getString(R.string.signin_welcome_premium)) }
        findNavController().navigate(R.id.action_nav_sign_in_to_nav_train)
        FireAnalytics.onAuthenticated()
        DynamicLinksReceiver.destroyReferrer()
        FireCrashlytics.log("Fetch UserData Success.")
    }

    /**
     * Error Handler
     */

    fun onSignInError(e: Exception) {
        onDestroyLoadingDialog()
        showNetworkErrorDialog()
        authenticator.signOut()
        FireCrashlytics.log("Sign in error.")
        FireCrashlytics.report(e)
    }

    fun onDuplicateEmailError() {
        Toast.makeText(requireContext(), getString(R.string.duplicate_email), Toast.LENGTH_LONG).show()
        authenticator.signOut()
        onDestroyLoadingDialog()
    }

    protected fun onFetchUserDataError(setUnauthenticatedUi: () -> Unit) {
        showNetworkErrorDialog()
        setUnauthenticatedUi()
        viewModel.onFetchUserDataErrorDelivered()
        authenticator.signOut()
        FireCrashlytics.report(Exception("Error Fetch UserData."))
        FireCrashlytics.log("Fetch UserData Error.")
    }

    /**
     * Utilities
     */

    private fun showNetworkErrorDialog() {
        try {
            if (!isDialogShown) {
                val dialog = getConnectionErrorDialog()
                dialog.show(requireActivity().supportFragmentManager, "InternetErrorDialog")
                isDialogShown = true
            }
        } catch (e: Exception) {
            FireCrashlytics.report(e)
            requireActivity().finish()
        }
    }

    private fun showLoadingDialog(text: String) {
        try {
            loadingDialog = getLoadingDialog(text)
            loadingDialog?.isCancelable = false
            loadingDialog?.show(requireActivity().supportFragmentManager, "AuthLoadingDialog")
            showLoadingDialog = true
        } catch (e: Exception) {
            FireCrashlytics.report(e)
            requireActivity().finish()
        }
    }

    fun onDestroyLoadingDialog() {
        try {
            loadingDialog?.dismiss()
            loadingDialog = null
            isDialogShown = false
        } catch (e: Exception) {
            FireCrashlytics.report(e)
            requireActivity().finish()
        }
    }

}