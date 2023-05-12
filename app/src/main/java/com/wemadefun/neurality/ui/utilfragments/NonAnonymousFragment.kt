package com.wemadefun.neurality.ui.utilfragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.wemadefun.neurality.BrainApplication
import com.wemadefun.neurality.data.userdata.modules.DataModule
import com.wemadefun.neurality.ui.dialogs.AnonymousNotAvailableDialog
import javax.inject.Inject

abstract class NonAnonymousFragment : Fragment() {

    @Inject lateinit var dataModule: DataModule
    protected var isAnonymous = false

    override fun onAttach(context: Context) {
        (requireActivity().application as BrainApplication).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isAnonymous = dataModule.isAnonymous

        if (isAnonymous) {
            findNavController().navigateUp()
            showNotAvailableDialog()
        }
    }

    private fun showNotAvailableDialog() {
        AnonymousNotAvailableDialog().show(requireActivity().supportFragmentManager, "AnonymousNotAvailableDialog")
    }

}