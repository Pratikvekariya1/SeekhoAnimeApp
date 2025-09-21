package com.seekho.animeapp.utils

import android.content.Context
import android.view.View
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.PlaybackException
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoPlayerManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var exoPlayer: ExoPlayer? = null
    private var youTubePlayer: YouTubePlayer? = null

    /**
     * Initialize appropriate player based on URL type
     */
    fun setupPlayer(
        url: String,
        youTubePlayerView: YouTubePlayerView,
        exoPlayerView: PlayerView,
        networkStateManager: NetworkStateManager,
        onError: (String) -> Unit,
        onReady: () -> Unit,
        onNetworkWarning: () -> Unit = {}
    ) {

        // Check network before attempting to play
        if (!canPlayVideo(networkStateManager)) {
            onError("Internet connection required for video playback")
            return
        }

        // Show data usage warning for mobile connections
        if (networkStateManager.isConnectedToMobileData()) {
            onNetworkWarning()
        }

        when (val urlType = VideoUrlDetector.detectVideoUrlType(url)) {
            is VideoUrlDetector.VideoUrlType.YouTube -> {
                setupYouTubePlayer(urlType.videoId, youTubePlayerView, exoPlayerView, onError, onReady)
            }
            is VideoUrlDetector.VideoUrlType.Direct -> {
                setupExoPlayer(urlType.url, youTubePlayerView, exoPlayerView, onError, onReady)
            }
            is VideoUrlDetector.VideoUrlType.Invalid -> {
                onError("Invalid video URL")
            }
        }
    }

    private fun setupYouTubePlayer(
        videoId: String,
        youTubePlayerView: YouTubePlayerView,
        exoPlayerView: PlayerView,
        onError: (String) -> Unit,
        onReady: () -> Unit
    ) {
        // Hide ExoPlayer, show YouTube Player
        exoPlayerView.visibility = View.GONE
        youTubePlayerView.visibility = View.VISIBLE

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                this@VideoPlayerManager.youTubePlayer = youTubePlayer
                youTubePlayer.cueVideo(videoId, 0f)
                onReady()
            }

            override fun onError(
                youTubePlayer: YouTubePlayer,
                error: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerError
            ) {
                onError("YouTube player error: ${error.name}")
            }
        })
    }

    private fun setupExoPlayer(
        videoUrl: String,
        youTubePlayerView: YouTubePlayerView,
        exoPlayerView: PlayerView,
        onError: (String) -> Unit,
        onReady: () -> Unit
    ) {
        // Hide YouTube Player, show ExoPlayer
        youTubePlayerView.visibility = View.GONE
        exoPlayerView.visibility = View.VISIBLE

        try {
            // Release any existing player
            releaseExoPlayer()

            // Create new ExoPlayer
            exoPlayer = ExoPlayer.Builder(context).build()
            exoPlayerView.player = exoPlayer

            // Create MediaItem with appropriate MIME type
            val mimeType = VideoUrlDetector.getMimeType(videoUrl)
            val mediaItem = MediaItem.Builder()
                .setUri(videoUrl)
                .setMimeType(mimeType)
                .build()

            exoPlayer?.apply {
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = false // Don't auto-play, let user control

                // Add listener for playback state changes - CORRECTED IMPORT
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_READY -> {
                                onReady()
                            }
                            Player.STATE_ENDED -> {
                                // Handle video ended if needed
                            }
                            Player.STATE_BUFFERING -> {
                                // Handle buffering state
                            }
                            Player.STATE_IDLE -> {
                                // Handle idle state
                            }
                        }
                    }

                    override fun onPlayerError(error: PlaybackException) {
                        onError("Video playback error: ${error.message}")
                    }

                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        // Handle play/pause state changes
                    }
                })
            }

        } catch (e: Exception) {
            onError("Failed to setup video player: ${e.message}")
        }
    }

    fun canPlayVideo(networkStateManager: NetworkStateManager): Boolean {
        return when {
            !networkStateManager.isNetworkAvailable() -> {
                false // No internet = no video streaming
            }
            networkStateManager.isConnectedToMobileData() -> {
                // Show warning but allow playback
                true
            }
            else -> true
        }
    }

    /**
     * Release ExoPlayer resources
     */
    private fun releaseExoPlayer() {
        exoPlayer?.release()
        exoPlayer = null
    }

    /**
     * Release all player resources
     */
    fun releaseAllPlayers() {
        releaseExoPlayer()
        youTubePlayer = null
    }

    /**
     * Handle lifecycle pause
     */
    fun onPause() {
        exoPlayer?.pause()
        // YouTube player handles its own lifecycle
    }

    /**
     * Handle lifecycle resume
     */
    fun onResume() {
        // ExoPlayer will resume automatically if playWhenReady was true
        // YouTube player handles its own lifecycle
    }
}
