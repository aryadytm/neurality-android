package com.wemadefun.neurality.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.wemadefun.neurality.BrainApplication
import com.wemadefun.neurality.R
import com.wemadefun.neurality.admob.EnergyRewardedAd
import com.wemadefun.neurality.data.userdata.modules.energy.EnergyModule
import javax.inject.Inject

class RewardedEnergyDialog : DialogFragment() {

    @Inject lateinit var energyModule: EnergyModule
    @Inject lateinit var rewardedAd: EnergyRewardedAd

    override fun onAttach(context: Context) {
        (requireActivity().application as BrainApplication).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.get_free_energy))
            builder.setMessage(getString(R.string.msg_watch_video_ad))
                .setPositiveButton(getString(R.string.get_it_now)) { _, _ -> showRewardedAd() }
                .setNegativeButton(R.string.later) { _, _ -> dismiss() }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun showRewardedAd() {
        rewardedAd.showRewardedAd(requireActivity()) { userEarnedReward() }
    }

    private fun userEarnedReward() {
        energyModule.onUserRewardedFromVideoAd()
        dismiss()
    }
}
