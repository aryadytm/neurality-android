package com.wemadefun.neurality.ui.signin

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wemadefun.neurality.authentication.Authentication
import com.wemadefun.neurality.data.userdata.modules.DataModule
import com.wemadefun.neurality.data.userdata.modules.IapModule
import com.wemadefun.neurality.data.userdata.modules.loader.AnonymousLoader
import com.wemadefun.neurality.data.userdata.modules.loader.DefaultLoader
import com.wemadefun.neurality.data.userdata.modules.loader.MigrationLoader
import com.wemadefun.neurality.data.userdata.modules.loader.ReferralLoader
import com.wemadefun.neurality.firebaseutils.firedynamiclinks.DynamicLinksCreator
import com.wemadefun.neurality.firebaseutils.firedynamiclinks.DynamicLinksReceiver
import com.wemadefun.neurality.firebaseutils.fireremote.FireRemote
import com.wemadefun.neurality.utils.isIapMode
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInViewModel @Inject constructor(
    private val defaultLoader: DefaultLoader,
    private val refLoader: ReferralLoader,
    private val anonLoader: AnonymousLoader,
    private val migrationLoader: MigrationLoader,
    private val iapModule: IapModule,
    private val dataModule: DataModule
) : ViewModel() {

    private val _eventFetchingUserData = MutableLiveData<Boolean>()
    val eventFetchingUserData: LiveData<Boolean> = _eventFetchingUserData

    private val _eventFetchUserDataSuccess = MutableLiveData<Boolean>()
    val eventFetchUserDataSuccess: LiveData<Boolean> = _eventFetchUserDataSuccess

    private val _eventFetchUserDataError = MutableLiveData<Boolean>()
    val eventFetchUserDataError: LiveData<Boolean> = _eventFetchUserDataError

    private val _eventReferralInvalid = MutableLiveData<Boolean>()
    val eventReferralInvalid: LiveData<Boolean> = _eventReferralInvalid

    private val _eventReferralValid = MutableLiveData<Boolean>()
    val eventReferralValid: LiveData<Boolean> = _eventReferralValid

    private val _eventMigratedSuccess = MutableLiveData<Boolean>()
    val eventMigratedSuccess: LiveData<Boolean> = _eventMigratedSuccess

    fun onFetchUserData() {
        viewModelScope.launch {
            _eventFetchingUserData.value = true
            val uid = getUid()

            if (uid.isEmpty()) {
                onFetchUserDataError()
                return@launch
            }

            if (migrationLoader.isAnonymousDataNotMigrated()) {
                // Migrate anonymous data to permanent one
                migrationFetchFlow(uid)
                return@launch
            }

            if (DynamicLinksReceiver.isReferredBySomeone()) {
                referralFetchFlow(uid)
            } else {
                normalFetchFlow(uid)
            }
        }
    }

    fun onUseAnonymousAccount() {
        viewModelScope.launch {
            _eventFetchingUserData.value = true
            anonLoader.load()
            onFetchUserDataSuccess()
        }
    }

    private suspend fun normalFetchFlow(uid: String) {
        when (defaultLoader.load(uid)) {
            true -> onFetchUserDataSuccess()
            false -> onFetchUserDataError()
        }
    }

    private suspend fun migrationFetchFlow(uid: String) {
        when (migrationLoader.load(uid)) {
            true -> onFetchUserDataSuccess()
            false -> onFetchUserDataError()
        }
    }

    private suspend fun referralFetchFlow(uid: String) {
        val isValid = refLoader.isFirstTime(uid)
        if (isValid) {
            onReferralValid(uid)
        } else {
            onReferralInvalid(uid)
        }
    }

    private fun onReferralValid(uid: String) {
        // Valid criteria: No local or remote account.
        refLoader.load(uid, DynamicLinksReceiver.referrer)
        // Notify user if fetch is success and referral is valid.
        _eventReferralValid.value = true
        onFetchUserDataSuccess()
    }

    private suspend fun onReferralInvalid(uid: String) {
        _eventReferralInvalid.value = true
        normalFetchFlow(uid)
    }

    private fun onFetchUserDataSuccess() {
        iapModule.onCheckTempIap()
        _eventFetchingUserData.value = false
        _eventFetchUserDataSuccess.value = true
    }

    private fun onFetchUserDataError() {
        _eventFetchingUserData.value = false
        _eventFetchUserDataError.value = true
    }

    fun onFetchUserDataErrorDelivered() {
        _eventFetchUserDataError.value = false
    }

    fun onFetchUserDataSuccessDelivered() {
        _eventFetchUserDataSuccess.value = false
    }

    fun onReferralValidDelivered() {
        _eventReferralValid.value = false
    }

    fun onReferralInvalidDelivered() {
        _eventReferralInvalid.value = false
    }

    fun onCreateDynamicLink(context: Context) {
        if (FireRemote.isFreePremiumAvailable && !isIapMode()) {
            DynamicLinksCreator.createDynamicLink(context, dataModule.uid)
        }
    }

    private fun getUid(): String {
        var uid = ""
        try { uid = Authentication.getAuthUid() } catch (e: Exception) {}
        return uid
    }
}