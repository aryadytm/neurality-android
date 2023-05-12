package com.wemadefun.neurality.ui.subscription

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.wemadefun.neurality.BrainApplication
import com.wemadefun.neurality.R
import com.wemadefun.neurality.data.iapdata.IapDataRepository
import com.wemadefun.neurality.databinding.FragmentSubscriptionBinding
import com.wemadefun.neurality.ext.easyToastLong
import com.wemadefun.neurality.ext.setImageWithGlide
import com.wemadefun.neurality.firebaseutils.fireremote.FireRemote
import com.wemadefun.neurality.firebaseutils.fireremote.IS_PREMIUM_AVAILABLE
import com.wemadefun.neurality.ui.dialogs.InternetErrorDialog
import com.wemadefun.neurality.ui.dialogs.PremiumNotAvailableDialog
import com.wemadefun.neurality.ui.utilfragments.NonAnonymousFragment
import com.wemadefun.neurality.firebaseutils.FireAnalytics
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import javax.inject.Inject

const val SKU_PREMIUM_MONTHLY = "premium_monthly"
const val SKU_PREMIUM_YEARLY = "premium_yearly"

class SubscriptionFragment : NonAnonymousFragment() {

    @Inject lateinit var iapDataRepository: IapDataRepository
    private lateinit var billingClient: BillingClient

    private lateinit var binding: FragmentSubscriptionBinding
    private lateinit var activeDrawable: Drawable
    private lateinit var inactiveDrawable: Drawable
    private lateinit var viewModel: SubscriptionViewModel

    private lateinit var monthlyPremiumSkuDetails: SkuDetails
    private lateinit var yearlyPremiumSkuDetails: SkuDetails

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as BrainApplication).appComponent.inject(this)
        FireAnalytics.eventSubscriptionScreen()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (isAnonymous) { return null }

        binding = FragmentSubscriptionBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(SubscriptionViewModel::class.java)

        setupPrepareDrawable()
        setupButtons()
        showLoading()

        val isPremiumAvailable = FireRemote.remoteValue.getBoolean(IS_PREMIUM_AVAILABLE)

        if (isPremiumAvailable) {
            FireAnalytics.eventSubscriptionAvailableScreen()
            setupSubscriptionObservers()
            setupSubscriptionCallbacks()
            setupBillingClient()
        } else {
            FireAnalytics.eventSubscriptionNotAvailableScreen()
            showNotAvailableDialog()
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun onSubscribeYearly() {
        val flowParams = BillingFlowParams.newBuilder().setSkuDetails(yearlyPremiumSkuDetails).build()
        val response = billingClient.launchBillingFlow(requireActivity(), flowParams)
        if (response.responseCode != BillingResponseCode.OK) {
            FireCrashlytics.report(Exception("onSubscribeClick(Yearly) Error: " +
                    "(${response.responseCode}) ${response.debugMessage}"))
        }
    }

    private fun onSubscribeMonthly() {
        val flowParams = BillingFlowParams.newBuilder().setSkuDetails(monthlyPremiumSkuDetails).build()
        val response = billingClient.launchBillingFlow(requireActivity(), flowParams)
        if (response.responseCode != BillingResponseCode.OK) {
            FireCrashlytics.report(Exception("onSubscribeClick(Monthly) Error: " +
                    "(${response.responseCode}) ${response.debugMessage}"))
        }
    }

    private fun setupSubscriptionCallbacks() {
        iapDataRepository.setOnPurchaseCancelledListener {
            easyToastLong(getString(R.string.cancelled_text))
        }
        iapDataRepository.setOnPurchaseFailedListener {
            easyToastLong(getString(R.string.msg_purchase_error))
        }
        iapDataRepository.setOnPurchaseSuccessListener {
            easyToastLong(getString(R.string.text_success))
            findNavController().navigateUp()
        }
    }

    private fun setupBillingClient() {
        binding.cardPurchase.visibility = View.GONE
        billingClient = iapDataRepository.billingClient

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                when (billingResult.responseCode) {
                    BillingResponseCode.OK -> onBillingServiceOk()
                    else -> {
                        FireCrashlytics.report(Exception("onSetupBilling Error: (${billingResult.responseCode})"))
                        showInternetErrorDialog()
                    }
                }
            }
            override fun onBillingServiceDisconnected() {
                this@SubscriptionFragment.onBillingServiceDisconnected()
            }
        })
    }

    private fun onBillingServiceOk() {
        FireCrashlytics.log("Billing service OK.")
        onQuerySkuDetailsStart()
    }

    private fun onBillingServiceDisconnected() {
        FireCrashlytics.log("Billing service disconnected.")
        showInternetErrorDialog()
    }

    private fun onQuerySkuDetailsStart() {
        val skuList = arrayListOf(SKU_PREMIUM_MONTHLY, SKU_PREMIUM_YEARLY)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)

        billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
            onQuerySkuDetailsFinished(billingResult, skuDetailsList)
        }

    }

    private fun onQuerySkuDetailsFinished(billingResult: BillingResult, skuDetailsList: MutableList<SkuDetails>?) {
        if (skuDetailsList == null) {
            showInternetErrorDialog()
        }
        else if (billingResult.responseCode == BillingResponseCode.OK) {

            for (skuDetails in skuDetailsList) {
                when (skuDetails.sku) {
                    SKU_PREMIUM_MONTHLY -> {
                        monthlyPremiumSkuDetails = skuDetails
                        binding.textMonthlyPrice.text = getString(R.string.price_monthly_param,
                            monthlyPremiumSkuDetails.price)
                    }
                    SKU_PREMIUM_YEARLY -> {
                        yearlyPremiumSkuDetails = skuDetails
                        binding.textYearlyPrice.text = getString(R.string.price_yearly_param,
                            yearlyPremiumSkuDetails.price)
                    }
                }
            }
            if (this::monthlyPremiumSkuDetails.isInitialized and this::yearlyPremiumSkuDetails.isInitialized) {
                showSubscriptionPlans()
            } else {
                showInternetErrorDialog()
            }
        } else {
            showInternetErrorDialog()
        }
    }

    private fun setupPrepareDrawable() {
        activeDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_sub_active)!!
        inactiveDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_sub_inactive)!!
    }

    private fun setupButtons() {
        binding.buttonBack.setOnClickListener { findNavController().navigateUp() }
        binding.textTermsConditions.setOnClickListener {
            binding.textPricingTerms.visibility.let {
                when (it) {
                    View.VISIBLE -> binding.textPricingTerms.visibility = View.GONE
                    View.GONE -> binding.textPricingTerms.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupSubscriptionObservers() {
        viewModel.eventYearlyActive.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.icYearly.setImageWithGlide(activeDrawable)
                binding.buttonSubscribe.setOnClickListener { onSubscribeYearly() }
            }
            else { binding.icYearly.setImageWithGlide(inactiveDrawable) }
        })

        viewModel.eventMonthlyActive.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.icMonthly.setImageWithGlide(activeDrawable)
                binding.buttonSubscribe.setOnClickListener { onSubscribeMonthly() }
            }
            else { binding.icMonthly.setImageWithGlide(inactiveDrawable) }
        })

        viewModel.eventButtonActive.observe(viewLifecycleOwner, Observer {
            if (it) { binding.buttonSubscribe.alpha = 1f }
            else { binding.buttonSubscribe.alpha = 0.5f }
        })
    }

    private fun showSubscriptionPlans() {
        binding.progressBar.visibility = View.GONE
        binding.purchaseInfo.visibility = View.VISIBLE
        binding.cardMonthly.visibility = View.VISIBLE
        binding.cardYearly.visibility = View.VISIBLE
        binding.cardPurchase.visibility = View.VISIBLE
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.purchaseInfo.visibility = View.GONE
        binding.cardMonthly.visibility = View.GONE
        binding.cardYearly.visibility = View.GONE
        binding.cardPurchase.visibility = View.GONE
    }

    private fun showInternetErrorDialog() {
        binding.progressBar.visibility = View.INVISIBLE
        val dialog = InternetErrorDialog()
        dialog.setOnButtonClickListener { findNavController().navigateUp() }
        dialog.setOnCancelListener { findNavController().navigateUp() }
        dialog.show(requireActivity().supportFragmentManager, "SubscriptionError")
    }

    private fun showNotAvailableDialog() {
        binding.progressBar.visibility = View.INVISIBLE
        val dialog = PremiumNotAvailableDialog()
        dialog.setOnButtonClickListener { findNavController().navigateUp() }
        dialog.setOnCancelListener { findNavController().navigateUp() }
        dialog.show(requireActivity().supportFragmentManager, "NotAvailable")
    }

}