package com.wemadefun.neurality.ui.traindetails

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wemadefun.neurality.BrainApplication
import com.wemadefun.neurality.R
import com.wemadefun.neurality.databinding.FragmentTrainDetailsBinding
import com.wemadefun.neurality.ext.setImageWithGlide
import com.wemadefun.neurality.firebaseutils.FireAnalytics
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import com.wemadefun.neurality.firebaseutils.fireremote.FireRemote
import com.wemadefun.neurality.ui.EnergyFragment
import com.wemadefun.neurality.ui.EnergyViewModel
import com.wemadefun.neurality.ui.dialogs.PremiumOnlyGameDialog
import com.wemadefun.neurality.utils.*
import javax.inject.Inject

class TrainDetailsFragment : EnergyFragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: TrainDetailsViewModel
    private lateinit var binding: FragmentTrainDetailsBinding
    private val args: TrainDetailsFragmentArgs by navArgs()

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
        FireCrashlytics.log("Navigate to TrainDetailsFragment: ${args.argGameData.title}")
        binding = FragmentTrainDetailsBinding.inflate(inflater, container, false)
        setupViewModel()
        setupPlayGameButton()

        viewModel.imageResourceId.observe(viewLifecycleOwner, Observer {
            binding.imageGame.setImageWithGlide(it)
        })
        binding.buttonBack.setOnClickListener { findNavController().navigateUp() }

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun setupPlayGameButton() {
        if (!FireRemote.isPremiumAvailable) return
        if (!viewModel.isPremiumGamesEnabled) return

        val gameData = TrainDetailsFragmentArgs.fromBundle(requireArguments()).argGameData
        if (gameData.isPremiumOnly) {
            if (!isIapMode()) {
                binding.buttonStart.visibility = View.GONE
                binding.buttonStartLocked.visibility = View.VISIBLE
                binding.buttonStartLocked.setOnClickListener { showPremiumOnlyDialog() }
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(TrainDetailsViewModel::class.java)
        viewModel.start(args.argGameData)

        val gameData = TrainDetailsFragmentArgs.fromBundle(requireArguments()).argGameData
        if (gameData.tutorialText.isEmpty()) {
            binding.buttonTutorial.visibility = View.GONE
        }

        viewModel.eventNavigateToTutorial.observe(viewLifecycleOwner, Observer {
            if (it) {
                navToTutorial()
                viewModel.doneNavigating()
            }
        })
    }

    private fun navToTutorial() {
        val gameData = TrainDetailsFragmentArgs.fromBundle(requireArguments()).argGameData

        if (gameData.tutorialText.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.tutorial_included), Toast.LENGTH_SHORT).show()
            return
        }
        FireAnalytics.eventTutorialScreen(getGameName(gameData.gameId), getCategoryName(gameData.category))
        findNavController().navigate(TrainDetailsFragmentDirections.actionTutorial(gameData))
    }

    private fun showPremiumOnlyDialog() {
        PremiumOnlyGameDialog().show(requireActivity().supportFragmentManager, "PremiumGameDialog")
    }

}
