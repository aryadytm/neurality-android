package com.wemadefun.neurality.firebaseutils.firedynamiclinks

import android.content.Intent
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wemadefun.neurality.firebaseutils.FireAnalytics
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import com.wemadefun.neurality.utils.CONFIG_DB_PATH_USERDATA
import com.wemadefun.neurality.utils.CONFIG_REFERRER_REWARD_TIME
import com.wemadefun.neurality.utils.currentTime

object DynamicLinksReceiver {

    private var referrerUid: String = ""
    val referrer get() = referrerUid

    fun receiveReferrerData(intent: Intent) {
        val data = intent.data
        try { referrerUid = data?.getQueryParameter("inviter")!! }
        catch (e: Exception) { FireCrashlytics.report(e) }
    }

    fun destroyReferrer() {
        referrerUid = ""
    }

    fun isReferredBySomeone(): Boolean {
        return referrerUid.isNotEmpty()
    }

    @Deprecated("Now handled automatically")
    fun giveReferrerReward() {
        FireAnalytics.eventReferralSuccess()
        val refCollection = Firebase.firestore
            .collection(CONFIG_DB_PATH_USERDATA)
            .document(referrerUid)
        refCollection.update(mapOf("tempIap" to (currentTime + CONFIG_REFERRER_REWARD_TIME)))
        destroyReferrer()
    }


}