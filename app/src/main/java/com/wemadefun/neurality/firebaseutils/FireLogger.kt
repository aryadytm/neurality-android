package com.wemadefun.neurality.firebaseutils

import android.content.Context
import com.android.billingclient.api.Purchase
import com.google.android.exoplayer2.util.Util
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wemadefun.neurality.BuildConfig
import com.wemadefun.neurality.authentication.Authentication
import com.wemadefun.neurality.utils.CONFIG_DB_PATH_FEEDBACK
import com.wemadefun.neurality.utils.CONFIG_DB_PATH_MESSAGES
import com.wemadefun.neurality.utils.CONFIG_DB_PATH_PURCHASE
import com.wemadefun.neurality.utils.currentTime
import java.text.SimpleDateFormat
import java.util.*

object FireLogger {

    private var numFeedbacks = 0
    private var versionName = ""
    private var versionCode = ""

    init { setupVersion() }

    fun sendFeedback(text: String) {
        if (numFeedbacks < 5) {
            val time = getTime()
            val db = Firebase.firestore
            val docRef = db.collection(CONFIG_DB_PATH_FEEDBACK).document(time)

            val dataHashMap = hashMapOf(
                "version_name" to versionName,
                "version_code" to versionCode,
                "email" to Authentication.getAuthEmail(),
                "feedback" to text,
                "date" to time
            )

            FireAnalytics.eventActionFeedback(text)
            FireCrashlytics.report(Exception("Feedback received: $text"))
            docRef.set(dataHashMap, SetOptions.merge())
                .addOnFailureListener { FireCrashlytics.report(it) }
                .addOnSuccessListener { FireCrashlytics.log("Feedback Success!") }
        }
    }

    fun sendMessage(message: String, contactEmail: String, type: Int, context: Context, geoMap: HashMap<String, String>) {
        val time = getTime()

        val language = if (Util.SDK_INT >= 24) {
            context.resources.configuration.locales.get(0).toString()
        } else {
            context.resources.configuration.locale.country;
        }

        val messageMutableMap = mutableMapOf(
            "account_email" to Authentication.getAuthEmail(),
            "account_provider" to Authentication.getAuthProvider(),
            "contact_email" to contactEmail,
            "contact_message" to message,
            "contact_type" to type.toString(),
            "contact_time" to time,
            "contact_language" to language,
            "version_name" to versionName,
            "version_code" to versionCode
        )

        try { messageMutableMap["account_uid"] = Authentication.getAuthUid() }
        catch (e: Exception) { }

        messageMutableMap.putAll(geoMap)
        val messageHashMap = messageMutableMap as HashMap<String, String>
        val db = Firebase.firestore
        val docRef = db.collection(CONFIG_DB_PATH_MESSAGES).document(currentTime.toString())
        docRef.set(messageHashMap, SetOptions.merge())
            .addOnFailureListener { FireCrashlytics.report(it) }
            .addOnSuccessListener { FireCrashlytics.log("Message Success!") }
    }

    fun logPurchase(purchase: Purchase) {
        val purchaseHashMap = hashMapOf(
            "account_email" to Authentication.getAuthEmail(),
            "account_provider" to Authentication.getAuthProvider(),
            "version_name" to versionName,
            "version_code" to versionCode,
            "accountIdentifiers" to purchase.accountIdentifiers.toString(),
            "purchaseState" to purchase.purchaseState.toString(),
            "purchaseTime" to purchase.purchaseTime.toString(),
            "purchaseDate" to getDate(purchase.purchaseTime),
            "purchaseToken" to purchase.purchaseToken,
            "orderId" to purchase.orderId,
            "signature" to purchase.signature,
            "sku" to purchase.sku,
            "isAcknowledged" to purchase.isAcknowledged.toString(),
            "isAutoRenewing" to purchase.isAutoRenewing.toString(),
            "developerPayload" to purchase.developerPayload,
            "packageName" to purchase.packageName,
            "originalJson" to purchase.originalJson
        )
        try { purchaseHashMap["account_uid"] = Authentication.getAuthUid() }
        catch (e: Exception) { }

        val db = Firebase.firestore
        val docRef = db.collection(CONFIG_DB_PATH_PURCHASE).document(purchase.purchaseToken)
        docRef.set(purchaseHashMap, SetOptions.merge())
            .addOnFailureListener { FireCrashlytics.report(it) }
            .addOnSuccessListener { FireCrashlytics.log("Purchase Message Sent!") }
    }

    private fun getTime(): String {
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("GMT+0:00")
        val currentDate = Date(calendar.timeInMillis)
        val formatter = SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.US)
        return formatter.format(currentDate).toString()
    }

    private fun getDate(millis: Long): String {
        val formatter = SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.US)
        return formatter.format(Date(millis)).toString()
    }

    private fun setupVersion() {
        try {
            versionCode = BuildConfig.VERSION_CODE.toString()
            versionName = BuildConfig.VERSION_NAME
        } catch (e: Exception) { FireCrashlytics.report(e)}
    }

}