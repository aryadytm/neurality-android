package com.wemadefun.neurality.ui.help

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.wemadefun.neurality.R
import com.wemadefun.neurality.databinding.FragmentHelpBinding
import com.wemadefun.neurality.firebaseutils.fireremote.FireRemote

class HelpFragment : Fragment() {

    private lateinit var binding: FragmentHelpBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHelpBinding.inflate(inflater, container, false)

        val helpHashMap = FireRemote.jsonHelpUrls
        val helpTitle : List<String> = helpHashMap.keys.toList()
        val helpUrl: List<Any> = helpHashMap.values.toList()
        val listAdapter = ArrayAdapter<String>(requireContext(), R.layout.item_help, helpTitle)

        binding.listHelp.adapter = listAdapter
        binding.listHelp.setOnItemClickListener { _: AdapterView<*>, _: View, pos: Int, _: Long ->
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(helpUrl[pos].toString()))
            startActivity(browserIntent)
        }
        binding.buttonBack.setOnClickListener { findNavController().navigateUp() }
        return binding.root
    }

}