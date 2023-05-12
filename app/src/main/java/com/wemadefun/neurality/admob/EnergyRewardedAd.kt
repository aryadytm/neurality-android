package com.wemadefun.neurality.admob

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.wemadefun.neurality.R
import com.wemadefun.neurality.utils.CONFIG_ADMOB_REWARDED_ENERGY
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EnergyRewardedAd @Inject constructor(context: Context) {

    private lateinit var rewardedAd: RewardedAd
    private var rewarded = false

    init {
        loadAd(context)
    }

    fun isLoaded(): Boolean {
        return rewardedAd.isLoaded
    }

    fun loadAd(context: Context) {
        val newRewardedAd = RewardedAd(context, CONFIG_ADMOB_REWARDED_ENERGY)
        val adLoadCallback = object: AdListeners.EnergyRewardedLoadCallback() {}
        newRewardedAd.loadAd(AdRequest.Builder().build(), adLoadCallback)
        rewardedAd = newRewardedAd
    }

    fun showRewardedAd(activity: Activity, onUserEarnedRewardListener: () -> Unit) {
        val adCallback = object : AdListeners.EnergyRewardedCallback() {

            override fun onRewardedAdClosed() {
                super.onRewardedAdClosed()
                loadAd(activity)
                if (rewarded) {
                    Toast.makeText(activity, activity.getString(R.string.free_energy_succeed), Toast.LENGTH_SHORT).show()
                    rewarded = false
                } else {
                    Toast.makeText(activity, activity.getString(R.string.why_skip), Toast.LENGTH_SHORT).show()
                    FireCrashlytics.log("Energy Rewarded Ad Cancelled")
                }
            }

            override fun onUserEarnedReward(@NonNull reward: RewardItem) {
                onUserEarnedRewardListener()
                FireCrashlytics.log("Energy Rewarded Ad User Earned: ${reward.type}")
                rewarded = true
            }
        }
        rewardedAd.show(activity, adCallback)
    }

    fun show(activity: Activity, callback: RewardedAdCallback) {
        if (rewardedAd.isLoaded) {
            rewardedAd.show(activity, callback)
        }
    }

}