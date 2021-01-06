package com.ms.playstop.ui.playVideo

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProviders
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.ms.playstop.R
import com.ms.playstop.extension.hide
import com.ms.playstop.extension.show
import kotlinx.android.synthetic.main.activity_play_video.*
import kotlinx.android.synthetic.main.exo_playback_control_view.*


class PlayVideoActivity : AppCompatActivity(), Player.EventListener {

    companion object {
        const val PLAY_VIDEO_URL = "PLAY VIDEO URL"
    }

    private lateinit var viewModel: PlayVideoViewModel
    private var exoPlayer: ExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L
    private lateinit var videoUrl : String
    private var status: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
            w.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)
        videoUrl = intent?.takeIf { it.hasExtra(PLAY_VIDEO_URL) }?.getStringExtra(PLAY_VIDEO_URL) ?: ""
        viewModel = ViewModelProviders.of(this).get(PlayVideoViewModel::class.java)
        handleConfigurationChange()
        play_fullscreen?.setOnClickListener {
            changeOrientation()
        }
        play_back?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun changeOrientation() {
        val currentOrientation = resources.configuration.orientation
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    override fun onResume() {
        super.onResume()
        initializePlayer(videoUrl)
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        handleConfigurationChange()
    }

    private fun handleConfigurationChange() {
        val currentOrientation = resources.configuration.orientation
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemUiFullScreen()
            play_fullscreen?.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.ic_exit_fullscreen))
        } else {
            hideSystemUi()
            play_fullscreen?.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.ic_fullscreen))
        }

    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUiFullScreen() {
        play_video_player?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        play_video_player?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private fun initializePlayer(url: String) {
        if(exoPlayer == null) {
            val trackSelector = DefaultTrackSelector()
            val loadControl = DefaultLoadControl()
            val renderersFactory = DefaultRenderersFactory(this)
            exoPlayer = ExoPlayerFactory.newSimpleInstance(
                renderersFactory, trackSelector, loadControl
            )
            play_video_player?.player = exoPlayer
        }
        val mediaSource = buildMediaSource(Uri.parse(url))
        exoPlayer?.addListener(this)
        exoPlayer?.prepare(mediaSource)
        exoPlayer?.seekTo(currentWindow, playbackPosition)
        exoPlayer?.playWhenReady = playWhenReady
    }

    private fun buildMediaSource(uri: Uri): MediaSource {

        val userAgent = "exoplayer-playstop"

//        return ExtractorMediaSource
//            .Factory(DefaultDataSourceFactory(this, userAgent))
//            .setExtractorsFactory(DefaultExtractorsFactory())
//            .createMediaSource(uri)

        return if (uri.lastPathSegment?.contains("mkv") == true || uri.lastPathSegment?.contains("mp4") == true) {
            ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(uri)
        } else if (uri.lastPathSegment?.contains("m3u8") == true) {
            HlsMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(uri)
        } else {
            val dashChunkSourceFactory = DefaultDashChunkSource.Factory(
                DefaultHttpDataSourceFactory("ua", null))
            val manifestDataSourceFactory = DefaultHttpDataSourceFactory(userAgent)
            DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory).createMediaSource(uri)
        }
    }

    private fun releasePlayer() {
        if (exoPlayer != null) {
            playbackPosition = exoPlayer?.currentPosition ?: 0L
            currentWindow = exoPlayer?.currentWindowIndex ?: 0
            playWhenReady = exoPlayer?.playWhenReady ?: false
            exoPlayer?.removeListener(this)
            exoPlayer?.release()
            exoPlayer = null
        }
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {

    }

    override fun onSeekProcessed() {

    }

    override fun onTracksChanged(
        trackGroups: TrackGroupArray?,
        trackSelections: TrackSelectionArray?
    ) {

    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        Toast.makeText(this,error?.localizedMessage,Toast.LENGTH_SHORT).show()
    }

    override fun onLoadingChanged(isLoading: Boolean) {

    }

    override fun onPositionDiscontinuity(reason: Int) {
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        when (playbackState) {
            Player.STATE_BUFFERING ->  {
                status = PlaybackStatus.LOADING
                play_video_progress?.show()
            }
            Player.STATE_ENDED -> {
                status = PlaybackStatus.STOPPED
                play_video_progress?.hide()
            }
            Player.STATE_IDLE -> {
                status = PlaybackStatus.IDLE
                play_video_progress?.hide()
            }
            Player.STATE_READY -> {
                if (playWhenReady) {
                    status = PlaybackStatus.PLAYING
                    play_video_progress?.hide()
                }
                else {
                    status = PlaybackStatus.PAUSED
                    play_video_progress?.hide()
                }
            }
            else -> {
                status = PlaybackStatus.IDLE
                play_video_progress?.hide()
            }
        }
    }

    fun pausePlayer() {
        exoPlayer?.playWhenReady = false
        exoPlayer?.playbackState
    }
    fun startPlayer() {
        exoPlayer?.playWhenReady = true
        exoPlayer?.playbackState
    }

}