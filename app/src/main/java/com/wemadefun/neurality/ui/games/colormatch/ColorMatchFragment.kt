package com.wemadefun.neurality.ui.games.colormatch

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.wemadefun.neurality.R
import com.wemadefun.neurality.databinding.FragmentGameColormatchBinding
import com.wemadefun.neurality.ui.games.GameFragment
import com.wemadefun.neurality.ui.games.GameViewModel

class ColorMatchFragment : GameFragment() {
    override val backgroundId: Int = 0
    override lateinit var gameViewModel: GameViewModel
    override lateinit var lottieCorrect: LottieAnimationView
    override lateinit var lottieIncorrect: LottieAnimationView
    private lateinit var binding: FragmentGameColormatchBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGameColormatchBinding.inflate(inflater, container, false)
        gameViewModel = ViewModelProvider(this).get(ColorMatchViewModel::class.java)
        lottieCorrect = binding.lottieCorrect
        lottieIncorrect = binding.lottieIncorrect

        binding.viewModel = gameViewModel as ColorMatchViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        updateUI(false)
    }

    override fun onNext() {
        updateUI(true)
    }

    private fun updateUI(animate: Boolean) {
        when ((gameViewModel as ColorMatchViewModel).topText.value) {
            ColorMatchGameModel.Color.BLUE -> binding.textMeaning.text = getString(R.string.truecolor_text_blue)
            ColorMatchGameModel.Color.RED -> binding.textMeaning.text = getString(R.string.truecolor_text_red)
            ColorMatchGameModel.Color.GREEN -> binding.textMeaning.text = getString(R.string.truecolor_text_green)
            ColorMatchGameModel.Color.YELLOW -> binding.textMeaning.text = getString(R.string.truecolor_text_yellow)
            ColorMatchGameModel.Color.BLACK -> binding.textMeaning.text = getString(R.string.truecolor_text_black)
        }
        when ((gameViewModel as ColorMatchViewModel).bottomText.value) {
            ColorMatchGameModel.Color.BLUE -> binding.textColored.text = getString(R.string.truecolor_text_blue)
            ColorMatchGameModel.Color.RED -> binding.textColored.text = getString(R.string.truecolor_text_red)
            ColorMatchGameModel.Color.GREEN -> binding.textColored.text = getString(R.string.truecolor_text_green)
            ColorMatchGameModel.Color.YELLOW -> binding.textColored.text = getString(R.string.truecolor_text_yellow)
            ColorMatchGameModel.Color.BLACK -> binding.textColored.text = getString(R.string.truecolor_text_black)
        }
        when ((gameViewModel as ColorMatchViewModel).bottomColor.value) {
            ColorMatchGameModel.Color.BLUE -> binding.textColored
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
            ColorMatchGameModel.Color.RED -> binding.textColored
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            ColorMatchGameModel.Color.GREEN -> binding.textColored
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            ColorMatchGameModel.Color.YELLOW -> binding.textColored
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.yellow))
            ColorMatchGameModel.Color.BLACK -> binding.textColored
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.text_darkgray))
        }

        if (animate) {
            ObjectAnimator.ofFloat(binding.cardTop, "translationX", -1000f).apply {
                duration = 0
                start()
            }.doOnEnd {
                ObjectAnimator.ofFloat(binding.cardTop, "translationX", 0f).apply {
                    duration = 100
                    start()
                }
            }

            ObjectAnimator.ofFloat(binding.cardBottom, "translationX", 1000f).apply {
                duration = 0
                start()
            }.doOnEnd {
                ObjectAnimator.ofFloat(binding.cardBottom, "translationX", 0f).apply {
                    duration = 100
                    start()
                }
            }
        }

    }
}