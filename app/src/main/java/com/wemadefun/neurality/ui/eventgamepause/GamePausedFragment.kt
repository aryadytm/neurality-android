package com.wemadefun.neurality.ui.eventgamepause

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.wemadefun.neurality.BrainApplication
import com.wemadefun.neurality.R
import com.wemadefun.neurality.admob.AdListeners
import com.wemadefun.neurality.admob.PauseBannerAd
import com.wemadefun.neurality.databinding.FragmentGamepausedBinding
import com.wemadefun.neurality.ui.EnergyFragment
import com.wemadefun.neurality.ui.EnergyViewModel
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import com.wemadefun.neurality.utils.isIapMode
import javax.inject.Inject

const val ARG_BACKGROUND = "argBackground"
// ARG_SCORE

class GamePausedFragment : EnergyFragment() {

    @Inject lateinit var pauseBannerAd: PauseBannerAd
    private lateinit var binding: FragmentGamepausedBinding

    override fun onBindEnergyViewModel(energyViewModel: EnergyViewModel) {
        // Now restart needs one energy.
        binding.energyViewModel = energyViewModel
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as BrainApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FireCrashlytics.log("Game Paused")
        binding = FragmentGamepausedBinding.inflate(inflater, container, false)
        val background = GamePausedFragmentArgs.fromBundle(requireArguments()).argBackground

        if (!isIapMode()) {
            setupBannerAd()
        } else {
            binding.bannerAd.visibility = View.GONE
        }

        binding.buttonResume.setOnClickListener {
            FireCrashlytics.log("Game Resumed")
            findNavController().navigateUp()
        }
        binding.buttonExit.setOnClickListener {
            FireCrashlytics.log("Game Exited")
            findNavController().navigate(R.id.action_train)
        }

        if (background != 0)
            binding.root.setBackgroundResource(background)

        return binding.root
    }

    private fun setupBannerAd() {
        val adRequest = pauseBannerAd.getAdRequest(requireContext())
        binding.bannerAd.adListener = object : AdListeners.PauseBannerListener() {}
        binding.bannerAd.loadAd(adRequest)
    }

    override fun onStart() {
        super.onStart()
        view?.viewTreeObserver?.addOnWindowFocusChangeListener { hasFocus ->
            if (!hasFocus) {
                binding.bannerAd.visibility = View.GONE
            } else {
                binding.bannerAd.visibility = View.VISIBLE
            }
        }
    }

}
