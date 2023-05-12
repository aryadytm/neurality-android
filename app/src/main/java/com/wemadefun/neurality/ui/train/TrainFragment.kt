package com.wemadefun.neurality.ui.train

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.wemadefun.neurality.BrainApplication
import com.wemadefun.neurality.R
import com.wemadefun.neurality.admob.EnergyRewardedAd
import com.wemadefun.neurality.data.userdata.modules.ConfigModule
import com.wemadefun.neurality.data.userdata.modules.DataModule
import com.wemadefun.neurality.databinding.FragmentTrainBinding
import com.wemadefun.neurality.firebaseutils.firedynamiclinks.DynamicLinksCreator
import com.wemadefun.neurality.firebaseutils.fireremote.FireRemote
import com.wemadefun.neurality.ui.EnergyFragment
import com.wemadefun.neurality.ui.EnergyViewModel
import com.wemadefun.neurality.ui.dialogs.ExitDialog
import com.wemadefun.neurality.ui.dialogs.PremiumAvailableDialog
import com.wemadefun.neurality.utils.CONFIG_ENERGY_MAX
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import com.wemadefun.neurality.utils.GameProvider
import com.wemadefun.neurality.utils.isIapMode
import javax.inject.Inject


class TrainFragment : EnergyFragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var rewardedAd: EnergyRewardedAd
    @Inject lateinit var dataModule: DataModule
    @Inject lateinit var configModule: ConfigModule

    private lateinit var viewModel: TrainViewModel
    private lateinit var binding: FragmentTrainBinding

    override fun onBindEnergyViewModel(energyViewModel: EnergyViewModel) {
        binding.energyVM = energyViewModel
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as BrainApplication).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        FireCrashlytics.log("Navigate to TrainFragment")
        binding = FragmentTrainBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(this)
        {
            ExitDialog().show(requireActivity().supportFragmentManager, "DIALOG_EXIT")
        }
        setupViewModel()
        setupFeaturedGameViewPager()
        setupSubscribeThings()
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun setupViewModel() {
        val adapter = TrainItemAdapter()
        viewModel = ViewModelProvider(this, viewModelFactory).get(TrainViewModel::class.java)
        viewModel.trainUiDataData.observe(viewLifecycleOwner, Observer {
            adapter.data = it
            adapter.categories = it.keys.toList()
        })
        binding.recyclerTrain.adapter = adapter
    }

    private fun setupFeaturedGameViewPager() {
        val games = getFeaturedGames()
        val adapter = ViewPagerAdapter(games, requireContext())
        binding.featuredGameViewpager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.featuredGameViewpager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position -> })
            .attach()
    }

    private fun setupSubscribeThings() {
        fetchRemoteValues()

        if (isIapMode()) {
            onIapMode()
        } else {
            onNotIapMode()
        }
    }

    private fun onIapMode() {
        binding.cardEnergy.visibility = View.GONE
        binding.cardReferral.visibility = View.GONE
    }

    private fun onNotIapMode() {
        binding.cardEnergy.visibility = View.VISIBLE
        binding.textPremiumMsg.text = getString(R.string.train_your_brain_anytime)
        binding.buttonStartTrial.setOnClickListener { findNavController().navigate(R.id.action_subscription) }

        if (DynamicLinksCreator.isAvailable() && FireRemote.isFreePremiumPromoted) {
            binding.cardReferral.visibility = View.VISIBLE
            binding.cardReferral.setOnClickListener { findNavController().navigate(R.id.action_referral) }
        } else {
            binding.cardReferral.visibility = View.GONE
        }
        observeAddEnergy()
    }

    private fun fetchRemoteValues() {
        val gamesPlayed = dataModule.gamesPlayed

        if (!FireRemote.isPremiumAvailable) {
            FireRemote.remoteValue.fetchAndActivate().addOnCompleteListener(requireActivity()) { task ->

                if (task.isSuccessful) {
                    if (FireRemote.isPremiumAvailable && gamesPlayed >= 1) {
                        // Existing users when premium is available. Don't enable premium-only games.
                        showPremiumAvailableDialog()
                    } else if (FireRemote.isPremiumAvailable) {
                        // New users when premium is available. Enable premium-only games.
                        configModule.onEnablePremiumOnlyGames()
                    }
                }
            }
        } else {
            FireRemote.remoteValue.fetchAndActivate()
        }
    }

    private fun observeAddEnergy() {
        binding.buttonAddEnergy.setOnClickListener { showRewardedEnergyDialog() }
        energyViewModel.energyValueLiveData.observe(viewLifecycleOwner, Observer {
            if (it >= CONFIG_ENERGY_MAX) {
                binding.buttonAddEnergy.visibility = View.GONE
            }
            else {
                if (rewardedAd.isLoaded()) {
                    binding.buttonAddEnergy.visibility = View.VISIBLE
                } else {
                    binding.buttonAddEnergy.visibility = View.GONE
                }
            }
        })
        if (!rewardedAd.isLoaded()) {
            rewardedAd.loadAd(requireContext())
        }
    }

    private fun showPremiumAvailableDialog() {
        PremiumAvailableDialog().show(requireActivity().supportFragmentManager, "PremiumAvailableDialog")
    }

    private fun getFeaturedGames(): MutableList<GameProvider.GameData> {
        val games = GameProvider.getInstance().gameData
        val currentTime = System.currentTimeMillis().toString()
        val gameIndex = currentTime.slice(2..4).toInt() % games.size
        val listOfGames = mutableListOf<GameProvider.GameData>()
        listOfGames.add(games[gameIndex])
        listOfGames.add(games[(gameIndex + 5) % games.size])
        listOfGames.add(games[(gameIndex + 11) % games.size])
        return listOfGames
    }
}
