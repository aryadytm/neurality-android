package com.wemadefun.neurality.data.userdata.modules

import com.android.billingclient.api.Purchase
import com.wemadefun.neurality.data.userdata.UserDataRepository
import com.wemadefun.neurality.ui.subscription.SKU_PREMIUM_MONTHLY
import com.wemadefun.neurality.ui.subscription.SKU_PREMIUM_YEARLY
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import com.wemadefun.neurality.utils.WEEK
import com.wemadefun.neurality.utils.currentTime
import javax.inject.Inject

class IapModule @Inject constructor(private val repository: UserDataRepository) {

    companion object {
        var IS_IAP_ENABLED: Boolean = false
    }

    fun onUpdateIap(purchase: Purchase) {
        repository.ifActiveThenSaveLocal {
            val isTrial = it.iapToken.isEmpty()
            val isPaid = purchase.isAutoRenewing
            it.iapToken = purchase.purchaseToken
            it.iapSku = purchase.sku

            if (isTrial) {
                val currentTime = System.currentTimeMillis()
                when (purchase.sku) {
                    SKU_PREMIUM_MONTHLY -> it.tempIap = currentTime + WEEK
                    SKU_PREMIUM_YEARLY -> it.tempIap = currentTime + (2 * WEEK)
                }
                repository.saveLocalAndRemote()
                FireCrashlytics.log("ACTIVATING TEMP IAP")
            }
        }
        IS_IAP_ENABLED = true
        FireCrashlytics.log("IAP MODE: NORMAL")
    }

    fun onCheckTempIap() {
        repository.ifActive {
            val isValid = currentTime < it.tempIap
            if (isValid) {
                IS_IAP_ENABLED = true
            }
        }
    }

}