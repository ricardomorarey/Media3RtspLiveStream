package com.example.tssexoprueba

import android.content.ContentValues
import android.content.Context
import android.util.AndroidException
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlaybackException
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.rtsp.RtspMediaSource
import androidx.media3.exoplayer.text.ExoplayerCuesDecoder
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import com.example.tssexoprueba.databinding.ActivityMainBinding
import java.lang.Error


class Media3Streaming(context: Context, binding: ActivityMainBinding) {

    private var player: ExoPlayer? = null
    var viewBinding = binding
    var context = context

    private val playbackStateListener: Player.Listener = playbackStateListener()

    private var playWhenReady = true
    private var currentItem = 0
    private var playbackPosition = 0L

    private var isPlayMedia = false

    fun initializePlayer(rtspUri: String) {

        try {
        val trackSelector = DefaultTrackSelector(context).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }

            player = ExoPlayer.Builder(context)
                .setTrackSelector(trackSelector)
                .setMediaSourceFactory(RtspMediaSource.Factory().setDebugLoggingEnabled(true))
                .build()
                .also { exoPlayer ->

                    viewBinding.videoView.player = exoPlayer

                    val mediaItem = MediaItem.Builder()
                        .setUri(rtspUri)
                        .setMimeType(MimeTypes.APPLICATION_RTSP)
                        .build()
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.playWhenReady = playWhenReady
                    exoPlayer.seekTo(currentItem, playbackPosition)
                    exoPlayer.addListener(playbackStateListener)
                    exoPlayer.prepare()
                    isPlayMedia = true
                }
        } catch (e: ExoPlaybackException) {
            Log.e("ERROR", e.toString())
        }
    }

    fun setOnVolume(){
        player!!.volume = 1.0F
    }

    fun setOffVolume(){
        player!!.volume = 0F
    }

    fun releasePlayer() {
        player?.let { exoPlayer ->
            playbackPosition = exoPlayer.currentPosition
            currentItem = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.removeListener(playbackStateListener)
            exoPlayer.release()
        }
        player = null
        isPlayMedia = false
    }

    fun isPlayMedia(): Boolean {
        return isPlayMedia
    }
}

fun playbackStateListener() = object : Player.Listener {
    override fun onPlaybackStateChanged(playbackState: Int) {
        val stateString: String = when (playbackState) {
            ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE      -"
            ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING -"
            ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY     -"
            ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED     -"
            else -> "UNKNOWN_STATE             -"
        }
        Log.d(ContentValues.TAG, "changed state to $stateString")
    }
}

