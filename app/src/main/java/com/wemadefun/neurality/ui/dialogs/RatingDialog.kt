package com.wemadefun.neurality.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.wemadefun.neurality.BrainApplication
import com.wemadefun.neurality.R
import com.wemadefun.neurality.data.userdata.modules.ConfigModule
import com.wemadefun.neurality.databinding.DialogRatingBinding
import com.wemadefun.neurality.firebaseutils.FireAnalytics
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import javax.inject.Inject


class RatingDialog : DialogFragment() {

    @Inject lateinit var configModule: ConfigModule
    private lateinit var binding: DialogRatingBinding

    private var rating: Float = 0f
    private var threshold: Float = 4f

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as BrainApplication).appComponent.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        FireCrashlytics.log("Show Rating Dialog")
        return activity.let {
            val inflater = requireActivity().layoutInflater
            binding = DialogRatingBinding.inflate(inflater)
            setupView()

            val builder = AlertDialog.Builder(requireContext())
            builder.setView(binding.root)
            builder.create()
        }
    }

    private fun setupView() {
        binding.buttonRate.setOnClickListener { onRateButtonClick() }
        binding.buttonNever.setOnClickListener { onNeverButtonClick() }
        binding.ratingBar.numStars = 5
        binding.ratingBar.stepSize = 1f
        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, ratingValue, b ->
            rating = ratingValue
        }
    }

    private fun onRateButtonClick() {
        FireAnalytics.eventRating(rating.toString())
        when {
            (rating == 0f) -> {
                Toast.makeText(requireContext(), getString(R.string.please_enter_rating), Toast.LENGTH_SHORT).show()
                return
            }
            (rating >= threshold) -> redirectToPlaystorePage()
            (rating < threshold) -> showFeedbackDialog()
        }
        dismiss()
    }

    private fun onNeverButtonClick() {
        FireAnalytics.eventRating("never")
        configModule.onUserHasRated()
        dismiss()
    }

    private fun showFeedbackDialog() {
        configModule.onUserHasRated()
        val feedbackDialog = FeedbackDialog()
        feedbackDialog.show(requireActivity().supportFragmentManager, "FeedbackDialog")
    }

    private fun redirectToPlaystorePage() {
        configModule.onUserHasRated()
        FireCrashlytics.log("Redirect to Play Store Page")
        FireAnalytics.eventPositiveStar()

        val appPackageName: String = requireActivity().packageName
        try {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$appPackageName")))
        } catch (anfe: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }
    }


}