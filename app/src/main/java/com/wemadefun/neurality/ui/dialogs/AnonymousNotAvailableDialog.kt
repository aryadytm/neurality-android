package com.wemadefun.neurality.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.wemadefun.neurality.R

class AnonymousNotAvailableDialog : DialogFragment() {

    private var onPositiveClick: () -> Unit = {}
    private var onNegativeClick: () -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(getString(R.string.dialog_must_authenticated))
                .setPositiveButton(getString(R.string.dialog_ok)) { dialog, id ->
                    onPositiveClick()
                    dismiss()
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun setOnPositiveClick(l: () -> Unit) {
        onPositiveClick = l
    }

    fun setOnNegativeClick(l: () -> Unit) {
        onNegativeClick = l
    }
}