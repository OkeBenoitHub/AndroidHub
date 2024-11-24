package com.okebenoithub.android.www.ui.media

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.media3.common.PlaybackException
import com.okebenoithub.android.www.databinding.FragmentMediaFileBinding
import com.okebenoithub.android.www.ui.activities.MainActivity
import com.okebenoithub.android.www.ui.media.viewModel.MediaFileViewModel
import com.okebenoithub.android.www.utils.ExoPlayerUtil

class MediaFileFragment : Fragment() {

    private var _binding: FragmentMediaFileBinding? = null
    // This property is only valid between onCreateView and
    private val binding get() = _binding!!

    private lateinit var media3ExoPlayerUtil: ExoPlayerUtil
    private var isLocalPermissionsGranted = false

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                isLocalPermissionsGranted = true
            } else {
                // Handle permission denied case
                // show a message
                (activity as MainActivity).mainUtil.showToastMessage(requireContext(), "Permission denied")
            }
        }

    // Play local media files using ExoPlayer
    private fun playLocalMediaFiles(binding: FragmentMediaFileBinding) {
        // Replace with your actual local file paths
        val videoUri = Uri.parse("file:///path/to/local/video.mp4")
        val audioUri = Uri.parse("file:///path/to/local/audio.mp3")

        // play local video
        binding.playAudio.setOnClickListener {
            binding.mediaPlayerView.visibility = View.VISIBLE
            media3ExoPlayerUtil.playAudio(videoUri.toString())
        }

        // Uncomment to play local audio
        // media3ExoPlayerUtil.playAudio(audioUri)

        // Add subtitles
        val subtitleUri = Uri.parse("file:///path/to/local/subtitle.srt")
        media3ExoPlayerUtil.addSubtitle(subtitleUri, "application/x-subrip")
    }

    // Setup playlist for ExoPlayer
    private fun setupPlaylist() {
        // Replace with your actual local file paths
        val videoUri1 = Uri.parse("file:///path/to/local/video1.mp4")
        val videoUri2 = Uri.parse("file:///path/to/local/video2.mp4")
        val audioUri1 = Uri.parse("file:///path/to/local/audio1.mp3")
        val audioUri2 = Uri.parse("file:///path/to/local/audio2.mp3")

        // Add media items to the playlist
        media3ExoPlayerUtil.addMediaItem(videoUri1)
        media3ExoPlayerUtil.addMediaItem(videoUri2)
        media3ExoPlayerUtil.addMediaItem(audioUri1)
        media3ExoPlayerUtil.addMediaItem(audioUri2)

        // Play the first media item
        media3ExoPlayerUtil.playMediaItemAtIndex(0, (activity as MainActivity).mainUtil)
        /*
        // Play next media item
        binding.playNext.setOnClickListener {
            media3ExoPlayerUtil.playNext()
        }
        // Play previous media item
        binding.playPrevious.setOnClickListener {
            media3ExoPlayerUtil.playPrevious()
        }*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            isLocalPermissionsGranted = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaFileBinding.inflate(inflater, container, false)

        // init view model
        val mediaFileViewModel: MediaFileViewModel by activityViewModels { (activity as MainActivity).mediaFileViewModelFactory }
        binding.viewModel = mediaFileViewModel
        // init media player
        media3ExoPlayerUtil = ExoPlayerUtil(requireContext(), binding.mediaPlayerView, object: ExoPlayerUtil.PlayerListener {

            override fun onPlaybackStateChanged(playbackState: Int) {
                (activity as MainActivity).mainUtil.showToastMessage(requireContext(), "Playback state changed: $playbackState")
            }

            override fun onPlayerError(error: PlaybackException) {
                (activity as MainActivity).mainUtil.showToastMessage(requireContext(), "Player error: ${error.message}")
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                (activity as MainActivity).mainUtil.showToastMessage(requireContext(), "Is playing: $isPlaying")
            }
        })

        // play audio
        binding.playAudio.setOnClickListener {
            binding.mediaPlayerView.visibility = View.VISIBLE
            media3ExoPlayerUtil.playAudio("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
        }
        // play video
        binding.playVideo.setOnClickListener {
            binding.mediaPlayerView.visibility = View.VISIBLE
            media3ExoPlayerUtil.playMedia("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
        }

        // Play local media files
        if (isLocalPermissionsGranted) playLocalMediaFiles(binding)

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        media3ExoPlayerUtil.pause()
    }

    override fun onResume() {
        super.onResume()
        media3ExoPlayerUtil.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        media3ExoPlayerUtil.release()
        _binding = null
    }
}