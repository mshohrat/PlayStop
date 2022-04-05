package com.ms.playstop.ui.movie

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.textview.MaterialTextView
import com.microsoft.appcenter.crashes.Crashes
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.*
import com.ms.playstop.model.Character
import com.ms.playstop.model.Episode
import com.ms.playstop.model.Movie
import com.ms.playstop.model.Profile
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.ui.character.CharacterFragment
import com.ms.playstop.ui.character.adapter.CharacterAdapter
import com.ms.playstop.ui.comments.CommentsFragment
import com.ms.playstop.ui.enrerPhoneNumber.EnterPhoneNumberFragment
import com.ms.playstop.ui.login.LoginFragment
import com.ms.playstop.ui.movie.adapter.CommentAdapter
import com.ms.playstop.ui.movie.adapter.EpisodeAdapter
import com.ms.playstop.ui.movie.adapter.LinkAdapter
import com.ms.playstop.ui.movie.adapter.SeasonAdapter
import com.ms.playstop.ui.movieLists.adapter.MovieAdapter
import com.ms.playstop.ui.payment.PaymentFragment
import com.ms.playstop.ui.playVideo.PlayVideoActivity
import com.ms.playstop.utils.*
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : BaseFragment(), EpisodeAdapter.OnItemClickListener,
    CharacterAdapter.OnItemClickListener {

    companion object {
        fun newInstance() = MovieFragment()
        const val MOVIE_ID_KEY = "MOVIE ID KEY"
        const val SHOW_CASE_SHARE_KEY = "Show Case Share Key"
        const val SHOW_CASE_LIKE_KEY = "Show Case Like Key"
    }

    private lateinit var viewModel: MovieViewModel
    private var loadingDialog: LoadingDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun tag(): String {
        return "Movie Fragment"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(isUserLoggedIn()) {
            movie_like_btn?.show()
        } else {
            movie_like_btn?.hide()
        }
        viewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
    }

    override fun onViewLoaded() {
        super.onViewLoaded()
        initViews()
        subscribeToViewModel()
        subscribeToViewEvents()
        val movieId = arguments?.takeIf { it.containsKey(MOVIE_ID_KEY) }?.getInt(MOVIE_ID_KEY) ?: -1
        viewModel.fetchMovie(movieId)
    }

    override fun onDayNightModeApplied(type: Int) {
        activity?.let { ctx ->
            with(ctx.getResourceFromThemeAttribute(R.attr.textAppearanceHeadline3,R.style.Headline3_FixSize)) {
                movie_collapsing_toolbar_layout?.setCollapsedTitleTextAppearance(this)
                movie_collapsing_toolbar_layout?.setExpandedTitleTextAppearance(this)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    movie_description_title_tv?.setTextAppearance(this)
                    movie_actors_title_tv?.setTextAppearance(this)
                    movie_director_title_tv?.setTextAppearance(this)
                    movie_writer_title_tv?.setTextAppearance(this)
                    movie_language_title_tv?.setTextAppearance(this)
                    movie_country_title_tv?.setTextAppearance(this)
                    movie_trailer_title_tv?.setTextAppearance(this)
                    movie_links_title_tv?.setTextAppearance(this)
                    movie_seasons_title_tv?.setTextAppearance(this)
                    movie_subtitle_title_tv?.setTextAppearance(this)
                    movie_similar_title_tv?.setTextAppearance(this)
                    movie_comments_title_tv?.setTextAppearance(this)
                } else {
                    movie_description_title_tv?.setTextAppearance(ctx,this)
                    movie_actors_title_tv?.setTextAppearance(ctx,this)
                    movie_director_title_tv?.setTextAppearance(ctx,this)
                    movie_writer_title_tv?.setTextAppearance(ctx,this)
                    movie_language_title_tv?.setTextAppearance(ctx,this)
                    movie_country_title_tv?.setTextAppearance(ctx,this)
                    movie_trailer_title_tv?.setTextAppearance(ctx,this)
                    movie_links_title_tv?.setTextAppearance(ctx,this)
                    movie_seasons_title_tv?.setTextAppearance(ctx,this)
                    movie_subtitle_title_tv?.setTextAppearance(ctx,this)
                    movie_similar_title_tv?.setTextAppearance(ctx,this)
                    movie_comments_title_tv?.setTextAppearance(ctx,this)
                }
            }
            with(ContextCompat.getColor(ctx,R.color.colorAccentDark)){
                movie_collapsing_toolbar_layout?.contentScrim = ColorDrawable(this)
                movie_collapsing_toolbar_root?.setBackgroundColor(this)
            }
            movie_shimmer_image?.background = MaterialShapeDrawable(
                ShapeAppearanceModel.builder()
                .setAllCornerSizes(ctx.resources.getDimensionPixelSize(R.dimen.shimmer_radius).toFloat())
                .build()
            ).apply {
                fillColor = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.white))
            }
            movie_score_tv?.background = MaterialShapeDrawable(
                ShapeAppearanceModel.builder()
                    .setAllCornerSizes(ctx.resources.getDimensionPixelSize(R.dimen.shimmer_radius).toFloat())
                    .build()
            ).apply {
                fillColor = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.yellow))
            }
            with(ContextCompat.getColor(ctx,R.color.colorAccent)){
                movie_description_divider?.setBackgroundColor(this)
                movie_actors_divider?.setBackgroundColor(this)
                movie_director_divider?.setBackgroundColor(this)
                movie_writer_divider?.setBackgroundColor(this)
                movie_language_divider?.setBackgroundColor(this)
                movie_country_divider?.setBackgroundColor(this)
                movie_trailer_divider?.setBackgroundColor(this)
                movie_links_divider?.setBackgroundColor(this)
                movie_seasons_divider?.setBackgroundColor(this)
                movie_subtitle_divider?.setBackgroundColor(this)
                movie_similar_movies_divider?.setBackgroundColor(this)
                movie_comments_divider?.setBackgroundColor(this)
                movie_login_and_watch_btn?.backgroundTintList = ColorStateList.valueOf(this)
                movie_verify_phone_and_watch_btn?.backgroundTintList = ColorStateList.valueOf(this)
                movie_purchase_subscription_and_watch_btn?.backgroundTintList = ColorStateList.valueOf(this)
                movie_subtitle_btn?.backgroundTintList = ColorStateList.valueOf(this)
                movie_submit_comment_btn?.backgroundTintList = ColorStateList.valueOf(this)
                movie_show_comments_tv?.backgroundTintList = ColorStateList.valueOf(this)
            }
            with(ContextCompat.getColor(ctx,R.color.colorPrimary)){
                movie_nested_scroll_view?.setBackgroundColor(this)
                movie_show_comments_tv?.setTextColor(this)
                movie_subtitle_btn?.setTextColor(this)
                movie_purchase_subscription_and_watch_btn?.setTextColor(this)
                movie_verify_phone_and_watch_btn?.setTextColor(this)
                movie_login_and_watch_btn?.setTextColor(this)
            }
            with(ContextCompat.getColor(ctx,R.color.white)){
                movie_description_tv?.setTextColor(this)
                movie_actors_tv?.setTextColor(this)
                movie_director_name_tv?.setTextColor(this)
                movie_director_tv?.setTextColor(this)
                movie_writer_name_tv?.setTextColor(this)
                movie_writer_tv?.setTextColor(this)
                movie_language_tv?.setTextColor(this)
                movie_country_tv?.setTextColor(this)
                movie_submit_comment_btn?.setTextColor(this)
                movie_submit_comment_et?.setTextColor(this)
            }
            with(ContextCompat.getColor(ctx,R.color.gray)){
                movie_submit_comment_et?.setHintTextColor(this)
                movie_no_comments_tv?.setTextColor(this)
                movie_no_similar_tv?.setTextColor(this)
                movie_no_seasons_tv?.setTextColor(this)
                movie_no_links_tv?.setTextColor(this)
            }
            movie_submit_comment_et?.background = MaterialShapeDrawable(ShapeAppearanceModel.builder()
                .setAllCornerSizes(ctx.resources.getDimensionPixelSize(R.dimen.shimmer_radius).toFloat())
                .build()
            ).apply {
                strokeColor = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.white))
                strokeWidth = convertDpToPixel(1f).toFloat()
            }
            movie_actors_recycler?.adapter?.takeIf { it is DayNightModeAwareAdapter }?.let {
                (it as DayNightModeAwareAdapter).onDayNightModeChanged(type)
            }
            movie_comments_recycler?.adapter?.takeIf { it is DayNightModeAwareAdapter }?.let {
                (it as DayNightModeAwareAdapter).onDayNightModeChanged(type)
            }
            movie_similar_recycler?.adapter?.takeIf { it is DayNightModeAwareAdapter }?.let {
                (it as DayNightModeAwareAdapter).onDayNightModeChanged(type)
            }
            movie_links_recycler?.adapter?.takeIf { it is DayNightModeAwareAdapter }?.let {
                (it as DayNightModeAwareAdapter).onDayNightModeChanged(type)
            }
            movie_seasons_recycler?.adapter?.takeIf { it is DayNightModeAwareAdapter }?.let {
                (it as DayNightModeAwareAdapter).onDayNightModeChanged(type)
            }
        }
    }

    private fun initViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val transitionName = arguments?.takeIf { it.containsKey(TRANSITION_NAME) }?.getString(TRANSITION_NAME)
            movie_image_iv?.transitionName = transitionName
        }
    }

    @SuppressLint("CheckResult")
    private fun subscribeToViewModel() {
        viewModel.movie.observe(viewLifecycleOwner, {
            handleShowGuideToUser()
            fillMovieData(it)
            handleFetchSimilarMovies()
            endLogAndIndexMovie(it?.name)
        })

        viewModel.movieError.observe(viewLifecycleOwner, {
            movie_shimmer_image?.hide()
            movie_shimmer_detail?.hide()
            movie_refresh_layout?.isRefreshing = false
            it.messageResId?.let {
                Toast.makeText(activity,it,Toast.LENGTH_SHORT).show()
            } ?: kotlin.run {
                Toast.makeText(activity,it.message ?: "",Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.sendComment.observe(viewLifecycleOwner, {
            showToast(it)
            movie_submit_comment_et?.text = null
            dismissLoadingDialog()
        })

        viewModel.sendCommentError.observe(viewLifecycleOwner, {
            showToast(it)
            dismissLoadingDialog()
        })

        viewModel.likeMovie.observe(viewLifecycleOwner, {
            showToast(it)
            handleMovieLikeAndDislike(viewModel.movie.value)
        })

        viewModel.likeMovieError.observe(viewLifecycleOwner, {
            showToast(it)
            handleMovieLikeAndDislike(viewModel.movie.value)
        })

        viewModel.similarMovies.observe(viewLifecycleOwner, {
            fillSimilarMovieData(it)
        })

        viewModel.similarMoviesError.observe(viewLifecycleOwner, {
            fillSimilarMovieData(null)
        })
    }

    private fun handleShowGuideToUser() {
        if(isUserLoggedIn()){
            if(isGuideShown(SHOW_CASE_SHARE_KEY)) {
                showGuide(
                    (R.string.like_movie to R.string.like_this_movie) to (SHOW_CASE_LIKE_KEY to movie_like_btn)
                )
            } else {
                showSequenceGuide(listOf(
                    (R.string.share_movie to R.string.share_this_movie_link) to (SHOW_CASE_SHARE_KEY to movie_share_btn)
                    ,(R.string.like_movie to R.string.like_this_movie) to (SHOW_CASE_LIKE_KEY to movie_like_btn)
                ))
            }
        } else {
            showGuide(
                (R.string.share_movie to R.string.share_this_movie_link) to (SHOW_CASE_SHARE_KEY to movie_share_btn)
            )
        }
    }

    @SuppressLint("CheckResult")
    private fun fillMovieData(movie: Movie?) {
        movie_shimmer_image?.hide()
        movie_shimmer_detail?.hide()
        movie_image_iv?.show()
        movie_detail_layout?.show()

        movie_refresh_layout?.isRefreshing = false

        movie?.let { movie ->
            handleMovieLikeAndDislike(movie)
            movie_name_tv?.text = movie.name
            movie_toolbar_name_tv?.text = movie.name
            movie.genres?.let { list ->
                list.takeIf { it.size >= 3 }?.take(3)?.let {
                    var genres = ""
                    for (genre in it) {
                        genres = "$genres${genre.name} - "
                    }
                    genres = genres.dropLast(3)
                    movie_genre_tv?.text = genres
                } ?: kotlin.run {
                    var genres = ""
                    for (genre in list) {
                        genres = "$genres${genre.name} - "
                    }
                    genres = genres.dropLast(3)
                    movie_genre_tv?.text = genres
                }
            }
            if (movie.isSeries) {
                movie_length_tv?.text = getString(R.string.unknown_time)
            } else {
                movie_length_tv?.text = separateLengthByTime(movie.length)
            }
            movie_score_tv?.show()
            movie.score?.toString()?.takeIf { it.isNotEmpty() && it != "0" }?.let {
                movie_score_tv?.text = String.format(getString(R.string.imdb_score_x), it)
            } ?: kotlin.run {
                //movie_score_tv?.text = getString(R.string.imdb_empty)
                movie_score_tv?.hide()
            }

            movie.scoreVotes?.let { votes ->
                votes.takeIf { it != 0 }?.let {
                    try {
                        movie_score_number_tv?.text =
                            String.format(getString(R.string.votes_x), String.format("%,d", it))
                    } catch (e: Exception) {
                        Crashes.trackError(e)
                    }
                } ?: kotlin.run {
                    movie_score_number_tv?.hide()
                }
            } ?: kotlin.run {
                movie_score_number_tv?.hide()
            }
            loadImage(
                movie_image_iv,
                movie.image,
                listOf(RequestOptions.bitmapTransform(RoundedCornersTransformation(16,0))),
                3
            )
            movie.header?.let { header ->
                movie_image_background_group?.show()
                loadImage(
                    movie_image_background_iv,
                    header,
                    listOf(RequestOptions.bitmapTransform(RoundedCornersTransformation(16,0))),
                    3
                )
            } ?: kotlin.run {
                movie_image_background_group?.hide()
            }

            movie_description_tv?.text = movie.description
            movie_production_year_tv?.text = movie.productionYear.toString()

            movie.realActors?.takeIf { it.isNotEmpty() }?.let { list ->
                val layoutManager = RtlGridLayoutManager(activity, 3, RecyclerView.VERTICAL, false)
                movie_actors_recycler?.layoutManager = layoutManager
                movie_actors_recycler?.itemDecorationCount?.takeIf { it == 0 }?.let {
                    val spacing = activity?.resources?.getDimensionPixelSize(R.dimen.margin_medium) ?: 0
                    movie_actors_recycler?.addItemDecoration(GridSpacingItemDecoration(3, spacing, true))
                }
                movie_actors_recycler?.adapter = CharacterAdapter(list,CharacterFragment.CHARACTER_TYPE_ACTOR,this)
            } ?: kotlin.run {
                movie_actors_recycler?.hide()
                movie_actors_tv?.show()
                movie_actors_tv?.text = movie.actors
            }

            val movieDirectorTitleParams = movie_director_title_tv?.layoutParams as? LinearLayout.LayoutParams
            movie.realActors?.takeIf { it.isNotEmpty() }?.let {
                movieDirectorTitleParams?.topMargin = activity?.resources?.getDimensionPixelSize(R.dimen.margin_medium) ?: 0
            } ?: kotlin.run {
                movieDirectorTitleParams?.topMargin = activity?.resources?.getDimensionPixelSize(R.dimen.margin_large) ?: 0
            }
            movie_director_title_tv?.layoutParams = movieDirectorTitleParams

            movie.realDirector?.takeIf { it.name.isValidCharacterName() }?.let {
                movie_director_avatar_iv?.let { iv ->
                    iv.context?.let { ctx ->
                        Glide.with(ctx).load(it.image)
                            .placeholder(AppCompatResources.getDrawable(ctx,R.drawable.ic_person))
                            .error(AppCompatResources.getDrawable(ctx,R.drawable.ic_person))
                            .circleCrop()
                            .into(iv)
                    }
                }
                movie_director_name_tv?.text = it.name

                val params = movie_director_layout?.layoutParams as? LinearLayout.LayoutParams
                val margins = (activity?.resources?.getDimensionPixelSize(R.dimen.margin_medium) ?: 0 ) * 4
                val outerMargins = (activity?.resources?.getDimensionPixelSize(R.dimen.padding_standard) ?: 0 ) * 2
                val width = (widthOfDevice() - outerMargins - margins) / 3
                params?.width = width
                movie_director_layout?.layoutParams = params
                movie_director_layout?.setOnClickListener { v ->
                    onItemClick(it,CharacterFragment.CHARACTER_TYPE_DIRECTOR,-1)
                }
            } ?: kotlin.run {
                movie_director_layout?.hide()
                movie_director_tv?.show()
                movie_director_tv?.text = movie.director
            }

            movie.realWriter?.takeIf { it.name.isValidCharacterName() }?.let {
                movie_writer_avatar_iv?.let { iv ->
                    iv.context?.let { ctx ->
                        Glide.with(ctx).load(it.image)
                            .placeholder(AppCompatResources.getDrawable(ctx,R.drawable.ic_person))
                            .error(AppCompatResources.getDrawable(ctx,R.drawable.ic_person))
                            .circleCrop()
                            .into(iv)
                    }
                }
                movie_writer_name_tv?.text = it.name

                val params = movie_writer_layout?.layoutParams as? LinearLayout.LayoutParams
                val margins = (activity?.resources?.getDimensionPixelSize(R.dimen.margin_medium) ?: 0 ) * 4
                val outerMargins = (activity?.resources?.getDimensionPixelSize(R.dimen.padding_standard) ?: 0 ) * 2
                val width = (widthOfDevice() - outerMargins - margins) / 3
                params?.width = width
                movie_writer_layout?.layoutParams = params
                movie_writer_layout?.setOnClickListener { v ->
                    onItemClick(it,CharacterFragment.CHARACTER_TYPE_WRITER,-1)
                }
            } ?: kotlin.run {
                movie_writer_layout?.hide()
                movie_writer_tv?.show()
                movie_writer_tv?.text = movie.writer
            }

            movie.languages?.let { list ->
                list.takeIf { it.isEmpty().not() }?.let {
                    var languages = ""
                    for (language in it) {
                        languages = "$languages${language.name} - "
                    }
                    languages = languages.dropLast(3)
                    movie_language_tv?.text = languages
                } ?: kotlin.run {
                    movie_language_tv?.setText(R.string.unknown)
                }
            }
            movie.countries?.let { list ->
                list.takeIf { it.isEmpty().not() }?.let {
                    var countries = ""
                    for (country in it) {
                        countries = "$countries${country.name} - "
                    }
                    countries = countries.dropLast(3)
                    movie_country_tv?.text = countries
                } ?: kotlin.run {
                    movie_country_tv?.setText(R.string.unknown)
                }
            }

            if (movie.trailer != null) {
                movie_trailer_title_tv?.show()
                movie_trailer_divider?.show()
                movie_trailer_frame?.show()
                val trailerImageParams = movie_trailer_iv?.layoutParams as? FrameLayout.LayoutParams
                val trailerImageWidth = widthOfDevice().minus(
                    context?.resources?.getDimensionPixelSize(R.dimen.padding_standard)?.times(2)
                        ?: 0
                )
                trailerImageParams?.height = trailerImageWidth.times(3).div(5)
                movie_trailer_iv?.layoutParams = trailerImageParams
                getBitmapFromVideo(movie.trailer)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ bitmap ->
                        movie_trailer_progress?.hide()
                        movie_trailer_play_iv?.show()
                        movie_trailer_iv?.let {
                            context?.let { ctx ->
                                Glide.with(ctx).load(bitmap).override(bitmap?.width?.div(3) ?: 400,bitmap?.height?.div(3) ?: 280).apply(
                                    RequestOptions.bitmapTransform(
                                        RoundedCornersTransformation(8, 0)
                                    )
                                ).into(it)
                            }
                        }
                    }, {})
                movie_trailer_frame?.setOnClickListener {
                    val intent = Intent(context, PlayVideoActivity::class.java)
                    intent.putExtra(PlayVideoActivity.PLAY_VIDEO_URL, movie.trailer)
                    intent.putExtra(PlayVideoActivity.PLAY_VIDEO_NAME, movie.name.plus(" ").plus(getString(R.string.trailer_phrase)))
                    activity?.startActivity(intent)
                }
            } else {
                movie_trailer_title_tv?.hide()
                movie_trailer_divider?.hide()
                movie_trailer_frame?.hide()
                movie_trailer_frame?.setOnClickListener(null)
            }
            when {
                isUserLoggedIn().not() -> {
                    hideAllLinks()
                    movie_verify_phone_and_watch_btn?.hide()
                    movie_purchase_subscription_and_watch_btn?.hide()
                    movie_login_and_watch_btn?.show()
                }
                isUserPhoneVerified().not() -> {
                    hideAllLinks()
                    movie_login_and_watch_btn?.hide()
                    movie_purchase_subscription_and_watch_btn?.hide()
                    movie_verify_phone_and_watch_btn?.show()
                }
                isSubscriptionEnabled() && movie.isUserSubscriptionExpired -> {
                    when {
                        movie.isSeries && movie.seasons?.isNotEmpty() == true -> handleMovieSeasonsAndEpisodes(movie)
                        movie.urls?.isNotEmpty() == true -> handleMovieUrls(movie)
                        else -> {
                            hideAllLinks()
                            movie_login_and_watch_btn?.hide()
                            movie_verify_phone_and_watch_btn?.hide()
                            movie_purchase_subscription_and_watch_btn?.show()
                        }
                    }
                }
                movie.isSeries -> {
                    handleMovieSeasonsAndEpisodes(movie)
                }
                else -> {
                    handleMovieUrls(movie)
                }
            }
            movie.comments?.takeIf { it.isNotEmpty() }?.let {
                movie_no_comments_tv?.hide()
                movie_comments_recycler?.show()
                val commentLayoutManager =
                    LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                val commentAdapter = CommentAdapter(it)
                movie_comments_recycler?.layoutManager = commentLayoutManager
                movie_comments_recycler?.adapter = commentAdapter
            } ?: kotlin.run {
                movie_no_comments_tv?.show()
            }
            movie.subtitle?.let { subtitle ->
                movie_subtitle_title_tv?.show()
                movie_subtitle_divider?.show()
                movie_subtitle_btn?.show()
                movie_subtitle_btn?.setOnClickListener {
                    tryToShowUrl(subtitle)
                }
            } ?: kotlin.run {
                movie_subtitle_title_tv?.hide()
                movie_subtitle_divider?.hide()
                movie_subtitle_btn?.hide()
            }
        }
    }

    private fun handleMovieUrls(movie: Movie) {
        movie_login_and_watch_btn?.hide()
        movie_verify_phone_and_watch_btn?.hide()
        movie_links_title_tv?.show()
        movie_links_divider?.show()
        movie.urls?.takeIf { it.isNotEmpty() }?.let {
            movie_no_links_tv?.hide()
            movie_links_recycler?.show()
            val linkLayoutManager =
                LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            val linkAdapter = LinkAdapter(it.toMutableList(), movie.subtitles,movie.name,movie.id,Movie.TYPE_MOVIE)
            movie_links_recycler?.layoutManager = linkLayoutManager
            movie_links_recycler?.adapter = linkAdapter
        } ?: kotlin.run {
            movie_no_links_tv?.show()
            (movie_links_recycler?.adapter as? LinkAdapter)?.clearAll()
            movie_links_recycler?.hide()
        }
    }

    private fun handleMovieSeasonsAndEpisodes(movie: Movie) {
        movie_login_and_watch_btn?.hide()
        movie_verify_phone_and_watch_btn?.hide()
        movie_seasons_title_tv?.show()
        movie_seasons_divider?.show()
        movie.seasons?.takeIf { it.isNotEmpty() }?.let {
            movie_no_seasons_tv?.hide()
            movie_seasons_recycler?.show()
            val seasonLayoutManager =
                LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            val seasonAdapter = SeasonAdapter(it, this)
            movie_seasons_recycler?.layoutManager = seasonLayoutManager
            movie_seasons_recycler?.adapter = seasonAdapter
        } ?: kotlin.run {
            movie_no_seasons_tv?.show()
            (movie_seasons_recycler?.adapter as? SeasonAdapter)?.clearAll()
            movie_seasons_recycler?.hide()
        }
    }

    private fun handleFetchSimilarMovies() {
        val adapter = MovieAdapter(emptyList(),null,6)
        val layoutManager = RtlLinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
        movie_similar_recycler?.layoutManager = layoutManager
        movie_similar_recycler?.adapter = adapter
        viewModel.fetchSimilarMovies()
    }

    private fun fillSimilarMovieData(movies: List<Movie>?) {
        movies?.takeIf { it.isNotEmpty() }?.let {
            movie_no_similar_tv?.hide()
            movie_similar_recycler?.show()
            val adapter = MovieAdapter(it,object : MovieAdapter.OnItemClickListener{
                override fun onItemClick(movie: Movie?) {
                    movie?.let {
                        val movieFragment = newInstance()
                        movieFragment.arguments = Bundle().apply {
                            this.putInt(MOVIE_ID_KEY,it.id)
                        }
                        addToParent(movieFragment)
                    }
                }
            })
            movie_similar_recycler?.adapter = adapter
        } ?: kotlin.run {
            movie_similar_recycler?.hide()
            movie_no_similar_tv?.show()
        }
    }

    private fun handleMovieLikeAndDislike(movie: Movie?) {
        if(isUserLoggedIn()) {
            movie?.let {
                if(it.isLiked) {
                    movie_like_btn?.setImageResource(R.drawable.ic_like)
                } else {
                    movie_like_btn?.setImageResource(R.drawable.ic_dislike)
                }
            }
            movie_like_btn?.show()
        } else {
            movie_like_btn?.hide()
        }
    }

    private fun hideAllLinks() {
        movie_seasons_title_tv?.hide()
        movie_seasons_divider?.hide()
        movie_no_seasons_tv?.hide()
        movie_seasons_recycler?.hide()
        movie_links_title_tv?.hide()
        movie_links_divider?.hide()
        movie_no_links_tv?.hide()
        movie_links_recycler?.hide()
        movie_no_links_tv?.hide()
    }

    private fun getBitmapFromVideo(path: String?) : Single<Bitmap?> {
        return Single.fromCallable {
            try {
                val mediaMetadataRetriever = MediaMetadataRetriever()
                val m = mediaMetadataRetriever.apply { setDataSource(path, hashMapOf<String,String>()) }.frameAtTime
                mediaMetadataRetriever.release()
                m
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun tryToShowUrl(urlString: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlString)
        val resolveInfo = activity?.packageManager?.queryIntentActivities(intent,0)
        resolveInfo?.takeIf { it.isNotEmpty() }?.let {
            activity?.startActivity(
                Intent.createChooser(intent,getString(R.string.show_by))
            )
        } ?: kotlin.run {
            intent.data = Uri.parse(urlString)
            activity?.startActivity(
                Intent.createChooser(intent,getString(R.string.show_by))
            )
        }
    }

    private fun subscribeToViewEvents() {
        movie_show_comments_tv?.setOnClickListener {
            val commentsFragment = CommentsFragment.newInstance()
            commentsFragment.arguments = Bundle().apply {
                this.putInt(CommentsFragment.COMMENTS_MOVIE_ID,viewModel.movie.value?.id ?: -1)
            }
            addToParent(commentsFragment)
        }

        movie_back_btn?.setOnClickListener {
            activity?.onBackPressed()
        }

        movie_appbar?.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var scrollRange = -1
            var isShow = true
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                appBarLayout?.let {
                    if (scrollRange == -1) {
                        scrollRange = it.totalScrollRange
                    }
                    if (scrollRange + verticalOffset == 0) {
                        movie_toolbar_name_tv?.show()
                        isShow = true
                    } else if(isShow) {
                        movie_toolbar_name_tv?.hide()
                        isShow = false
                    }
                }

            }

        })

        movie_submit_comment_btn?.setOnClickListener {
            if(isUserLoggedIn()) {
                showLoadingDialog()
                viewModel.sendComment(movie_submit_comment_et?.text?.toString())
            } else {
                val loginFragment = LoginFragment.newInstance()
                addToParent(loginFragment)
            }
        }

        movie_share_btn?.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT,"http://playstopapp.ir/open/movie".plus(viewModel.movie.value?.id))
            intent.type = "text/plain"
            activity?.startActivity(
                Intent.createChooser(intent,activity?.getString(R.string.send_by))
            )
        }

        movie_refresh_layout?.setOnRefreshListener {
            viewModel.refresh()
        }

        movie_login_and_watch_btn?.setOnClickListener {
            val enterPhoneNumberFragment = EnterPhoneNumberFragment.newInstance()
            val args = Bundle().apply {
                putInt(EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE, EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE_LOGIN)
            }
            enterPhoneNumberFragment.arguments = args
            addToParent(enterPhoneNumberFragment)
        }

        movie_verify_phone_and_watch_btn?.setOnClickListener {
            val enterPhoneNumberFragment = EnterPhoneNumberFragment.newInstance()
            val args = Bundle().apply {
                putInt(EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE, EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE_ADD)
            }
            enterPhoneNumberFragment.arguments = args
            addToParent(enterPhoneNumberFragment)
        }

        movie_purchase_subscription_and_watch_btn?.setOnClickListener {
            val paymentFragment = PaymentFragment.newInstance()
            addToParent(paymentFragment)
        }

        movie_like_btn?.setOnClickListener {
            handleMovieLikeAndDislike(viewModel.movie.value)
            viewModel.likeOrDislikeMovie()
        }
    }

    override fun onItemClick(episode: Episode,seasonName: String?) {
        showBottomSheetDialogForUrls(episode,seasonName)
    }

    private fun showBottomSheetDialogForUrls(episode: Episode, seasonName: String?) {
        activity?.let { ctx ->
            episode.urls?.takeIf { it.isNotEmpty() }?.let { urls ->
                val dialog = BottomSheetDialog(ctx)
                dialog.setContentView(R.layout.layout_bottom_sheet_recycler)
                val titleTv = dialog.findViewById<MaterialTextView>(R.id.bottom_sheet_recycler_title_tv)
                val closeIb = dialog.findViewById<AppCompatImageButton>(R.id.bottom_sheet_recycler_close_ib)
                val recycler = dialog.findViewById<RecyclerView>(R.id.bottom_sheet_recycler_recycler)
                titleTv?.text = episode.name
                closeIb?.setOnClickListener {
                    dialog.takeIf { it.isShowing }?.dismiss()
                    dialog.cancel()
                }
                val lm = LinearLayoutManager(ctx,RecyclerView.VERTICAL,false)
                recycler?.layoutManager = lm
                val adapter = LinkAdapter(urls.toMutableList(),
                    episode.subtitles,
                    viewModel.movie.value?.name?.plus(" - ")?.plus(seasonName)?.plus(" - ")?.plus(episode.name),
                    episode.id,
                    Movie.TYPE_SERIES
                )
                recycler?.adapter = adapter
                dialog.show()
            }

        }

    }

    private fun showToast(response: GeneralResponse) {
        response.message?.takeIf { it.isNotEmpty() }?.let {
            Toast.makeText(activity,it,Toast.LENGTH_SHORT).show()
        } ?: kotlin.run {
            response.messageResId?.let {
                Toast.makeText(activity,it,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoadingDialog() {
        activity?.let { ctx ->
            loadingDialog = LoadingDialog(ctx)
            loadingDialog?.show()
        }
    }

    private fun dismissLoadingDialog() {
        loadingDialog?.takeIf { it.isShowing }?.dismiss()
        loadingDialog?.cancel()
    }

    override fun onSharedPreferencesChanged(key: String) {
        if(key == Profile.SAVE_KEY) {
            viewModel.refresh()
        }
    }

    override fun onItemClick(character: Character,type: Int, position: Int) {
        val characterFragment = CharacterFragment.newInstance()
        characterFragment.arguments = Bundle().apply {
            putInt(CharacterFragment.CHARACTER_TYPE_KEY,type)
            putInt(CharacterFragment.CHARACTER_ID_KEY,character.id)
        }
        addToParent(characterFragment)
    }

}
