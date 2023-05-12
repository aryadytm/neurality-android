package com.wemadefun.neurality.firebaseutils

import android.app.Activity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.wemadefun.neurality.authentication.Authentication
import com.wemadefun.neurality.utils.isIapMode

const val REGISTRATION_TIME = "registration_time"
const val IS_IRREGULAR = "is_irregular"
const val IS_IAP_USER = "is_iap_user"

object FireAnalytics {

    private val firebaseAnalytics: FirebaseAnalytics get() = Firebase.analytics

    fun onAuthenticated() {
        val irregulars = listOf("ytfunbox@gmail.com", "yanguniktube@gmail.com")
        val email = Authentication.getAuthEmail()

        if (email in irregulars) {
            setUserProperty(
                IS_IRREGULAR,
                "true"
            )
        } else {
            setUserProperty(
                IS_IRREGULAR,
                "false"
            )
        }

        if (isIapMode()) {
            setUserProperty(
                IS_IAP_USER,
                "true"
            )
        } else {
            setUserProperty(
                IS_IAP_USER,
                "false"
            )
        }
    }

    fun setUserProperty(key: String, value: String) {
        firebaseAnalytics.setUserProperty(key, value)
    }

    fun setScreen(activity: Activity, fragment: String) {
        firebaseAnalytics.setCurrentScreen(activity, fragment, fragment)
    }

    fun eventSignIn(method: String) {
        FireCrashlytics.log("Sign In Using: $method")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
            param(FirebaseAnalytics.Param.METHOD, method)
        }
    }

    fun eventSignOut() {
        FireCrashlytics.log("Sign Out")
        firebaseAnalytics.logEvent("action_sign_out") {}
    }

    fun eventPlayGame(game: String, category: String) {
        FireCrashlytics.log("Navigate to GameFragment: $category, $game")
        firebaseAnalytics.logEvent("screen_play_game") {
            param("game_name", game)
            param("game_category", category)
        }
    }

    fun eventTutorialScreen(game: String, category: String) {
        FireCrashlytics.log("Navigate to TutorialFragment: $category, $game")
        firebaseAnalytics.logEvent("screen_tutorial") {
            param("game_name", game)
            param("game_category", category)
        }
    }

    fun eventPurchase(result: String) {
        FireCrashlytics.log("Purchase Result: $result")
        firebaseAnalytics.logEvent("purchase") {
            param("result", result)
        }
    }

    fun eventShareScreen() {
        FireCrashlytics.log("Navigate to ShareFragment")
        firebaseAnalytics.logEvent("screen_share") {}
    }

    fun eventSubscriptionScreen() {
        FireCrashlytics.log("Navigate to SubscriptionFragment")
        firebaseAnalytics.logEvent("screen_subscription") {}
    }

    fun eventSubscriptionAvailableScreen() {
        FireCrashlytics.log("Navigate to SubscriptionFragment Available")
        firebaseAnalytics.logEvent("screen_subscription_available") {}
    }

    fun eventSubscriptionNotAvailableScreen() {
        FireCrashlytics.log("Navigate to SubscriptionFragment NOT Available")
        firebaseAnalytics.logEvent("screen_subscription_not_available") {}
    }

    fun eventLowEnergyDialog() {
        FireCrashlytics.log("Show Low Energy Dialog")
        firebaseAnalytics.logEvent("dialog_lowenergy") {}
    }

    fun eventReferralSuccess() {
        FireCrashlytics.log("Referral Success")
        firebaseAnalytics.logEvent("referral_success") {}
    }

    fun eventPositiveStar() {
        FireCrashlytics.log("Five Star Rated")
        firebaseAnalytics.logEvent("action_positive_star") {}
    }

    fun eventRating(stars: String) {
        FireCrashlytics.log("User Rated")
        firebaseAnalytics.logEvent("action_rating") {
            param("stars", stars)
        }
    }

    fun eventActionFeedback(message: String) {
        FireCrashlytics.log("Feedback Sent: $message")
        firebaseAnalytics.logEvent("action_feedback") {
            param("message", message)
        }
    }

    fun eventEnterAge(isOverEighteen: Boolean) {
        FireCrashlytics.log("Age over 18: $isOverEighteen")
        val age = when(isOverEighteen) {
            true -> "over_18"
            else -> "under_18"
        }
        firebaseAnalytics.logEvent("attr_age") {
            param("age", age)
        }
        setUserProperty(
            "user_age",
            age
        )
    }

}