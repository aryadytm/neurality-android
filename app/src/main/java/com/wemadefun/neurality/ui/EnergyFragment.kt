package com.wemadefun.neurality.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wemadefun.neurality.BrainApplication
import com.wemadefun.neurality.R
import com.wemadefun.neurality.firebaseutils.FireAnalytics
import com.wemadefun.neurality.ui.dialogs.LowEnergyDialog
import com.wemadefun.neurality.ui.dialogs.RewardedEnergyDialog
import com.wemadefun.neurality.utils.*
import javax.inject.Inject

abstract class EnergyFragment : Fragment() {

    abstract fun onBindEnergyViewModel(energyViewModel: EnergyViewModel)

    @Inject lateinit var energyViewModelFactory: ViewModelProvider.Factory
    protected lateinit var energyViewModel: EnergyViewModel

    @CallSuper
    override fun onAttach(context: Context) {
        (requireActivity().application as BrainApplication).appComponent.inject(this)
        energyViewModel = ViewModelProvider(this, energyViewModelFactory).get(EnergyViewModel::class.java)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBindEnergyViewModel(energyViewModel)
        setupNavigateToGame()
    }

    open fun setupNavigateToGame() {
        energyViewModel.eventNavigateToGame.observe(viewLifecycleOwner, Observer {
            if (it) {
                navigateToGame()
                energyViewModel.onEventDone()
            }
        })
        energyViewModel.eventLowEnergy.observe(viewLifecycleOwner, Observer {
            if (it) {
                showLowEnergyDialog()
                energyViewModel.onEventDone()
            }
        })
    }

    private fun navigateToGame() {
        val gameData = requireArguments()
            .getParcelable<GameProvider.GameData>(ARG_GAME_DATA)!!
        val args = bundleOf(ARG_GAME_DATA to gameData)
        FireAnalytics.eventPlayGame(getGameName(gameData.gameId), getCategoryName(gameData.category))
        findNavController().navigate(R.id.action_get_ready, args)
    }

    open fun showLowEnergyDialog() {
        val dialog = LowEnergyDialog()
        dialog.show(requireActivity().supportFragmentManager, "LowEnergyDialog")
        FireAnalytics.eventLowEnergyDialog()
    }

    open fun showRewardedEnergyDialog() {
        RewardedEnergyDialog().show(requireActivity().supportFragmentManager, "RewardedEnergyDialog")
    }
}