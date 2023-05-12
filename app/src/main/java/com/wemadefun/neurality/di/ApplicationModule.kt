package com.wemadefun.neurality.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.wemadefun.neurality.R
import com.wemadefun.neurality.data.userdata.modules.source.remote.UserDataRemoteDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(application: Application) {

    private var resources: Resources = application.resources
    private var context = application.applicationContext
    private var sharedPreferences: SharedPreferences

    init {
        sharedPreferences = application.getSharedPreferences(resources.getString(R.string.file_pref_userdata), Context.MODE_PRIVATE)
    }

    @Provides
    fun provideContext(): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideResources(): Resources {
        return resources
    }

    @Singleton
    @Provides
    fun providePreferences(): SharedPreferences {
        return sharedPreferences
    }

    @Singleton
    @Provides
    fun provideRemoteUserDataSource(): UserDataRemoteDataSource {
        return UserDataRemoteDataSource
    }
}


