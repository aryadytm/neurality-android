package com.wemadefun.neurality.ui.games

import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.wemadefun.neurality.BrainApplication
import com.wemadefun.neurality.R
import com.wemadefun.neurality.data.userdata.modules.ScoreModule
import com.wemadefun.neurality.ext.hideKeyboard
import com.wemadefun.neurality.ui.eventgameover.ARG_SCORE
import com.wemadefun.neurality.ui.eventgamepause.ARG_BACKGROUND
import com.wemadefun.neurality.utils.ARG_GAME_DATA
import com.wemadefun.neurality.utils.GameProvider
import javax.inject.Inject

// Arguments: ARG_GAME_DATA
abstract class GameFragment : Fragment() {

    @Inject
    lateinit var scoreModule: ScoreModule

    abstract val backgroundId: Int
    abstract var gameViewModel: GameViewModel
    abstract var lottieCorrect: LottieAnimationView
    abstract var lottieIncorrect: LottieAnimationView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as BrainApplication).appComponent.inject(this)
    }

    private fun onGameOver() {
        val gameData = requireArguments().getParcelable<GameProvider.GameData>(ARG_GAME_DATA)!!
        val args = bundleOf(
            ARG_GAME_DATA to gameData,
            ARG_SCORE to gameViewModel.scoreText.value
        )
        scoreModule.insertGameScore(gameData.gameId, gameViewModel.score.value!!.toInt())
        findNavController().navigate(R.id.action_game_over, args)
    }

    private fun onGamePause() {
        val gameData = requireArguments().getParcelable<GameProvider.GameData>(ARG_GAME_DATA)!!
        val args = bundleOf(
            ARG_GAME_DATA to gameData,
            ARG_SCORE to gameViewModel.scoreText.value,
            ARG_BACKGROUND to backgroundId
        )
        findNavController().navigate(R.id.action_game_pause, args)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            onGamePause()
        }
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
        gameViewModel.pauseTimer()
    }

    override fun onResume() {
        super.onResume()
        hideKeyboard()
        gameViewModel.resumeTimer()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBindCorrectIncorrectLottieAnimation()

        gameViewModel.eventGameOver.observe(viewLifecycleOwner, Observer { isGameOver ->
            if (isGameOver) {
                onGameOver()
                gameViewModel.eventGameOverDone()
            }
        })
        gameViewModel.eventGamePause.observe(viewLifecycleOwner, Observer { isGamePause ->
            if (isGamePause) {
                onGamePause()
                gameViewModel.eventGamePauseDone()
            }
        })
        gameViewModel.eventGameCorrect.observe(viewLifecycleOwner, Observer { isCorrect ->
            if (isCorrect) {
                onCorrect()
                gameViewModel.eventGameCorrectDone()
            }
        })
        gameViewModel.eventGameIncorrect.observe(viewLifecycleOwner, Observer { isIncorrect ->
            if (isIncorrect) {
                onIncorrect()
                gameViewModel.eventGameIncorrectDone()
            }
        })
        gameViewModel.eventGameNext.observe(viewLifecycleOwner, Observer { isNext ->
            if (isNext) {
                onNext()
                gameViewModel.eventGameNextDone()
            }
        })
    }

    private fun onCorrect() {
        lottieCorrect.visibility = View.VISIBLE
        lottieCorrect.playAnimation()
    }

    private fun onIncorrect() {
        lottieIncorrect.visibility = View.VISIBLE
        lottieIncorrect.playAnimation()
    }

    private fun onBindCorrectIncorrectLottieAnimation() {
        lottieCorrect.setAnimation("lottie_correct.json")
        lottieIncorrect.setAnimation("lottie_incorrect.json")
        lottieCorrect.visibility = View.INVISIBLE
        lottieIncorrect.visibility = View.INVISIBLE
        lottieCorrect.speed = 1f
        lottieIncorrect.speed = 1f
        lottieCorrect.repeatCount = 0
        lottieIncorrect.repeatCount = 0

        lottieCorrect.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationEnd(p0: Animator?) { lottieCorrect.visibility = View.INVISIBLE }
            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
        })

        lottieIncorrect.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationEnd(p0: Animator?) { lottieIncorrect.visibility = View.INVISIBLE }
            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
        })
    }

    /**
     * Open function.
     *
     * Invocated after [onCorrect] or [onIncorrect].
     *
     * Hint: Call [onNext] to update views to UI when ViewModel data binding method is not possible.
     */
    open fun onNext() {}
}
