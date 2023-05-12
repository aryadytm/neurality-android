package com.wemadefun.neurality.firebaseutils.fireremote

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.wemadefun.neurality.R
import com.wemadefun.neurality.utils.jsonStringToHashMap


const val IS_PREMIUM_AVAILABLE = "is_premium_available"
const val IS_FREE_PREMIUM_AVAILABLE = "is_free_premium_available"
const val IS_FREE_PREMIUM_PROMOTED = "is_free_premium_promoted"

const val JSON_HELP_URLS = "json_help_urls"

const val STRING_URL_HELP = "string_url_help"
const val STRING_URL_HELP_FREE_PREMIUM = "string_url_help_free_premium"
const val STRING_URL_LEGAL = "string_url_legal_package"
const val STRING_URL_PRIVACY_POLICY = "string_url_privacy_policy"
const val STRING_URL_TERMS_CONDITIONS = "string_url_terms_conditions"
const val STRING_URL_APP_ICON = "string_url_app_icon"

object FireRemote {
    private val remoteConfig = Firebase.remoteConfig
    private val configSettings = remoteConfigSettings { minimumFetchIntervalInSeconds = 84400L }

    val remoteValue: FirebaseRemoteConfig get() = remoteConfig
    val jsonHelpUrls: HashMap<String, Any> get() = jsonStringToHashMap(remoteConfig.getString(JSON_HELP_URLS))

    val isPremiumAvailable: Boolean get() = remoteConfig.getBoolean(IS_PREMIUM_AVAILABLE)
    val isFreePremiumAvailable: Boolean get() = remoteConfig.getBoolean(IS_FREE_PREMIUM_AVAILABLE)
    val isFreePremiumPromoted: Boolean get() = remoteConfig.getBoolean(IS_FREE_PREMIUM_PROMOTED)

    val stringUrlHelp: String get() = remoteConfig.getString(STRING_URL_HELP)
    val stringUrlHelpFreePremium: String get() = remoteConfig.getString(STRING_URL_HELP_FREE_PREMIUM)
    val stringUrlLegal: String get() = remoteConfig.getString(STRING_URL_LEGAL)
    val stringUrlPrivacyPolicy: String get() = remoteConfig.getString(STRING_URL_PRIVACY_POLICY)
    val stringUrlTermsConditions: String get() = remoteConfig.getString(STRING_URL_TERMS_CONDITIONS)
    val stringUrlAppIcon: String get() = remoteConfig.getString(STRING_URL_APP_ICON)

    init {
        initialize()
    }

    fun initialize() {
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }
}