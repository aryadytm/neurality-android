package com.wemadefun.neurality.di

import android.app.Activity
import com.wemadefun.neurality.authentication.source.GoogleAuthSource
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(activity: Activity) {

    private var googleAuthSource: GoogleAuthSource = GoogleAuthSource(activity)

    @Provides fun provideGoogleAuthSource(): GoogleAuthSource = googleAuthSource

}



