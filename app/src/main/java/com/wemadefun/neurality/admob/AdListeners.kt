package com.wemadefun.neurality.admob

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import java.lang.Exception

object AdListeners {

    abstract class StatsNativeListener : AdListener() {

        override fun onAdOpened() {
            // TODO: Log this event to Firebase.
            FireCrashlytics.log("Stats Native Ad: Opened")
        }

        override fun onAdFailedToLoad(errorCode: Int) {
            FireCrashlytics.log(Exception("[AdMob] Stats Native Ad Failed To Load: $errorCode").toString())
        }
    }


    abstract class EnergyRewardedLoadCallback : RewardedAdLoadCallback() {

        override fun onRewardedAdFailedToLoad(errorCode: Int) {
            // Ad failed to load. TODO: Log errors online.
            FireCrashlytics.log(Exception("[AdMob] Energy Rewarded Ad Failed To Load: $errorCode").toString())
        }
    }


    abstract class EnergyRewardedCallback : RewardedAdCallback() {

        override fun onRewardedAdOpened() {
            // TODO: Log this event with Firebase.
            FireCrashlytics.log("Energy Rewarded Ad: Opened")
        }

        override fun onRewardedAdFailedToShow(errorCode: Int) {
            FireCrashlytics.log(Exception("[AdMob] Energy Rewarded Ad Failed To Show: $errorCode").toString())
        }

    }


    abstract class GameOverInterstitialListener : AdListener() {

        override fun onAdClicked() {
            // TODO: Log this event using Firebase.
            FireCrashlytics.log("Game Over Interstital: Clicked")
        }

        override fun onAdFailedToLoad(errorCode: Int) {
            FireCrashlytics.log(Exception("[AdMob] Game Over Interstitial Ad Failed To Load: $errorCode").toString())
        }
    }


    abstract class PauseBannerListener : AdListener() {

        override fun onAdOpened() {
            // TODO: Log this event with Firebase.
            FireCrashlytics.log("Pause Banner: Opened")
        }

        override fun onAdFailedToLoad(errorCode : Int) {
            FireCrashlytics.log(Exception("[AdMob] Pause Banner Ad Failed To Load: $errorCode").toString())
        }
    }

}