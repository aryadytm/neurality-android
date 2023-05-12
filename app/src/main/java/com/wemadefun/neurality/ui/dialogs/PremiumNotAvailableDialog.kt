package com.wemadefun.neurality.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.wemadefun.neurality.R

class PremiumNotAvailableDialog : DialogFragment() {

    private var onButtonClickListener: () -> Unit = {}
    private var onCancelListener: () -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.not_available_msg))
            builder.setMessage(getString(R.string.premium_not_available_msg))
                .setPositiveButton("OK") { _, _ ->
                    onButtonClickListener()
                    dismiss()
                }
                .setOnCancelListener {
                    onCancelListener()
                    dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun setOnButtonClickListener(listener: () -> Unit) {
        onButtonClickListener = listener
    }

    fun setOnCancelListener(listener: () -> Unit) {
        onCancelListener = listener
    }
}
