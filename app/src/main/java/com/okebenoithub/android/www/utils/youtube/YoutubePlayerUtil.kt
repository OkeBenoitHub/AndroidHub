package com.okebenoithub.android.www.utils.youtube

import android.content.Context
import androidx.lifecycle.Lifecycle
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import javax.inject.Inject

/**
 * Youtube Player Util
 * This class is used to initialize youtube player view
 */
class YoutubePlayerUtil @Inject constructor(context: Context) {
    /**
     * Init sample youtube player view
     * @param lifecycle
     * @param youTubePlayerView
     * @param initYoutubePlayerViewCallback
     */
    fun initYoutubePlayerView(lifecycle: Lifecycle, youTubePlayerView: YouTubePlayerView,initYoutubePlayerViewCallback: InitYoutubePlayerViewCallback) {
        // add lifecycle observer
        lifecycle.addObserver(youTubePlayerView)
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                initYoutubePlayerViewCallback.onYoutubePlayerReady(youTubePlayer)
            }

            override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerState) {
                super.onStateChange(youTubePlayer, state)
                initYoutubePlayerViewCallback.onYoutubePlayerStateChange(state,youTubePlayer)
            }

            override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
                super.onError(youTubePlayer, error)
                initYoutubePlayerViewCallback.onYoutubePlayerError(youTubePlayer,error)
            }
        })
    }

    /**
     * Remove youtube player view
     * @param lifecycle
     * @param youTubePlayerView
     */
    fun removeYtPlayerListener(lifecycle: Lifecycle, youTubePlayerView: YouTubePlayerView) {
        lifecycle.removeObserver(youTubePlayerView)
        youTubePlayerView.removeYouTubePlayerListener(object : AbstractYouTubePlayerListener() {})
    }

    /**
     * Player state constants
     * buffer, paused, playing
     */
    companion object {
        const val PLAYER_STATE_BUFFERING = "BUFFERING"
        const val PLAYER_STATE_PAUSED = "PAUSED"
        const val PLAYER_STATE_PLAYING = "PLAYING"
    }

    /**
     * Interface callback for when youtube player
     * view get initialized
     */
    interface InitYoutubePlayerViewCallback {
        fun onYoutubePlayerReady(youTubePlayer: YouTubePlayer)
        fun onYoutubePlayerStateChange(state: PlayerState?, youTubePlayer: YouTubePlayer)
        fun onYoutubePlayerError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError?)
    }
}