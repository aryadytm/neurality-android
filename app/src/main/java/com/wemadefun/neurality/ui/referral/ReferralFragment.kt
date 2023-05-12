package com.wemadefun.neurality.ui.referral

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.wemadefun.neurality.R
import com.wemadefun.neurality.databinding.FragmentReferralBinding
import com.wemadefun.neurality.ext.easyToastLong
import com.wemadefun.neurality.ext.openBrowser
import com.wemadefun.neurality.firebaseutils.firedynamiclinks.DynamicLinksCreator
import com.wemadefun.neurality.firebaseutils.fireremote.FireRemote
import com.wemadefun.neurality.ui.utilfragments.NonAnonymousFragment
import com.wemadefun.neurality.firebaseutils.FireCrashlytics

class ReferralFragment : NonAnonymousFragment() {

    private lateinit var binding: FragmentReferralBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentReferralBinding.inflate(inflater, container, false)
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnInvite.setOnClickListener { startInviteIntent() }
        binding.btnHelp.setOnClickListener { openBrowser(FireRemote.stringUrlHelpFreePremium) }
    }

    private fun startInviteIntent() {
        try {
            startActivity(DynamicLinksCreator.getShareIntent(requireContext()))
        } catch (e: Exception) {
            FireCrashlytics.report(e)
            easyToastLong(getString(R.string.feature_notavailable))
        }

    }

}
