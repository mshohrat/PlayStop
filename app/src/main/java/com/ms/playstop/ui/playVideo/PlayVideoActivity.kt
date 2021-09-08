package com.ms.playstop.ui.playVideo

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.RippleDrawable
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.ViewTreeObserver.*
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.*
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.*
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textview.MaterialTextView
import com.microsoft.appcenter.analytics.Analytics
import com.ms.playstop.R
import com.ms.playstop.extension.*
import com.ms.playstop.model.Movie
import com.ms.playstop.ui.playVideo.adapter.RadioLinkAdapter
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_play_video.*
import kotlinx.android.synthetic.main.exo_playback_control_view.*
import kotlin.math.max
import kotlin.math.min


class PlayVideoActivity : AppCompatActivity(), Player.EventListener,
    RadioLinkAdapter.OnItemClickListener {

    companion object {
        const val PLAY_VIDEO_ID = "PLAY VIDEO ID"
        const val PLAY_VIDEO_URL = "PLAY VIDEO URL"
        const val PLAY_VIDEO_NAME = "PLAY VIDEO NAME"
        const val PLAY_VIDEO_SUBTITLES = "PLAY VIDEO SUBTITLES"
        const val PLAY_VIDEO_BRIGHTNESS_KEY = "Play Video Brightness Key"
        const val SHOW_CASE_FAST_FORWARD_KEY = "Show Case Fast Forward Key"
        const val SHOW_CASE_REWIND_KEY = "Show Case Rewind Key"
        const val TAG = "Play Video Activity"
    }

    private var subtitleDialog: BottomSheetDialog? = null
    private lateinit var viewModel: PlayVideoViewModel
    private var exoPlayer: ExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L
    private lateinit var videoUrl : String
    private var videoName : String? = null
    private var subtitles : ArrayList<String>? = null
    private var selectedSubtitle : String? = null
    private var showSubtitle = true
    private var status: String? = null
    lateinit var trackSelector: DefaultTrackSelector
    private var hardSubHandled = false
    private var selectedPosition = 0
    lateinit var gestureDetector : GestureDetector
    private var playControllerVisibility = View.VISIBLE
    private val isPlayControllerVisible = playControllerVisibility == View.VISIBLE
    private var isLoading = true
    lateinit var audioManager: AudioManager
    private val WATCHED_TIMES_DELAY = 15000L
    private val watchedTimesHandler = Handler()
    private val watchedTimesRunnable = Runnable {
        if(Hawk.contains(Movie.WATCHED_ONLINE_TIMES)) {
            val times = Hawk.get<Int>(Movie.WATCHED_ONLINE_TIMES)
            when(times) {
                1 -> Hawk.put(Movie.WATCHED_ONLINE_TIMES,2)
                else -> {}
            }
        } else {
            Hawk.put(Movie.WATCHED_ONLINE_TIMES,1)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            //w.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
            w.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)
        videoUrl = intent?.takeIf { it.hasExtra(PLAY_VIDEO_URL) }?.getStringExtra(PLAY_VIDEO_URL) ?: ""
        videoName = intent?.takeIf { it.hasExtra(PLAY_VIDEO_NAME) }?.getStringExtra(PLAY_VIDEO_NAME) ?: ""
        subtitles = intent?.takeIf { it.hasExtra(PLAY_VIDEO_SUBTITLES) }?.getStringArrayListExtra(
            PLAY_VIDEO_SUBTITLES
        )
        if(subtitles.isNullOrEmpty().not()) {
            selectedSubtitle = subtitles?.firstOrNull()
        }
        viewModel = ViewModelProviders.of(this).get(PlayVideoViewModel::class.java)
        subscribeToViewModel()
        val videoId = intent?.takeIf { it.hasExtra(PLAY_VIDEO_ID) }?.getIntExtra(PLAY_VIDEO_ID,-1)
        viewModel.setMovieId(videoId)
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        initViews()
        handleConfigurationChange()
        handleShowGuideToUser()
        handleWatchedTimesStore()
        Analytics.trackEvent(TAG)
    }

    private fun subscribeToViewModel() {
        viewModel.savedMovieSeenPosition.observe(this,{
            playbackPosition = it
            exoPlayer?.seekTo(currentWindow, playbackPosition)
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViews() {
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent?): Boolean {
                if (isTouchEventAtLeftOfScreen(e)) {
                    play_rew_double_tap?.animation?.cancel()
                    play_rew_double_tap?.animate()
                        ?.alpha(1f)
                        ?.setDuration(200)
                        ?.withStartAction {
                            play_rew_double_tap?.show()
                        }
                        ?.withEndAction {
                            play_rew_double_tap?.forceRipple(e?.x ?: 0f, e?.y ?: 0f)
                            play_rew_double_tap?.performClick()
                        }
                } else {
                    play_ffwd_double_tap?.animation?.cancel()
                    play_ffwd_double_tap?.animate()
                        ?.alpha(1f)
                        ?.setDuration(200)
                        ?.withStartAction {
                            play_ffwd_double_tap?.show()
                        }
                        ?.withEndAction {
                            play_ffwd_double_tap?.forceRipple(e?.x ?: 0f, e?.y ?: 0f)
                            play_ffwd_double_tap?.performClick()
                        }
                }
                return true
            }

            override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
                return true
            }

            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent?,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                return e2?.let { play_video_player?.onTouchEvent(it) } == true
            }

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                return e2?.let { play_video_player?.onTouchEvent(it) } == true
            }

            override fun onContextClick(e: MotionEvent?): Boolean {
                return e?.let { play_video_player?.onTouchEvent(it) } == true
            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                return true
            }

            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                play_video_player?.performClick()
                return true
            }

        })

        play_video_player?.setOnTouchListener { v, event ->
            if(gestureDetector.onTouchEvent(event)) {
                return@setOnTouchListener true
            } else {
                return@setOnTouchListener play_video_player?.onTouchEvent(event) == true
            }
        }

        play_subtitle?.isEnabled = subtitles.isNullOrEmpty().not()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            play_subtitle?.imageTintList = if(subtitles.isNullOrEmpty().not()) ColorStateList.valueOf(
                Color.WHITE
            ) else  ColorStateList.valueOf(ColorUtils.setAlphaComponent(Color.GRAY,200))
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
        play_back?.text = videoName

        play_video_player?.setControllerVisibilityListener(object :
            PlayerControlView.VisibilityListener {
            override fun onVisibilityChange(visibility: Int) {
                if (visibility != playControllerVisibility) {
                    playControllerVisibility = visibility
                    handleProgressOnPlayControllerVisibilityChange()
                }
                if (visibility != View.VISIBLE) {
                    play_audio_seekbar?.hide()
                    play_brightness_seekbar?.hide()
                }
            }

        })

        play_ffwd_double_tap?.setOnClickListener {
            forwardVideo(10000)
            play_ffwd_double_tap?.animation?.cancel()
            play_ffwd_double_tap?.animate()
                ?.alpha(0f)
                ?.setDuration(200)
                ?.setStartDelay(400)
                ?.withEndAction {
                    play_ffwd_double_tap?.invisible()
                }
        }

        play_rew_double_tap?.setOnClickListener {
            rewindVideo(10000)
            play_rew_double_tap?.animation?.cancel()
            play_rew_double_tap?.animate()
                ?.alpha(0f)
                ?.setDuration(200)
                ?.setStartDelay(400)
                ?.withEndAction {
                    play_rew_double_tap?.invisible()
                }
        }

        play_audio?.setOnClickListener {
            if(play_audio_seekbar?.isVisible == true) {
                play_audio_seekbar?.hide()
            } else {
                play_audio_seekbar?.show()
            }
        }

        play_audio_seekbar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            private val animator = ValueAnimator.ofFloat(1f, 0f).apply {
                duration = 400
                startDelay = 2000
                doOnEnd {
                    play_audio_seekbar?.hide()
                    play_audio_seekbar?.alpha = 1f
                    play_video_player?.controllerShowTimeoutMs =
                        PlayerControlView.DEFAULT_SHOW_TIMEOUT_MS
                }
                addUpdateListener {
                    val value = it.animatedValue as Float
                    play_audio_seekbar?.alpha = value
                }
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC, castSeekbarValueToAudioVolume(
                        progress
                    ), 0
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                animator.cancel()
                play_audio_seekbar?.alpha = 1f
                play_audio_seekbar?.show()
                play_video_player?.controllerShowTimeoutMs = 0
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                play_audio_seekbar?.alpha = 1f
                animator.start()
            }

        })

        play_audio_seekbar?.progress = castAudioVolumeToSeekbarValue(
            audioManager.getStreamVolume(
                AudioManager.STREAM_MUSIC
            )
        )

        play_brightness?.setOnClickListener {
            if(play_brightness_seekbar?.isVisible == true) {
                play_brightness_seekbar?.hide()
            } else {
                play_brightness_seekbar?.show()
            }
        }

        play_brightness_seekbar?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {

            private val animator = ValueAnimator.ofFloat(1f, 0f).apply {
                duration = 400
                startDelay = 2000
                doOnEnd {
                    play_brightness_seekbar?.hide()
                    play_brightness_seekbar?.alpha = 1f
                    play_video_player?.controllerShowTimeoutMs =
                        PlayerControlView.DEFAULT_SHOW_TIMEOUT_MS
                }
                addUpdateListener {
                    val value = it.animatedValue as Float
                    play_brightness_seekbar?.alpha = value
                }
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateBrightness(castSeekbarValueToBrightness(progress))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                animator.cancel()
                play_brightness_seekbar?.alpha = 1f
                play_brightness_seekbar?.show()
                play_video_player?.controllerShowTimeoutMs = 0
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                play_brightness_seekbar?.alpha = 1f
                animator.start()
            }

        })

        if(Hawk.contains(PLAY_VIDEO_BRIGHTNESS_KEY)) {
            val brightness = Hawk.get<Float>(PLAY_VIDEO_BRIGHTNESS_KEY)
            play_brightness_seekbar?.progress = castBrightnessToSeekbarValue(brightness)
            play_video_player?.overlayFrameLayout?.alpha = 1 - brightness
        } else {
            play_brightness_seekbar?.progress = castBrightnessToSeekbarValue(1f)
        }

        val exoPositionParams = exo_position?.layoutParams as? ConstraintLayout.LayoutParams
        exoPositionParams?.let {
            it.width = exo_duration?.measuredWidth ?: ConstraintLayout.LayoutParams.WRAP_CONTENT
            exo_position?.layoutParams = it
        }

        play_video_player?.overlayFrameLayout?.setBackgroundColor(ContextCompat.getColor(this,R.color.pure_black_opacity_40))
        play_video_player?.overlayFrameLayout?.alpha = 0f
    }

    private fun updateBrightness(brightness: Float) {
        play_video_player?.overlayFrameLayout?.alpha = 1 - brightness
        Hawk.put(PLAY_VIDEO_BRIGHTNESS_KEY,brightness)
    }

    private fun rewindVideo(duration: Int) {
        exoPlayer?.let { player ->
            player.seekTo(max(player.currentPosition.minus(duration), 0))
        }
    }

    private fun forwardVideo(duration: Int) {
        exoPlayer?.let { player ->
            player.seekTo(min(player.currentPosition.plus(duration), player.duration))
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
            val lm = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            recycler?.layoutManager = lm
            val adapter = RadioLinkAdapter(it, selectedPosition, this)
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

    private fun isOrientationLandscape() : Boolean {
        return resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    override fun onResume() {
        super.onResume()
        initializePlayer(videoUrl)
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
        viewModel.setSeenMoviePosition(playbackPosition)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        handleConfigurationChange()
    }

    private fun handleConfigurationChange() {
        val currentOrientation = resources.configuration.orientation
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemUiFullScreen()
            play_fullscreen?.setImageDrawable(
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.ic_exit_fullscreen
                )
            )
//            val params = play_video_player?.subtitleView?.layoutParams as? ViewGroup.MarginLayoutParams
//            params?.bottomMargin = resources?.getDimensionPixelSize(R.dimen.margin_large)
//            play_video_player?.subtitleView?.layoutParams = params
        } else {
            hideSystemUi()
            play_fullscreen?.setImageDrawable(
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.ic_fullscreen
                )
            )
//            val params = play_video_player?.subtitleView?.layoutParams as? ViewGroup.MarginLayoutParams
//            params?.bottomMargin = resources?.getDimensionPixelSize(R.dimen.margin_large)
//            play_video_player?.subtitleView?.layoutParams = params
        }
    }

    private fun handleShowGuideToUser() {
        showSequenceGuide(listOf(
            (R.string.fast_forward_video to R.string.fast_forward_video_by_double_tap) to (SHOW_CASE_FAST_FORWARD_KEY to play_ffwd_double_tap)
            ,(R.string.rewind_video to R.string.rewind_video_by_double_tap) to (SHOW_CASE_REWIND_KEY to play_rew_double_tap)
        ))
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
            trackSelector = DefaultTrackSelector(this)
            val loadControl = DefaultLoadControl()
            val renderersFactory = DefaultRenderersFactory(this)
            exoPlayer = SimpleExoPlayer.Builder(this,renderersFactory)
                .setLoadControl(loadControl)
                .setTrackSelector(trackSelector)
                .build()
            play_video_player?.player = exoPlayer
        }
        val mediaSource = buildMediaSource(Uri.parse(url))
        subtitles?.takeIf { it.isNotEmpty() }?.let { subtitles ->
            val subtitleSources = mutableListOf<MediaSource>()
            for (subtitle in subtitles) {
                subtitleSources.add(buildSubtitleMediaSource(Uri.parse(subtitle)))
            }
            val mergedDataSource = MergingMediaSource(mediaSource, *subtitleSources.map { it }.toTypedArray())
            exoPlayer?.setMediaSource(mergedDataSource)
            exoPlayer?.prepare()
        } ?: kotlin.run {
            exoPlayer?.setMediaSource(mediaSource)
            exoPlayer?.prepare()
        }
        play_video_player?.hideController()
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

        val dataSourceFactory = DefaultHttpDataSource.Factory().setUserAgent(userAgent)
        return if (uri.lastPathSegment?.contains("mkv") == true || uri.lastPathSegment?.contains("mp4") == true) {
            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri))
        } else if (uri.lastPathSegment?.contains("m3u8") == true) {
            HlsMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri))
        } else {
            val dashChunkSourceFactory = DefaultDashChunkSource.Factory(
                dataSourceFactory
            )
            DashMediaSource.Factory(dashChunkSourceFactory, dataSourceFactory).createMediaSource(
                MediaItem.fromUri(uri)
            )
        }
    }

    private fun buildSubtitleMediaSource(uri: Uri): MediaSource {

        val userAgent = "exoplayer-playstop"

//        val subtitleFormat = Format.Builder()
//            .setId(null)
//            .setSampleMimeType(MimeTypes.APPLICATION_SUBRIP)
//            .setSelectionFlags(Format.NO_VALUE)
//            .setLanguage("en")
//            .build()

        val dataSourceFactory = DefaultDataSourceFactory(this, userAgent)

        return SingleSampleMediaSource.Factory(dataSourceFactory).createMediaSource(
            MediaItem.Subtitle(uri,MimeTypes.APPLICATION_SUBRIP,"en",Format.NO_VALUE),
            C.TIME_UNSET
        )
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

    private fun getHardSubCount(): Int {
        var hardSubCount = 0
        trackSelector.currentMappedTrackInfo?.getTrackGroups(C.TRACK_TYPE_VIDEO)?.takeIf { it.isEmpty.not() }
            ?.takeIf { it.length > 0 }
            ?.let { groupArray ->
            for (i in 0 until groupArray.length){
                val group = groupArray.get(i)
                for (j in 0 until group.length) {
                    if (group.getFormat(j).id != null) {
                        hardSubCount++
                    }
                }
            }
        }
        return hardSubCount
    }

    private fun handleHardSubtitle() {
        if(hardSubHandled.not() && getHardSubCount() != 0) {
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
            val newIndex = index + getHardSubCount()
            val override = DefaultTrackSelector.SelectionOverride(
                newIndex,
                0
            )
            textGroups?.let {
                trackSelector.parameters = trackSelector.buildUponParameters().setSelectionOverride(C.TRACK_TYPE_VIDEO,
                    it, override).build()
            }
        }
    }

    private fun disableSubtitle() {
        trackSelector.parameters = trackSelector.buildUponParameters().setRendererDisabled(C.TRACK_TYPE_VIDEO, true).build()
        trackSelector.parameters = trackSelector.buildUponParameters().clearSelectionOverrides().build()
        hardSubHandled = false
    }

    private fun enableSubtitle() {
        trackSelector.parameters = trackSelector.buildUponParameters().setRendererDisabled(C.TRACK_TYPE_VIDEO, false).build()
        handleHardSubtitle()
    }

    private fun isSubtitleDisabled(): Boolean {
        return trackSelector.parameters.getRendererDisabled(C.TRACK_TYPE_VIDEO)
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {

    }

    override fun onSeekProcessed() {

    }

    override fun onTracksChanged(
        trackGroups: TrackGroupArray,
        trackSelections: TrackSelectionArray
    ) {

    }

    override fun onPlayerError(error: ExoPlaybackException) {
        Toast.makeText(this, R.string.failed_in_playing_video, Toast.LENGTH_SHORT).show()
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

    override fun onTimelineChanged(timeline: Timeline, manifest: Any?, reason: Int) {
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        when (playbackState) {
            Player.STATE_BUFFERING -> {
                status = PlaybackStatus.LOADING
                showProgress()
            }
            Player.STATE_ENDED -> {
                status = PlaybackStatus.STOPPED
                hideProgress()
            }
            Player.STATE_IDLE -> {
                status = PlaybackStatus.IDLE
                hideProgress()
            }
            Player.STATE_READY -> {
                handleHardSubtitle()
                if (playWhenReady) {
                    status = PlaybackStatus.PLAYING
                    hideProgress()
                } else {
                    status = PlaybackStatus.PAUSED
                    hideProgress()
                }
            }
            else -> {
                status = PlaybackStatus.IDLE
                hideProgress()
            }
        }
    }

    private fun showProgress() {
        isLoading = true
//        if(isPlayControllerVisible) {
            exo_play?.alpha = 0f
            exo_pause?.alpha = 0f
//        }
        play_video_progress?.show()
    }

    private fun hideProgress() {
        isLoading = false
        play_video_progress?.hide()
//        if(isPlayControllerVisible) {
            if(exoPlayer?.playWhenReady == true) {
                exo_play?.alpha = 0f
                exo_pause?.alpha = 1f
            } else {
                exo_pause?.alpha = 0f
                exo_play?.alpha = 1f
            }
//        }
    }

    private fun handleProgressOnPlayControllerVisibilityChange() {
        if(isLoading) {
            showProgress()
        } else {
            hideProgress()
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

    fun isPlaying() : Boolean {
        return exoPlayer?.playWhenReady == true
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

    fun isTouchEventAtLeftOfScreen(e: MotionEvent?): Boolean {
        e?.let {
            val halfScreenX = if (isOrientationLandscape()) {
                heightOfDevice().div(2)
            } else {
                widthOfDevice().div(2)
            }
            return it.x < halfScreenX
        } ?: return false
    }

    private fun getRippleBackgroundForClick(): Int {
        val attrs = intArrayOf(R.attr.selectableItemBackground)
        val typedArray = obtainStyledAttributes(attrs)
        val resourceBackgroundId = typedArray.getResourceId(0, 0)
        typedArray.recycle()
        return resourceBackgroundId
    }

    fun View.forceRipple(x: Float, y: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && background is RippleDrawable) {
            background.setHotspot(x, y)
        }
        isPressed = true
        // For a quick ripple, you can immediately set false.
        postDelayed({
            isPressed = false
        }, 50)
    }

    private fun castAudioVolumeToSeekbarValue(audioVolume: Int) : Int {
        val maxValue = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        return audioVolume.times(100).div(maxValue)
    }

    private fun castSeekbarValueToAudioVolume(progress: Int) : Int {
        val maxValue = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        return progress.times(maxValue).div(100)
    }

    private fun castBrightnessToSeekbarValue(brightness: Float) : Int {
        return brightness.times(100).toInt()
    }

    private fun castSeekbarValueToBrightness(progress: Int) : Float {
        return progress.toFloat().div(100)
    }

    override fun onBackPressed() {
        if(isOrientationLandscape()) {
            changeOrientation()
        } else {
            super.onBackPressed()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            play_audio_seekbar?.progress = castAudioVolumeToSeekbarValue(
                audioManager.getStreamVolume(
                    AudioManager.STREAM_MUSIC
                )
            )
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            play_audio_seekbar?.progress = castAudioVolumeToSeekbarValue(
                audioManager.getStreamVolume(
                    AudioManager.STREAM_MUSIC
                )
            )
        }
        return super.onKeyUp(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        watchedTimesHandler.removeCallbacks(watchedTimesRunnable)
        play_video_player?.setControllerVisibilityListener(null)
        viewModel.updateSeenMovie()
    }

    private fun handleWatchedTimesStore() {
        watchedTimesHandler.postDelayed(watchedTimesRunnable, WATCHED_TIMES_DELAY)
    }

}