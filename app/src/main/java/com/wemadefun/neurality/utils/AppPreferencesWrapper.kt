package com.wemadefun.neurality.utils

import android.content.Context
import com.wemadefun.neurality.R
import java.io.File

class AppPreferencesWrapper(context: Context) {

    private val prefName = context.getString(R.string.file_pref_application)
    private val prefDir = context.applicationContext.applicationInfo.dataDir + "/shared_prefs/" + prefName + ".xml"
    private val preferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

    fun getString(key: String, defaultValue: String): String {
        return preferences.getString(key, defaultValue)!!
    }

    fun putString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return preferences.getLong(key, defaultValue)
    }

    fun putLong(key: String, value: Long) {
        preferences.edit().putLong(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    fun isKeyExists(key: String): Boolean {
        return preferences.contains(key)
    }

    fun isExists(): Boolean {
        return File(prefDir).exists()
    }

}