package com.ms.playstop.ui.playVideo

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageButton
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.*
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.*
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textview.MaterialTextView
import com.ms.playstop.R
import com.ms.playstop.extension.hide
import com.ms.playstop.extension.show
import com.ms.playstop.ui.playVideo.adapter.RadioLinkAdapter
import kotlinx.android.synthetic.main.activity_play_video.*
import kotlinx.android.synthetic.main.exo_playback_control_view.*


class PlayVideoActivity : AppCompatActivity(), Player.EventListener,
    RadioLinkAdapter.OnItemClickListener {

    companion object {
        const val PLAY_VIDEO_URL = "PLAY VIDEO URL"
        const val PLAY_VIDEO_SUBTITLES = "PLAY VIDEO SUBTITLES"
    }

    private var subtitleDialog: BottomSheetDialog? = null
    private lateinit var viewModel: PlayVideoViewModel
    private var exoPlayer: ExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L
    private lateinit var videoUrl : String
    private var subtitles : ArrayList<String>? = null
    private var selectedSubtitle : String? = null
    private var showSubtitle = true
    private var status: String? = null
    lateinit var trackSelector: DefaultTrackSelector
    private var hardSubHandled = false
    private var selectedPosition = 0

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
        subtitles = intent?.takeIf { it.hasExtra(PLAY_VIDEO_SUBTITLES) }?.getStringArrayListExtra(PLAY_VIDEO_SUBTITLES)
        if(subtitles.isNullOrEmpty().not()) {
            selectedSubtitle = subtitles?.firstOrNull()
        }
        viewModel = ViewModelProviders.of(this).get(PlayVideoViewModel::class.java)
        initViews()
        handleConfigurationChange()
    }

    private fun initViews() {
        play_subtitle?.isEnabled = subtitles.isNullOrEmpty().not()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            play_subtitle?.imageTintList = if(subtitles.isNullOrEmpty().not()) ColorStateList.valueOf(Color.WHITE) else  ColorStateList.valueOf(Color.GRAY)
        }
        play_fullscreen?.setOnClickListener {
            changeOrientation()
        }
        play_back?.setOnClickListener {
            onBackPressed()
        }
        play_subtitle?.setOnClickListener {
            showSubtitlesDialog()
        }
    }

    private fun showSubtitlesDialog() {
        subtitles?.takeIf { it.isNotEmpty() }?.let {
            subtitleDialog = BottomSheetDialog(this)
            subtitleDialog?.setContentView(R.layout.layout_bottom_sheet_recycler)
            val titleTv = subtitleDialog?.findViewById<MaterialTextView>(R.id.bottom_sheet_recycler_title_tv)
            val closeIb = subtitleDialog?.findViewById<AppCompatImageButton>(R.id.bottom_sheet_recycler_close_ib)
            val recycler = subtitleDialog?.findViewById<RecyclerView>(R.id.bottom_sheet_recycler_recycler)
            titleTv?.text = getString(R.string.choose_subtitle)
            closeIb?.setOnClickListener {
                subtitleDialog?.takeIf { it.isShowing }?.dismiss()
                subtitleDialog?.cancel()
            }
            val lm = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
            recycler?.layoutManager = lm
            val adapter = RadioLinkAdapter(it,selectedPosition,this)
            recycler?.adapter = adapter
            subtitleDialog?.show()
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
//            val params = play_video_player?.subtitleView?.layoutParams as? ViewGroup.MarginLayoutParams
//            params?.bottomMargin = resources?.getDimensionPixelSize(R.dimen.margin_large)
//            play_video_player?.subtitleView?.layoutParams = params
        } else {
            hideSystemUi()
            play_fullscreen?.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.ic_fullscreen))
//            val params = play_video_player?.subtitleView?.layoutParams as? ViewGroup.MarginLayoutParams
//            params?.bottomMargin = resources?.getDimensionPixelSize(R.dimen.margin_large)
//            play_video_player?.subtitleView?.layoutParams = params
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
            val bandwidthMeter = DefaultBandwidthMeter()
            trackSelector = DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))
            val loadControl = DefaultLoadControl()
            val renderersFactory = DefaultRenderersFactory(this)
            exoPlayer = ExoPlayerFactory.newSimpleInstance(
                renderersFactory, trackSelector, loadControl
            )
            play_video_player?.player = exoPlayer
        }
        val mediaSource = buildMediaSource(Uri.parse(url))
        subtitles?.takeIf { it.isNotEmpty() }?.let { subtitles ->
            val subtitleSources = arrayOfNulls<MediaSource>(subtitles.size)
            for (i in subtitles.indices) {
                subtitleSources[i] = buildSubtitleMediaSource(Uri.parse(subtitles[i]))
            }
            val mergedDataSource = MergingMediaSource(mediaSource,*subtitleSources)
            exoPlayer?.prepare(mergedDataSource)
        } ?: kotlin.run {
            exoPlayer?.prepare(mediaSource)
        }
        exoPlayer?.addListener(this)
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

    private fun buildSubtitleMediaSource(uri: Uri): MediaSource {

        val userAgent = "exoplayer-playstop"

        val subtitleFormat = Format.createTextSampleFormat(null,MimeTypes.APPLICATION_SUBRIP,Format.NO_VALUE,"en")

        val dataSourceFactory = DefaultDataSourceFactory(this,userAgent,DefaultBandwidthMeter())

        val subtitleSource = SingleSampleMediaSource.Factory(dataSourceFactory).createMediaSource(uri,subtitleFormat,C.TIME_UNSET)

        return subtitleSource
    }

    private fun releasePlayer() {
        if (exoPlayer != null) {
            playbackPosition = exoPlayer?.currentPosition ?: 0L
            currentWindow = exoPlayer?.currentWindowIndex ?: 0
            playWhenReady = exoPlayer?.playWhenReady ?: false
            exoPlayer?.removeListener(this)
            exoPlayer?.release()
            exoPlayer = null
            hardSubHandled = false
        }
    }

    private fun hasHardSub(): Boolean {
        return trackSelector.currentMappedTrackInfo?.getTrackGroups(C.TRACK_TYPE_VIDEO)?.takeIf { it.isEmpty.not() }?.get(0)?.takeIf { it.length > 0 }?.getFormat(0)?.let {
            it.sampleMimeType != MimeTypes.APPLICATION_SUBRIP
        } ?: false
    }

    private fun handleHardSubtitle() {
        if(hardSubHandled.not() && hasHardSub()) {
            reloadSubtitle()
            hardSubHandled = true
            if(isSubtitleDisabled()) {
                selectedPosition = -1
            }
        }
    }

    private fun reloadSubtitle() {
        if(showSubtitle && selectedSubtitle.isNullOrEmpty().not() && subtitles.isNullOrEmpty().not()) {
            val mappedTrackInfo = trackSelector.currentMappedTrackInfo
            val textGroups = mappedTrackInfo?.getTrackGroups(C.TRACK_TYPE_VIDEO) // list of captions
            val index = subtitles!!.indexOf(selectedSubtitle)
            val newIndex = if(hasHardSub()) index+1 else index
            val override = MappingTrackSelector.SelectionOverride(FixedTrackSelection.Factory(), newIndex, 0)
            trackSelector.setSelectionOverride(C.TRACK_TYPE_VIDEO, textGroups, override)
        }
    }

    private fun disableSubtitle() {
        trackSelector.setRendererDisabled(C.TRACK_TYPE_VIDEO, true)
        trackSelector.clearSelectionOverrides()
        hardSubHandled = false
    }

    private fun enableSubtitle() {
        trackSelector.setRendererDisabled(C.TRACK_TYPE_VIDEO, false)
        handleHardSubtitle()
    }

    private fun isSubtitleDisabled(): Boolean {
        return trackSelector.getRendererDisabled(C.TRACK_TYPE_VIDEO)
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
        Toast.makeText(this,R.string.failed_in_playing_video,Toast.LENGTH_SHORT).show()
        finish()
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
                handleHardSubtitle()
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

    override fun onItemClick(position: Int, item: String?) {
        selectedPosition = position
        if(position == -1) {
            disableSubtitle()
        } else {
            enableSubtitle()
            selectedSubtitle = item
            reloadSubtitle()
        }
        subtitleDialog?.takeIf { it.isShowing } ?.dismiss()
        subtitleDialog?.cancel()
    }

}