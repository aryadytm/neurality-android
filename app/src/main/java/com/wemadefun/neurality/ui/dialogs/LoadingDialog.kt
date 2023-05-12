package com.wemadefun.neurality.ui.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.airbnb.lottie.LottieDrawable.INFINITE
import com.wemadefun.neurality.R
import com.wemadefun.neurality.databinding.DialogLoadingBinding
import com.wemadefun.neurality.firebaseutils.FireCrashlytics

class LoadingDialog(private val text: String) : DialogFragment() {

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        FireCrashlytics.log("Show Loading Dialog: $text")
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val inflater = it.layoutInflater
            val binding = DialogLoadingBinding.inflate(inflater)

            binding.root.background = resources.getDrawable(R.drawable.dialog_curved)
            binding.titleText.text = text
            binding.bar.visibility = View.VISIBLE
            binding.bar.repeatCount = INFINITE
            binding.bar.setAnimation("lottie_rocket.json")
            binding.bar.playAnimation()

            val builder = AlertDialog.Builder(it)
            builder.setView(binding.root)
            builder.setCancelable(false)
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }

}