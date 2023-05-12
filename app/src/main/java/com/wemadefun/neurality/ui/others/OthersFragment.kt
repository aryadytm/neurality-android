package com.wemadefun.neurality.ui.others

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.wemadefun.neurality.BrainApplication
import com.wemadefun.neurality.BuildConfig
import com.wemadefun.neurality.R
import com.wemadefun.neurality.authentication.Authentication
import com.wemadefun.neurality.authentication.Authenticator
import com.wemadefun.neurality.data.userdata.modules.DataModule
import com.wemadefun.neurality.data.userdata.modules.ScoreModule
import com.wemadefun.neurality.databinding.FragmentOthersNewBinding
import com.wemadefun.neurality.ext.openBrowser
import com.wemadefun.neurality.firebaseutils.FireAnalytics
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import com.wemadefun.neurality.firebaseutils.firedynamiclinks.DynamicLinksCreator
import com.wemadefun.neurality.firebaseutils.fireremote.FireRemote
import com.wemadefun.neurality.ui.dialogs.ExitDialog
import com.wemadefun.neurality.utils.isIapMode
import javax.inject.Inject

class OthersFragment : Fragment() {

    @Inject lateinit var scoreModule: ScoreModule
    @Inject lateinit var dataModule: DataModule
    @Inject lateinit var authenticator: Authenticator
    private lateinit var binding: FragmentOthersNewBinding

    override fun onAttach(context: Context) {
        (requireActivity().application as BrainApplication).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentOthersNewBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(this)
        {
            ExitDialog().show(requireActivity().supportFragmentManager, "DIALOG_EXIT")
        }
        setupUserStats()
        setupPreferenceClickListeners()
        return binding.root
    }

    private fun setupUserStats() {
        try {
            binding.textName.text = Authentication.getAuthName()
            binding.textNq.text = getString(R.string.pref_nq_string, getNq())
            setupVersion()
        } catch (e: Exception) {
            FireCrashlytics.report(e)
        }
    }

    private fun setupVersion() {
        val versionCode: String = BuildConfig.VERSION_CODE.toString()
        val versionName: String = BuildConfig.VERSION_NAME
        binding.unclickableVersionText.text = getString(R.string.pref_version_placeholder, versionName, versionCode)
        binding.unclickableVersionDesc.text = getString(R.string.pref_uid_placeholder, dataModule.uid)
    }

    private fun setupPreferenceClickListeners() {
        binding.clickableGetPremium.setOnClickListener { findNavController().navigate(R.id.action_subscription) }
        binding.clickableSignOut.setOnClickListener { onSignOut() }
        binding.clickableMessageUs.setOnClickListener { findNavController().navigate(R.id.action_feedback) }
        binding.clickablePrivacyPolicy.setOnClickListener { openBrowser(FireRemote.stringUrlPrivacyPolicy) }
        binding.clickableTermsOfService.setOnClickListener { openBrowser(FireRemote.stringUrlTermsConditions) }
        binding.clickableHelp.setOnClickListener { openBrowser(FireRemote.stringUrlHelp) }

        if (DynamicLinksCreator.isAvailable()) {
            binding.clickableFreePremium.visibility = View.VISIBLE
            binding.clickableFreePremium.setOnClickListener { findNavController().navigate(R.id.action_referral) }
        } else {
            binding.clickableFreePremium.visibility = View.GONE
        }

        if (isIapMode()) {
            binding.clickableFreePremium.visibility = View.GONE
            binding.clickableGetPremium.visibility = View.GONE
        }
    }

    private fun onSignOut() {
        FireAnalytics.eventSignOut()
        authenticator.signOut()
        findNavController().navigate(R.id.action_sign_in)
    }

    private fun getNq(): String {
        val nq = scoreModule.getNeuralityScore()
        return if (nq < 56) { "0" } else nq.toString()
    }

}