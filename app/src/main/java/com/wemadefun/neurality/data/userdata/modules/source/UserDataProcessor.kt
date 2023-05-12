package com.wemadefun.neurality.data.userdata.modules.source

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wemadefun.neurality.data.userdata.UserData
import java.lang.reflect.Type

object UserDataProcessor
{
    private val gson: Gson = Gson()

    fun getUserDataJson(userData: UserData): String {
        return gson.toJson(userData)
    }

    fun getUserDataJson(userData: HashMap<String, Any>): String {
        return gson.toJson(userData)
    }

    fun getUserDataFromJson(json: String): UserData {
        return gson.fromJson(json, UserData::class.java)
    }

    fun getHashMapFromJson(json: String): HashMap<String, Any> {
        val type: Type = object : TypeToken<HashMap<String, Any>>(){}.type
        return gson.fromJson(json, type)
    }
}