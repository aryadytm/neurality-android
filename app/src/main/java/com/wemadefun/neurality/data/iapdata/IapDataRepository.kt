package com.wemadefun.neurality.data.iapdata

import android.content.Context
import android.widget.Toast
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.wemadefun.neurality.data.userdata.modules.IapModule
import com.wemadefun.neurality.firebaseutils.FireAnalytics
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import com.wemadefun.neurality.firebaseutils.FireLogger
import javax.inject.Inject
import javax.inject.Singleton


const val PURCHASE_CANCELLED = "cancelled"
const val PURCHASE_ERROR = "error"
const val PURCHASE_SUCCESS = "success"

@Singleton
class IapDataRepository @Inject constructor(
    private val iapModule: IapModule,
    context: Context) {
    private var onPurchaseSuccessListener: () -> Unit = {}
    private var onPurchaseFailedListener: () -> Unit = {}
    private var onPurchaseCancelledListener: () -> Unit = {}

    private val purchaseUpdateListener = PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.responseCode == BillingResponseCode.OK && purchases != null)
        {
            FireAnalytics.eventPurchase(PURCHASE_SUCCESS)
            onPurchaseSuccessListener()
            for (purchase in purchases) { onHandlePurchase(purchase) }
        }
        else if (billingResult.responseCode == BillingResponseCode.USER_CANCELED) {
            FireAnalytics.eventPurchase(PURCHASE_CANCELLED)
            onPurchaseCancelledListener()
        } else {
            FireAnalytics.eventPurchase(PURCHASE_ERROR)
            FireCrashlytics.report(Exception("Purchase Error: (${billingResult.responseCode}) ${billingResult.debugMessage}"))
            onPurchaseFailedListener()
        }
    }

    private val premiumToast = Toast.makeText(context, "Sign In Success", Toast.LENGTH_SHORT)
    private val activatedToast = Toast.makeText(context, "Activated", Toast.LENGTH_LONG)
    private val _billingClient = BillingClient.newBuilder(context).setListener(purchaseUpdateListener)
        .enablePendingPurchases()
        .build()
    val billingClient: BillingClient get() = _billingClient

    fun setOnPurchaseSuccessListener(listener: () -> Unit ) {
        onPurchaseSuccessListener = listener
    }

    fun setOnPurchaseCancelledListener(listener: () -> Unit ) {
        onPurchaseCancelledListener = listener
    }

    fun setOnPurchaseFailedListener(listener: () -> Unit ) {
        onPurchaseFailedListener = listener
    }

    fun queryPurchases() {
        val purchases = billingClient.queryPurchases(BillingClient.SkuType.SUBS).purchasesList
        purchases?.let {
            for (purchase in it) { onHandlePurchase(purchase) }
        }
    }

    private fun onHandlePurchase(purchase: Purchase) {
        FireLogger.logPurchase(purchase)

        if (!purchase.isAcknowledged) {
            val acknowledgePurchaseParams: AcknowledgePurchaseParams = AcknowledgePurchaseParams
                .newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            billingClient.acknowledgePurchase(acknowledgePurchaseParams) {result ->
                if(result.responseCode == BillingResponseCode.OK){
                    activatedToast.show()
                }
            }
        }
        iapModule.onUpdateIap(purchase)
        premiumToast.show()
    }

}