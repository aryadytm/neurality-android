package com.wemadefun.neurality.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.wemadefun.neurality.R

class AnonymousSigninDialog : DialogFragment() {

    private var onPositiveClick: () -> Unit = {}
    private var onNegativeClick: () -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(getString(R.string.signin_dialog_anonymous_warning))
                .setPositiveButton(getString(R.string.signin_dialog_anonymous_continue)) { dialog, id ->
                    onPositiveClick()
                    dismiss()
                }
                .setNegativeButton(getString(R.string.signin_dialog_anonymous_back)) { dialog, id ->
                    onNegativeClick()
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