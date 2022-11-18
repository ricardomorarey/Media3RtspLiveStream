package com.example.tssexoprueba

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        exoplayer(this, 1)
    }

    fun exoplayer(context: Context, numberSurfecaeView: Int) {

        when (numberSurfecaeView) {
            1 -> {
                //Get View and put style
                val playerView = findViewById<StyledPlayerView>(R.id.player_view)
                playerView.requestFocus()
                // Create a player instance.
                val player = ExoPlayer.Builder(context).build()
                // Create an RTSP media source pointing to an RTSP uri and override the socket factory.
                val mediaSource: MediaSource = RtspMediaSource.Factory()
                    .createMediaSource(
                        MediaItem.fromUri(
                            "rtsp://admin:Demes2323@hik1pro2.davidsat.dnsdemes.com:554/ISAPI/Streaming/Channels/301"
                        )
                    )
                // Set the media source to be played.
                player.setMediaSource(mediaSource)
                player.playWhenReady = true
                playerView.player = player
                // Prepare the player.
                player.prepare()
            }
        }
    }
}