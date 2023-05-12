package com.wemadefun.neurality.data.userdata

import java.io.Serializable

data class UserData(

    var gameScores: HashMap<String, MutableList<Int>>,

    var neuralityScores: HashMap<String, Int>,

    var registrationTime: Long,

    var email: String,

    var provider: String,

    var playCount: Int,

    var energy: Int,

    var energyLastAddTime: Long,

    var isHasRated: Boolean,

    var isSynced: Boolean,

    var isPremiumGamesEnabled: Boolean,

    var uid: String,

    var tempIap: Long,

    var iapToken: String,

    var iapSku: String,

    var referredBy: String

) : Serializable