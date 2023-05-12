package com.wemadefun.neurality.admob

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.wemadefun.neurality.utils.CONFIG_ADMOB_BANNER_PAUSE
import javax.inject.Inject

class PauseBannerAd @Inject constructor() {

    fun getAdRequest(context: Context): AdRequest {
        val adview = AdView(context)
        adview.adSize = AdSize.BANNER
        adview.adUnitId = CONFIG_ADMOB_BANNER_PAUSE
        return AdRequest.Builder().build()
    }

}