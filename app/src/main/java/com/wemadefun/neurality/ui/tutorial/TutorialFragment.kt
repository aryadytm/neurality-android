package com.wemadefun.neurality.ui.tutorial

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.AssetDataSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.util.Util
import com.wemadefun.neurality.databinding.FragmentTutorialBinding

/**
 * A simple [Fragment] subclass.
 */
class TutorialFragment : Fragment() {
    private lateinit var exoPlayer: SimpleExoPlayer
    private lateinit var binding: FragmentTutorialBinding

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTutorialBinding.inflate(inflater, container, false)
        val gameData = TutorialFragmentArgs.fromBundle(requireArguments()).argGameData

        binding.textTutorial.text = "Tutorial: ${gameData.title}"
        binding.textDescription.text = gameData.tutorialText
        binding.buttonBack.setOnClickListener { findNavController().navigateUp() }
        return binding.root
    }

    private fun initializePlayer() {
        val mediaSource = buildMediaSource()
        exoPlayer = ExoPlayerFactory.newSimpleInstance(requireActivity().baseContext);
        exoPlayer.prepare(mediaSource, true, true);
        exoPlayer.seekTo(currentWindow, playbackPosition);
        exoPlayer.playWhenReady = playWhenReady;
        binding.simpleExoPlayerView.requestFocus()
        binding.simpleExoPlayerView.player = exoPlayer

    }

    private fun releasePlayer()
    {
        playWhenReady = exoPlayer.playWhenReady
        playbackPosition = exoPlayer.currentPosition
        currentWindow = exoPlayer.currentWindowIndex
        exoPlayer.release()
    }

    private fun buildMediaSource(): MediaSource? {
        val dataSourceFactory: DataSource.Factory = DataSource.Factory { AssetDataSource(requireActivity().baseContext) }
        val uriString = TutorialFragmentArgs.fromBundle(requireArguments()).argGameData.tutorialUri
        val uri = Uri.parse(uriString)
        return ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24) {
            initializePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }
}
