package com.wemadefun.neurality.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.wemadefun.neurality.R

class ExitDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.exit_neurality))
            builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(R.string.yes) { dialog, id ->
                    requireActivity().finish()
                }
                .setNegativeButton(R.string.cancel) { dialog, id -> dismiss() }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}