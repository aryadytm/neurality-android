package com.wemadefun.neurality.admob

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdLoader
import com.wemadefun.neurality.R
import com.wemadefun.neurality.utils.CONFIG_ADMOB_NATIVE_GAMEOVER
import javax.inject.Inject

class GameOverNativeAd @Inject constructor() {

    fun getAdLoader(activity: Activity, templateView: TemplateView): AdLoader.Builder {
        return AdLoader.Builder(activity, CONFIG_ADMOB_NATIVE_GAMEOVER)
            .forUnifiedNativeAd { unifiedNativeAd ->
                val color = ContextCompat.getDrawable(activity.baseContext, R.color.white) as ColorDrawable
                val styles = NativeTemplateStyle.Builder().withMainBackgroundColor(color).build()
                val template: TemplateView = templateView
                template.setStyles(styles)
                template.setNativeAd(unifiedNativeAd)
            }
    }

}