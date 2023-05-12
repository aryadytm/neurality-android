package com.wemadefun.neurality.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.wemadefun.neurality.R
import com.wemadefun.neurality.firebaseutils.FireCrashlytics

class InternetErrorDialog : DialogFragment() {

    private var onButtonClickListener: () -> Unit = {}
    private var onCancelListener: () -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        FireCrashlytics.log("Show Internet Error Dialog")
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.error_offline)
            builder.setMessage(R.string.error_db_offline)
                .setPositiveButton("OK") { _, _ ->
                    onButtonClickListener()
                    dismiss()
                }
            builder.setOnCancelListener {
                onCancelListener()
                dismiss()
            }
            // Create the AlertDialog object and return it
            builder.setCancelable(true)
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
