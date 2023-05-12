package com.wemadefun.neurality

import android.app.Application
import com.facebook.appevents.AppEventsLogger
import com.google.android.gms.ads.MobileAds
import com.wemadefun.neurality.di.ApplicationComponent

class BrainApplication : Application() {

    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        AppEventsLogger.activateApp(this)
        MobileAds.initialize(applicationContext)
    }

}