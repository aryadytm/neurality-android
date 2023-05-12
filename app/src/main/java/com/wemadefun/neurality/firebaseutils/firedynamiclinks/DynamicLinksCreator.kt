package com.wemadefun.neurality.firebaseutils.firedynamiclinks

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.dynamiclinks.ktx.socialMetaTagParameters
import com.google.firebase.ktx.Firebase
import com.wemadefun.neurality.R
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import com.wemadefun.neurality.firebaseutils.fireremote.FireRemote
import com.wemadefun.neurality.utils.CONFIG_APP_DOMAIN
import com.wemadefun.neurality.utils.CONFIG_APP_URI

object DynamicLinksCreator {

    private var dynamicLink = ""
    private var flowchartLink = ""

    fun createDynamicLink(context: Context, uid: String) {

        Firebase.dynamicLinks.shortLinkAsync(ShortDynamicLink.Suffix.SHORT) {
            link = Uri.parse("$CONFIG_APP_URI?inviter=$uid")
            domainUriPrefix = CONFIG_APP_DOMAIN

            androidParameters(context.packageName) {
                minimumVersion = 43
            }
            socialMetaTagParameters {
                imageUrl = Uri.parse(FireRemote.stringUrlAppIcon)
                title = context.getString(R.string.referral_link_title)
                description = context.getString(R.string.referral_link_desc)
            }
        }.addOnSuccessListener { result ->
            dynamicLink = result.shortLink.toString()
            flowchartLink = result.previewLink.toString()
        }.addOnFailureListener {
            FireCrashlytics.report(it)
        }
    }

    fun getShareIntent(context: Context): Intent {
        val sendIntent = Intent()
        val msg = context.getString(R.string.referral_share_msg, dynamicLink)
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg)
        sendIntent.type = "text/plain"
        return sendIntent
    }

    fun isAvailable(): Boolean {
        return FireRemote.isFreePremiumAvailable && dynamicLink.isNotEmpty()
    }

    fun destroyMyLink() {
        dynamicLink = ""
        flowchartLink = ""
    }

}