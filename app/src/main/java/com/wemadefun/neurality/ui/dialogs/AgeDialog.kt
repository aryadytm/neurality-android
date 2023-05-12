package com.wemadefun.neurality.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.wemadefun.neurality.R

class AgeDialog : DialogFragment() {

    private var onAboveEighteenClick: () -> Unit = {}
    private var onBelowEighteenClick: () -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(getString(R.string.signin_dialog_age))
                .setPositiveButton(getString(R.string.signin_dialog_overeighteen)) { dialog, id ->
                    onAboveEighteenClick()
                    dismiss()
                }
                .setNegativeButton(getString(R.string.signin_dialog_undereighteen)) { dialog, id ->
                    onBelowEighteenClick()
                    dismiss()
                }
                .setCancelable(false)
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun setOnAgeOverEighteen(l: () -> Unit) {
        onAboveEighteenClick = l
    }

    fun setOnAgeUnderEighteen(l: () -> Unit) {
        onBelowEighteenClick = l
    }
}