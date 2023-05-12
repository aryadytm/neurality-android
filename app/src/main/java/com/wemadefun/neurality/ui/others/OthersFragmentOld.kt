package com.wemadefun.neurality.ui.others

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.wemadefun.neurality.BrainApplication
import com.wemadefun.neurality.R
import com.wemadefun.neurality.authentication.Authenticator
import com.wemadefun.neurality.ui.dialogs.ExitDialog
import com.wemadefun.neurality.firebaseutils.FireAnalytics
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import com.wemadefun.neurality.utils.isIapMode
import javax.inject.Inject


class OthersFragmentOld : PreferenceFragmentCompat() {

    @Inject lateinit var authenticator: Authenticator

    override fun onAttach(context: Context) {
        super.onAttach(context)
        FireCrashlytics.log("Navigate to OthersFragment")
        (requireActivity().applicationContext as BrainApplication).appComponent.inject(this)
        requireActivity().onBackPressedDispatcher.addCallback(this)
        {
            ExitDialog().show(requireActivity().supportFragmentManager, "DIALOG_EXIT")
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_others, rootKey)
        val signOut = findPreference<Preference>("signout")
        val navToPremium = findPreference<Preference>("get_premium")
        val navToFeedback = findPreference<Preference>("feedback")
        val openPrivacyPolicy = findPreference<Preference>("privacy_policy")
        val openTermsOfService = findPreference<Preference>("terms_of_service")
        signOut?.setOnPreferenceClickListener { signOut() }
        navToPremium?.setOnPreferenceClickListener { navigateToPremium() }
        navToFeedback?.setOnPreferenceClickListener { navigateToFeedback() }
        openPrivacyPolicy?.setOnPreferenceClickListener { openPrivacyPolicy() }
        openTermsOfService?.setOnPreferenceClickListener { openTermsOfService() }
        if (isIapMode()) { navToPremium?.isVisible = false }
    }

    private fun signOut(): Boolean {
        FireAnalytics.eventSignOut()
        authenticator.signOut()
        findNavController().navigate(R.id.action_sign_in)
        return true
    }

    private fun navigateToPremium(): Boolean {
        findNavController().navigate(R.id.action_subscription)
        return true
    }

    private fun navigateToFeedback(): Boolean {
        findNavController().navigate(R.id.action_feedback)
        return true
    }

    private fun openPrivacyPolicy(): Boolean {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.url_privacypolicy)))
        startActivity(browserIntent)
        return true
    }

    private fun openTermsOfService(): Boolean {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.url_termsofservice)))
        startActivity(browserIntent)
        return true
    }

}
