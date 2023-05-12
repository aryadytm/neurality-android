package com.wemadefun.neurality.ui.subscription

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SubscriptionViewModel : ViewModel() {

    private val _eventYearlyActive = MutableLiveData<Boolean>()
    val eventYearlyActive: LiveData<Boolean> = _eventYearlyActive

    private val _eventMonthlyActive = MutableLiveData<Boolean>()
    val eventMonthlyActive: LiveData<Boolean> = _eventMonthlyActive

    private val _eventButtonActive = MutableLiveData<Boolean>()
    val eventButtonActive = _eventButtonActive

    init {
        _eventButtonActive.value = false
    }

    fun selectYearly() {
        _eventYearlyActive.value = true
        _eventMonthlyActive.value = false
        activateButton()
    }

    fun selectMonthly() {
        _eventMonthlyActive.value = true
        _eventYearlyActive.value = false
        activateButton()
    }

    private fun activateButton() {
        _eventButtonActive.value = true
    }

}