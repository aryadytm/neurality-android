package com.wemadefun.neurality.ui.games.dayofweek

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.wemadefun.neurality.R
import com.wemadefun.neurality.databinding.FragmentGameDayofweekBinding
import com.wemadefun.neurality.ui.games.GameFragment
import com.wemadefun.neurality.ui.games.GameViewModel

class DayOfWeekFragment : GameFragment() {

    override val backgroundId: Int = 0
    override lateinit var gameViewModel: GameViewModel
    override lateinit var lottieCorrect: LottieAnimationView
    override lateinit var lottieIncorrect: LottieAnimationView
    private lateinit var binding: FragmentGameDayofweekBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameDayofweekBinding.inflate(inflater, container, false)
        lottieCorrect = binding.lottieCorrect
        lottieIncorrect = binding.lottieIncorrect
        gameViewModel = ViewModelProvider(this).get(DayOfWeekViewModel::class.java)

        observeClick()
        binding.viewModel = (gameViewModel as DayOfWeekViewModel)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onNext() {
        updateUI()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun getDayQuestion(currentDay: Int, targetDay: Int): String {
        val dayDifference = targetDay - currentDay
        return when {
            (dayDifference == 1) -> getString(R.string.day_tommorow)
            (dayDifference == -1) -> getString(R.string.day_yesterday)
            (dayDifference > 1) -> getString(R.string.day_will, dayDifference.toString())
            (dayDifference < -1) -> getString(R.string.day_was, (-dayDifference).toString())
            else -> throw Exception("Illegal same day with question!")
        }
    }

    private fun getDayText(day: Int): String {
        val days = resources.getStringArray(R.array.days)
        return days[day-1]
    }

    private fun updateUI() {
        (gameViewModel as DayOfWeekViewModel).choiceOneDay.value?.let {
            binding.btnChoice1.text = getDayText(it)
        }
        (gameViewModel as DayOfWeekViewModel).choiceTwoDay.value?.let {
            binding.btnChoice2.text = getDayText(it)
        }
        (gameViewModel as DayOfWeekViewModel).choiceThreeDay.value?.let {
            binding.btnChoice3.text = getDayText(it)
        }
        (gameViewModel as DayOfWeekViewModel).currentDay.value?.let {
            binding.textCurrentDay.text = getString(R.string.today_is, getDayText(it))
        }
        (gameViewModel as DayOfWeekViewModel).currentDay.value?.let {
            binding.textQuestion.text = getDayQuestion(it, (gameViewModel as DayOfWeekViewModel).targetDay.value!!)
        }
    }

    private fun observeClick() {
        binding.cardQuestion.visibility = View.INVISIBLE
        (gameViewModel as DayOfWeekViewModel).clickable.observe(viewLifecycleOwner, Observer { clickable ->
            if (clickable) {
                ObjectAnimator.ofFloat(binding.cardQuestion, "translationX", 1200f).apply {
                    duration = 0
                    start()
                }.doOnEnd {
                    ObjectAnimator.ofFloat(binding.cardQuestion, "translationX", 0f).apply {
                        binding.cardQuestion.visibility = View.VISIBLE
                        duration = 200
                        start()
                    }
                }
            } else {
                ObjectAnimator.ofFloat(binding.cardQuestion, "translationX", -1200f).apply {
                    duration = 200
                    start()
                }
            }
        })
    }

}