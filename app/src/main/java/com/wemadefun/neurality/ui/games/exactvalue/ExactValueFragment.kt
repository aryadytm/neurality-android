package com.wemadefun.neurality.ui.games.exactvalue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.wemadefun.neurality.databinding.FragmentGameExactvalueBinding
import com.wemadefun.neurality.ui.games.GameFragment
import com.wemadefun.neurality.ui.games.GameViewModel

class ExactValueFragment : GameFragment() {

    override val backgroundId: Int = 0
    override lateinit var gameViewModel: GameViewModel
    override lateinit var lottieCorrect: LottieAnimationView
    override lateinit var lottieIncorrect: LottieAnimationView
    private lateinit var binding: FragmentGameExactvalueBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameExactvalueBinding.inflate(inflater, container, false)
        lottieCorrect = binding.lottieCorrect
        lottieIncorrect = binding.lottieIncorrect
        gameViewModel = ViewModelProvider(this).get(ExactValueViewModel::class.java)

        binding.viewModel = gameViewModel as ExactValueViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

}