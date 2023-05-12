package com.wemadefun.neurality.ui.stats

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdRequest
import com.wemadefun.neurality.BrainApplication
import com.wemadefun.neurality.R
import com.wemadefun.neurality.admob.AdListeners
import com.wemadefun.neurality.admob.StatsNativeAd
import com.wemadefun.neurality.databinding.FragmentStatsBinding
import com.wemadefun.neurality.ui.dialogs.ExitDialog
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import com.wemadefun.neurality.utils.isIapMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class StatsFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var statsNativeAd: StatsNativeAd

    private lateinit var viewModel: StatsViewModel
    private lateinit var binding: FragmentStatsBinding
    private lateinit var adapter: StatsItemAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)

        requireActivity().onBackPressedDispatcher.addCallback(this)
        {
            ExitDialog().show(requireActivity().supportFragmentManager, "DIALOG_EXIT")
        }

        (requireActivity().application as BrainApplication).appComponent.inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        FireCrashlytics.log("Navigate to StatsFragment")
        viewModel = ViewModelProvider(this, viewModelFactory).get(StatsViewModel::class.java)
        binding = FragmentStatsBinding.inflate(inflater, container, false)
        setupBinding()
        setupButton()

        if (isIapMode()) {
            binding.nativeAdCard.visibility = View.GONE
        } else {
            binding.nativeAdCard.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.Main).launch { setupNativeAd() }
        }

        return binding.root
    }

    private fun setupBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        adapter = StatsItemAdapter()
        viewModel.neuralityScore.observe(viewLifecycleOwner, Observer {
            if (it <= 0) {
                binding.textNeuralityScore.text = "-"
                binding.circularProgressBar.progress = (0f)
            }
            else {
                binding.textNeuralityScore.text = it.toString()
                binding.circularProgressBar.progress = (it-55f)
            }
        })
    }

    private suspend fun setupNativeAd() {
        binding.nativeAdStats.visibility = View.INVISIBLE

        withContext(Dispatchers.IO) {
            val adLoader= statsNativeAd.getAdLoader(requireActivity(), binding.nativeAdStats)
                .withAdListener(object : AdListeners.StatsNativeListener() {
                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        binding.nativeAdStats.visibility = View.VISIBLE
                    }
                }).build()
            adLoader.loadAd(AdRequest.Builder().build())
        }
    }

    @SuppressLint("ResourceType")
    private fun setupButton() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            if (state == StatsState.STATE_BY_CATEGORY) {
                adapter.data = viewModel.scores.value!!
                binding.recyclerStats.adapter = adapter

                binding.btnByGame.setTextColor(Color.parseColor(getString(R.color.button_activated)))
                binding.btnByGame.setBackgroundColor(ContextCompat.getColor(
                    requireActivity().applicationContext,
                    R.color.white))
                binding.btnByCategory.setTextColor(Color.parseColor(getString(R.color.white)))
                binding.btnByCategory.setBackgroundColor(ContextCompat.getColor(
                    requireActivity().applicationContext,
                    R.color.button_activated))
            }
            else {
                adapter.data = viewModel.scores.value!!
                binding.recyclerStats.adapter = adapter

                binding.btnByCategory.setTextColor(Color.parseColor(getString(R.color.button_activated)))
                binding.btnByCategory.setBackgroundColor(ContextCompat.getColor(
                    requireActivity().applicationContext,
                    R.color.white))
                binding.btnByGame.setTextColor(Color.parseColor(getString(R.color.white)))
                binding.btnByGame.setBackgroundColor(ContextCompat.getColor(
                    requireActivity().applicationContext,
                    R.color.button_activated))
            }
        })
    }
}

enum class StatsState {
    STATE_BY_CATEGORY,
    STATE_BY_GAME
}