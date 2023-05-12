package com.wemadefun.neurality.ui.games.digitspan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.wemadefun.neurality.R
import com.wemadefun.neurality.databinding.FragmentGameDigitspanBinding
import com.wemadefun.neurality.ext.hideKeyboard
import com.wemadefun.neurality.ui.games.GameFragment
import com.wemadefun.neurality.ui.games.GameViewModel


class DigitSpanFragment : GameFragment() {

    override val backgroundId: Int = 0
    override lateinit var gameViewModel: GameViewModel
    override lateinit var lottieCorrect: LottieAnimationView
    override lateinit var lottieIncorrect: LottieAnimationView
    private lateinit var binding: FragmentGameDigitspanBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGameDigitspanBinding.inflate(inflater, container, false)
        gameViewModel = ViewModelProvider(this).get(DigitSpanViewModel::class.java)
        lottieCorrect = binding.lottieCorrect
        lottieIncorrect = binding.lottieIncorrect

        observeUI()
        binding.viewModel = gameViewModel as DigitSpanViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (gameViewModel as DigitSpanViewModel).paused = false
        if ((gameViewModel as DigitSpanViewModel).numbersSequence.isNotEmpty()) {
            (gameViewModel as DigitSpanViewModel).showNumbersInSequence()
        }
    }

    override fun onPause() {
        super.onPause()
        (gameViewModel as DigitSpanViewModel).paused = true
    }

    override fun onNext() {
        super.onNext()
        showNumbers()
    }

    private fun observeUI() {
        (gameViewModel as DigitSpanViewModel).clickable.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.editTextAnswer.text = null
                showEditText()
            }
        })
        (gameViewModel as DigitSpanViewModel).eventSubmit.observe(viewLifecycleOwner, Observer {
            if (it) {
                val answer = binding.editTextAnswer.text.toString()
                if (answer.isEmpty()) {
                    Toast.makeText(requireContext(), getString(R.string.please_enter_answer), Toast.LENGTH_SHORT).show()
                } else {
                    (gameViewModel as DigitSpanViewModel).onSubmit(answer)
                    hideKeyboard()
                }
            }
        })
        (gameViewModel as DigitSpanViewModel).eventShowCorrect.observe(viewLifecycleOwner, Observer {
            if (it) {
                val correct =  (gameViewModel as DigitSpanViewModel).numbersSequence
                binding.editTextAnswer.setText(correct)
                binding.editTextAnswer.setTextColor(ContextCompat.getColor(requireContext(), R.color.gnt_red))
            } else {
                binding.editTextAnswer.text = null
                binding.editTextAnswer.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_darkgray))
            }
        })
    }

    private fun showNumbers() {
        binding.textQuestion.visibility = View.VISIBLE
        binding.textQuestion2.visibility = View.GONE
        binding.cardAnswer.visibility = View.GONE
        binding.cardNumber.visibility = View.VISIBLE
        binding.buttonSubmit.alpha = 0.5f
    }

    private fun showEditText() {
        binding.textQuestion.visibility = View.GONE
        binding.textQuestion2.visibility = View.VISIBLE
        binding.cardAnswer.visibility = View.VISIBLE
        binding.cardNumber.visibility = View.GONE
        binding.buttonSubmit.alpha = 1f
    }

}