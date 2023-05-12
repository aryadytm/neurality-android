package com.wemadefun.neurality.admob

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.wemadefun.neurality.utils.CONFIG_ADMOB_INTERSTITIAL_GAMEOVER
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameOverInterstitialAd @Inject constructor(context: Context) {

    private var interstitialAd: InterstitialAd = InterstitialAd(context)

    init {
        interstitialAd.adUnitId = CONFIG_ADMOB_INTERSTITIAL_GAMEOVER
        interstitialAd.adListener = object : AdListeners.GameOverInterstitialListener() {
            override fun onAdClosed() {
                super.onAdClosed()
                load()
            }
        }
        load()
    }

    fun load() {
        interstitialAd.loadAd(AdRequest.Builder().build())
    }

    fun show() {
        if (interstitialAd.isLoaded){
            interstitialAd.show()
        }
    }

}