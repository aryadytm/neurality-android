package com.wemadefun.neurality.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieDrawable.INFINITE
import com.wemadefun.neurality.BrainApplication
import com.wemadefun.neurality.R
import com.wemadefun.neurality.admob.EnergyRewardedAd
import com.wemadefun.neurality.databinding.DialogLowEnergyBinding
import com.wemadefun.neurality.ui.EnergyViewModel
import javax.inject.Inject


class LowEnergyDialog : DialogFragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var rewardedAd: EnergyRewardedAd

    override fun onAttach(context: Context) {
        (requireActivity().application as BrainApplication).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogLowEnergyBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(EnergyViewModel::class.java)

        if (!rewardedAd.isLoaded()) {
            binding.buttonFreeEnergy.visibility = View.GONE
        }

        binding.anim.setAnimation("lottie_lightning.json")
        binding.anim.repeatCount = INFINITE
        binding.anim.playAnimation()
        binding.buttonGetPremium.setOnClickListener { onGetPremiumClick() }
        binding.buttonFreeEnergy.setOnClickListener { onFreeEnergyClick() }

        binding.energyVM = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        return dialog
    }

    private fun onGetPremiumClick() {
        findNavController().navigate(R.id.action_subscription)
        dismiss()
    }

    private fun onFreeEnergyClick() {
        RewardedEnergyDialog().show(requireActivity().supportFragmentManager, "RewardedEnergyDialog")
        dismiss()
    }


}