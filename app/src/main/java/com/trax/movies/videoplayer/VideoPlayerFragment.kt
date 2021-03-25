package com.trax.movies.videoplayer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.EventLogger
import com.trax.movies.R
import com.trax.movies.databinding.FragmentDetailMovieBinding
import com.trax.movies.databinding.FragmentVideoplayerBinding
import com.trax.movies.databinding.FragmentVideoplayerBinding.bind
import com.trax.movies.ui.viewBinding


class VideoPlayerFragment: Fragment(R.layout.fragment_videoplayer) {

    private val videoPlayerBinding by viewBinding(::bind)

    private var player: SimpleExoPlayer? = null

    private val ARG_MOVIE_URL = "MOVIES_URL"

    private var movieUrl: String = ""

    companion object {
        const val KEY_PLAYER_POSITION = "KEY_PLAYER_POSITION"
        const val KEY_PLAYER_PLAY_WHEN_READY = "KEY_PLAYER_PLAY_WHEN_READY"
    }

    private val moviesDetailBinding by viewBinding(FragmentVideoplayerBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restoreState(savedInstanceState)

        player = SimpleExoPlayer.Builder(view.context).build()
        videoPlayerBinding.videoPlayer.player = player

        restorePlayer(player, savedInstanceState)

        val mediaItem: MediaItem = MediaItem.fromUri(movieUrl)
        player?.setMediaItem(mediaItem)

        player?.addAnalyticsListener( EventLogger(null))
        player?.playWhenReady = true
        player?.prepare()
        player?.play()
    }

    private fun restorePlayer(player: SimpleExoPlayer?, savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            player?.seekTo(savedInstanceState.getLong(KEY_PLAYER_POSITION))
        }
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_MOVIE_URL)) {
            movieUrl = savedInstanceState.getString(ARG_MOVIE_URL) ?: ""
        }

        arguments?.apply {
            movieUrl = VideoPlayerFragmentArgs.fromBundle(this).movieUrl
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(ARG_MOVIE_URL, movieUrl)

        player?.let {
            outState.putLong(KEY_PLAYER_POSITION, it.contentPosition)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        player?.release()
    }
}