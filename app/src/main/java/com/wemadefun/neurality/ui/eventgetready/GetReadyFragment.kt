package com.wemadefun.neurality.ui.eventgetready

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wemadefun.neurality.R
import com.wemadefun.neurality.databinding.FragmentGetreadyBinding
import com.wemadefun.neurality.utils.ARG_GAME_DATA

class GetReadyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGetreadyBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this).get(GetReadyViewModel::class.java)

        // callback var
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            (activity as AppCompatActivity).window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            (activity as AppCompatActivity).supportActionBar?.show()
            findNavController().popBackStack(R.id.nav_train, false)
        }

        viewModel.eventStartGame.observe(viewLifecycleOwner, Observer {
            if (it) {
                val gameData = GetReadyFragmentArgs.fromBundle(requireArguments()).argGameData
                val args = bundleOf(ARG_GAME_DATA to gameData)
                findNavController().navigate(gameData.navigationId, args)
                viewModel.doneNavigating()
            }
        })
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

}
