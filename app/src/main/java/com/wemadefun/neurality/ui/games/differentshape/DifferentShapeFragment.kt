package com.wemadefun.neurality.ui.games.differentshape

import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.wemadefun.neurality.R
import com.wemadefun.neurality.databinding.FragmentGameSymbolmatchBinding
import com.wemadefun.neurality.ext.setImageWithGlide
import com.wemadefun.neurality.ui.games.GameFragment
import com.wemadefun.neurality.ui.games.GameViewModel
import com.wemadefun.neurality.ui.games.differentshape.DifferentShapeGameModel.Shape

class DifferentShapeFragment : GameFragment() {
    override val backgroundId: Int = 0
    override lateinit var gameViewModel: GameViewModel
    override lateinit var lottieCorrect: LottieAnimationView
    override lateinit var lottieIncorrect: LottieAnimationView

    private lateinit var binding: FragmentGameSymbolmatchBinding
    private val shapesDrawable = mutableListOf<Drawable>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGameSymbolmatchBinding.inflate(inflater, container, false)
        gameViewModel = ViewModelProvider(this).get(DifferentShapeViewModel::class.java)
        lottieCorrect = binding.lottieCorrect
        lottieIncorrect = binding.lottieIncorrect
        (gameViewModel as DifferentShapeViewModel).start(DifferentShapeGameModel())

        prepareShapeDrawable()
        observeClickable()
        binding.viewModel = gameViewModel as DifferentShapeViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onNext() {
        animateCard()
        updateMainCard()
    }

    override fun onResume() {
        super.onResume()
        updateMainCard()
    }

    private fun updateMainCard() {
        val drawable = getImageDrawable()
        binding.imageMain.setImageWithGlide(drawable)
    }

    private fun prepareShapeDrawable() {
        if (shapesDrawable.isEmpty()) {
            val context = requireContext()
            shapesDrawable.add(ContextCompat.getDrawable(context, R.drawable.shape_1)!!)
            shapesDrawable.add(ContextCompat.getDrawable(context, R.drawable.shape_2)!!)
            shapesDrawable.add(ContextCompat.getDrawable(context, R.drawable.shape_3)!!)
            shapesDrawable.add(ContextCompat.getDrawable(context, R.drawable.shape_4)!!)
        }
    }

    private fun getImageDrawable(): Drawable {
        return when ((gameViewModel as DifferentShapeViewModel).shapeId.value) {
            Shape.SHAPE_1 -> shapesDrawable[0]
            Shape.SHAPE_2 -> shapesDrawable[1]
            Shape.SHAPE_3 -> shapesDrawable[2]
            Shape.SHAPE_4 -> shapesDrawable[3]
            null -> throw Exception("Unknown Different Shape ID")
        }
    }

    private fun observeClickable() {
        (gameViewModel as DifferentShapeViewModel).clickable.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.buttonNo.alpha = 1f
                binding.buttonYes.alpha = 1f
                binding.textQuestion.text = getString(R.string.question_symbolmatch)
            } else {
                binding.buttonNo.alpha = 0.5f
                binding.buttonYes.alpha = 0.5f
                binding.textQuestion.text = getString(R.string.question_remember_shape)
            }
        })
    }

    private fun animateCard() {
        val animDurationInMillis = 150L
        binding.imageOld.setImageWithGlide(binding.imageMain.drawable)

        ObjectAnimator.ofFloat(binding.cardLeft, "translationX", -1000f).apply {
            duration = animDurationInMillis - 50
            start()
        }.doOnEnd {
            ObjectAnimator.ofFloat(binding.cardLeft, "translationX", 0f).apply {
                duration = 0
                start()
            }
        }

        ObjectAnimator.ofFloat(binding.cardRight, "translationX", 0f).apply {
            duration = 50
            start()
        }.doOnEnd {
            ObjectAnimator.ofFloat(binding.cardRight, "translationX", 1000f).apply {
            duration = 0
            start()
            } .doOnEnd {
                ObjectAnimator.ofFloat(binding.cardRight, "translationX", 0f).apply {
                    duration = 100
                    start()
                }
            }
        }

        ObjectAnimator.ofFloat(binding.cardMain, "translationX", 1000f).apply{
            duration = 0
            start()
        }.doOnEnd {
            ObjectAnimator.ofFloat(binding.cardMain, "translationX", 0f).apply {
                duration = animDurationInMillis
                start()
            }
        }

        ObjectAnimator.ofFloat(binding.cardOld, "translationX", -1000f).apply {
            duration = animDurationInMillis
            start()
        }.doOnEnd {
            ObjectAnimator.ofFloat(binding.cardOld, "translationX", 0f).apply {
                duration = 0
                start()
            }
        }

    }

}