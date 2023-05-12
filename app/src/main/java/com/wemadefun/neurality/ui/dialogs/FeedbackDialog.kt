package com.wemadefun.neurality.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.wemadefun.neurality.BrainApplication
import com.wemadefun.neurality.R
import com.wemadefun.neurality.data.userdata.modules.ConfigModule
import com.wemadefun.neurality.databinding.DialogFeedbackBinding
import com.wemadefun.neurality.firebaseutils.FireCrashlytics
import com.wemadefun.neurality.firebaseutils.FireLogger
import javax.inject.Inject

class FeedbackDialog : DialogFragment() {

    @Inject lateinit var configModule: ConfigModule
    private lateinit var binding: DialogFeedbackBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as BrainApplication).appComponent.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        FireCrashlytics.log("Show Feedback Dialog")
        return activity.let {
            val inflater = requireActivity().layoutInflater
            binding = DialogFeedbackBinding.inflate(inflater)
            binding.buttonSubmit.setOnClickListener { onSubmitClick() }
            binding.buttonCancel.setOnClickListener { onCancelClick() }

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.pref_feedback))
            builder.setView(binding.root)
            builder.create()
        }
    }

    private fun onSubmitClick() {
        val text = binding.editText.text.toString()
        FireCrashlytics.log("Feedback: $text")

        if (text.length > 1) {
            FireLogger.sendFeedback(text)
            configModule.onUserHasRated()
            dismiss()
        }
        else {
            Toast.makeText(requireContext(), getString(R.string.please_enter_feedback), Toast.LENGTH_SHORT).show()
        }
    }

    private fun onCancelClick() {
        configModule.onUserHasRated()
        dismiss()
    }

}