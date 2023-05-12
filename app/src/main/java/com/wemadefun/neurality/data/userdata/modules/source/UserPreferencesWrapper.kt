package com.wemadefun.neurality.data.userdata.modules.source

import android.content.SharedPreferences
import javax.inject.Inject

class UserPreferencesWrapper @Inject constructor(private val preferences: SharedPreferences) {

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

}