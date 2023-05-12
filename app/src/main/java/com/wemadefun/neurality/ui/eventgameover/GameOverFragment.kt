package com.wemadefun.neurality.ui.eventgameover

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.wemadefun.neurality.BrainApplication
import com.wemadefun.neurality.R
import com.wemadefun.neurality.admob.AdListeners
import com.wemadefun.neurality.admob.GameOverInterstitialAd
import com.wemadefun.neurality.admob.GameOverNativeAd
import com.wemadefun.neurality.databinding.FragmentGameoverBinding
import com.wemadefun.neurality.ui.EnergyFragment
import com.wemadefun.neurality.ui.EnergyViewModel
import com.wemadefun.neurality.ui.dialogs.RatingDialog
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import com.wemadefun.neurality.utils.GameProvider
import com.wemadefun.neurality.utils.isIapMode
import com.wemadefun.neurality.utils.navigateToGame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val ARG_SCORE = "argScore"

class GameOverFragment : EnergyFragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var interstitialAd: GameOverInterstitialAd
    @Inject lateinit var nativeAd: GameOverNativeAd

    private lateinit var viewModel: GameOverViewModel
    private lateinit var binding: FragmentGameoverBinding
    private var isAdShown = false

    override fun onBindEnergyViewModel(energyViewModel: EnergyViewModel) {
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
        val score = GameOverFragmentArgs.fromBundle(requireArguments()).argScore
        val gameData = GameOverFragmentArgs.fromBundle(requireArguments()).argGameData
        FireCrashlytics.log("Navigate To GameOverFragment: ${gameData.title}")
        binding = FragmentGameoverBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory).get(GameOverViewModel::class.java)
        viewModel.start(score, gameData)

        observeButtons(gameData)
        crossfadeButtons()

        binding.nativeAdGameover.visibility = View.GONE
        val isNativeAdEligible = (viewModel.getUserGamesPlayed() > 7) && !isIapMode()
        if (isNativeAdEligible) {
            binding.nativeAdGameover.visibility = View.INVISIBLE
            CoroutineScope(Dispatchers.Main).launch { setupNativeAd() }
        }
        binding.viewModel = viewModel
        binding.textActionBar.text = gameData.title
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkIsRated()
    }

    private fun observeButtons(gameData: GameProvider.GameData) {
        binding.buttonShare.setOnClickListener {
            isAdShown = true
            val score = GameOverFragmentArgs.fromBundle(requireArguments()).argScore
            findNavController().navigate(GameOverFragmentDirections.actionNavGameOverToNavShare(gameData, score))
        }
        viewModel.eventGameReplay.observe(viewLifecycleOwner, Observer {
            if (it) {
                navigateToGame(gameData, findNavController())
                viewModel.doneNavigating()
            }
        })
        viewModel.eventOtherWorkout.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(R.id.action_train)
                viewModel.doneNavigating()
            }
        })
        val adapter = ScoreItemAdapter()
        viewModel.topScore.observe(viewLifecycleOwner, Observer {
            adapter.data = it.toList()
            binding.recyclerScore.adapter = adapter
        })
    }

    private suspend fun setupNativeAd() {
        binding.nativeAdGameover.visibility = View.INVISIBLE

        withContext(Dispatchers.IO) {
            val adLoader = nativeAd.getAdLoader(requireActivity(), binding.nativeAdGameover)
                .withAdListener(object : AdListeners.StatsNativeListener() {
                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        binding.nativeAdGameover.visibility = View.VISIBLE
                    }
                }).build()
            adLoader.loadAd(AdRequest.Builder().build())
        }
    }

    private fun checkIsRated() {
        val isNotRated = viewModel.isUserNotRated()
        val isMinimumPlayed = viewModel.getUserGamesPlayed() > 2
        if (isNotRated && isMinimumPlayed) {
            showRatingDalog()
        }
    }

    private fun showRatingDalog() {
        val dialog = RatingDialog()
        dialog.show(requireActivity().supportFragmentManager, "RatingDialog")
    }

    private fun showInterstitialAd() {
        if (!isIapMode()) {
            val chance = (0..100).random() < 34
            FireCrashlytics.log("Showing interstitial ad: $chance")
            if (chance && !isAdShown) {
                isAdShown = true
                interstitialAd.show()
            }
        }
    }

    private fun crossfadeButtons() {
        viewModel.onButtonNotClickable()
        energyViewModel.onButtonNotClickable()

        binding.buttonPlayOther.apply {
            alpha = 0f
            animate().alpha(1f).setDuration(2000L).setListener(null)
        }

        binding.buttonReplay.apply {
            alpha = 0f
            animate().alpha(1f).setDuration(2000L).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    viewModel.onButtonClickable()
                    energyViewModel.onButtonClickable()
                }
            })
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onButtonClickable()
        energyViewModel.onButtonClickable()
    }
}
