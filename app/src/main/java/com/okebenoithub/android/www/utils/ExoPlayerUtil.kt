package com.okebenoithub.android.www.utils

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

/**
 * ExoPlayerUtil class
 * This class is used to play media files using ExoPlayer
 */
class ExoPlayerUtil(private val context: Context,private val playerView: PlayerView, val playerListener: PlayerListener? = null) {

    private var player: ExoPlayer? = null
    private val mediaItems: MutableList<MediaItem> = mutableListOf()

    init {
        initializePlayer()
    }

    interface PlayerListener {
        fun onPlaybackStateChanged(playbackState: Int)
        fun onPlayerError(error: PlaybackException)
        fun onIsPlayingChanged(isPlaying: Boolean)
        // Add more listener methods if needed
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(context).build()
        playerView.player = player

        player?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> playerListener?.onPlaybackStateChanged(Player.STATE_BUFFERING)
                    Player.STATE_READY -> playerListener?.onPlaybackStateChanged(Player.STATE_READY)
                    Player.STATE_ENDED -> playerListener?.onPlaybackStateChanged(Player.STATE_ENDED)
                    Player.STATE_IDLE -> playerListener?.onPlaybackStateChanged(Player.STATE_IDLE)
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                playerListener?.onPlayerError(error)
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                playerListener?.onIsPlayingChanged(isPlaying)
            }

            // Add more listener methods if needed
        })
    }

    fun playMedia(mediaUrl: String) {
        val uri = Uri.parse(mediaUrl)
        val mediaItem = MediaItem.fromUri(uri)
        player?.apply {
            setMediaItem(mediaItem)
            prepare()
            play()
        }
    }

    fun playAudio(mediaUrl: String) {
        val uri = Uri.parse(mediaUrl)
        val mediaItem = MediaItem.fromUri(uri)
        player?.apply {
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    fun addSubtitle(mediaUri: Uri, mimeType: String) {
        val subtitle = MediaItem.SubtitleConfiguration.Builder(mediaUri)
            .setMimeType(mimeType)
            .build()
        val mediaItem = MediaItem.Builder()
            .setUri(mediaUri)
            .setSubtitleConfigurations(listOf(subtitle))
            .build()
        mediaItems.add(mediaItem)
    }

    fun addMediaItem(mediaUri: Uri) {
        val mediaItem = MediaItem.fromUri(mediaUri)
        mediaItems.add(mediaItem)
    }

    fun clearMediaItems() {
        mediaItems.clear()
    }

    fun playMediaItemAtIndex(index: Int, mainUtil: MainUtil) {
        if (index in mediaItems.indices) {
            player?.apply {
                setMediaItem(mediaItems[index])
                prepare()
                play()
            }
        } else {
            mainUtil.showToastMessage(context,"Invalid media item index")
        }
    }

    @OptIn(UnstableApi::class)
    fun playNext() {
        player?.next()
    }

    @OptIn(UnstableApi::class)
    fun playPrevious() {
        player?.previous()
    }

    fun stop() {
        player?.stop()
    }

    fun pause() {
        player?.pause()
    }

    fun resume() {
        player?.play()
    }

    fun release() {
        player?.release()
        player = null
    }
}