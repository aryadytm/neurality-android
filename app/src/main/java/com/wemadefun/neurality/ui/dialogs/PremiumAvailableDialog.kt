package com.wemadefun.neurality.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.wemadefun.neurality.R

class PremiumAvailableDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.good_news))
            builder.setMessage(getString(R.string.premium_available_msg))
                .setPositiveButton(getString(R.string.get_it_now)) { _, _ ->
                    findNavController().navigate(R.id.action_subscription)
                    dismiss()
                }
                .setNegativeButton(R.string.later) { _, _ -> dismiss() }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
