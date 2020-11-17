package com.ms.playstop.ui.movie

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textview.MaterialTextView
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.*
import com.ms.playstop.model.Episode
import com.ms.playstop.model.Movie
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.ui.comments.CommentsFragment
import com.ms.playstop.ui.login.LoginFragment
import com.ms.playstop.ui.movie.adapter.CommentAdapter
import com.ms.playstop.ui.movie.adapter.EpisodeAdapter
import com.ms.playstop.ui.movie.adapter.LinkAdapter
import com.ms.playstop.ui.movie.adapter.SeasonAdapter
import com.ms.playstop.utils.LoadingDialog
import com.ms.playstop.utils.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_comments.*
import kotlinx.android.synthetic.main.fragment_movie.*
import java.text.NumberFormat
import java.util.*

class MovieFragment : BaseFragment(), EpisodeAdapter.OnItemClickListener {

    companion object {
        fun newInstance() = MovieFragment()
        const val MOVIE_ID_KEY = "MOVIE ID KEY"
    }

    private lateinit var viewModel: MovieViewModel
    private var movie: Movie? = null
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
        viewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        initViews()
        subscribeToViewModel()
        subscribeToViewEvents()
        val movieId = arguments?.takeIf { it.containsKey(MOVIE_ID_KEY) }?.getInt(MOVIE_ID_KEY) ?: -1
        viewModel.fetchMovie(movieId)
    }

    private fun initViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val transitionName = arguments?.takeIf { it.containsKey(TRANSITION_NAME) }?.getString(TRANSITION_NAME)
            movie_image_iv?.transitionName = transitionName
        }
    }

    private fun subscribeToViewModel() {
        viewModel.movie.observe(viewLifecycleOwner, Observer {
            movie_shimmer_image?.hide()
            movie_shimmer_detail?.hide()
            movie_image_iv?.show()
            movie_detail_layout?.show()

            movie_refresh_layout?.isRefreshing = false

            this.movie = it
            it?.let { movie ->
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
                if(movie.isSeries) {
                    movie_length_tv?.text = getString(R.string.unknown_time)
                } else {
                    movie_length_tv?.text = separateLengthByTime(movie.length)
                }
                movie_score_tv?.show()
                movie.score?.toString()?.takeIf { it.isNotEmpty() && it != "0" }?.let {
                    movie_score_tv?.text = String.format(getString(R.string.imdb_score_x),it)
                } ?: kotlin.run {
                    //movie_score_tv?.text = getString(R.string.imdb_empty)
                    movie_score_tv?.hide()
                }

                movie.scoreVotes?.let { votes ->
                    val votesString = NumberFormat.getNumberInstance(Locale("fa-IR")).format(votes)
                    votesString?.takeIf { it.isNotEmpty() && it != "0" }?.let {
                        movie_score_number_tv?.text = String.format(getString(R.string.votes_x),it)
                    } ?: kotlin.run {
                        movie_score_number_tv?.hide()
                    }
                } ?: kotlin.run {
                    movie_score_number_tv?.hide()
                }
//                movie.scoreVotes?.toString()?.takeIf { it.isNotEmpty() && it != "0" }?.let {
//                    movie_score_number_tv?.text = String.format(getString(R.string.votes_x),it)
//                } ?: kotlin.run {
//                    movie_score_number_tv?.hide()
//                }

                movie_image_iv?.let {
                    Glide.with(it).load(movie.image).apply(
                        RequestOptions.bitmapTransform(
                            RoundedCornersTransformation(16,0)
                        )).into(it)
                }
                movie_description_tv?.text = movie.description
                movie_director_tv?.text = movie.director
                movie_writer_tv?.text = movie.writer
                movie_production_year_tv?.text = movie.productionYear.toString()
                movie_actors_tv?.text = movie.actors
                if(movie.isSeries) {
                    movie_seasons_title_tv?.show()
                    movie_seasons_divider?.show()
                    movie.seasons?.takeIf { it.isNotEmpty() }?.let {
                        movie_no_seasons_tv?.hide()
                        movie_seasons_recycler?.show()
                        val seasonLayoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
                        val seasonAdapter = SeasonAdapter(it,this)
                        movie_seasons_recycler?.layoutManager = seasonLayoutManager
                        movie_seasons_recycler?.adapter = seasonAdapter
                    } ?: kotlin.run {
                        movie_no_seasons_tv?.show()
                    }
                }
                else
                {
                    movie_links_title_tv?.show()
                    movie_links_divider?.show()
                    movie.urls?.takeIf { it.isNotEmpty() }?.let {
                        movie_no_links_tv?.hide()
                        movie_links_recycler?.show()
                        val linkLayoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
                        val linkAdapter = LinkAdapter(it)
                        movie_links_recycler?.layoutManager = linkLayoutManager
                        movie_links_recycler?.adapter = linkAdapter
                    } ?: kotlin.run {
                        movie_no_links_tv?.show()
                    }
                }
                movie.comments?.takeIf { it.isNotEmpty() }?.let {
                    movie_no_comments_tv?.hide()
                    movie_comments_recycler?.show()
                    val commentLayoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
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
        })

        viewModel.movieError.observe(viewLifecycleOwner, Observer {
            movie_shimmer_image?.hide()
            movie_shimmer_detail?.hide()
            movie_refresh_layout?.isRefreshing = false
            it.messageResId?.let {
                Toast.makeText(activity,it,Toast.LENGTH_SHORT).show()
            } ?: kotlin.run {
                Toast.makeText(activity,it.message ?: "",Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.sendComment.observe(viewLifecycleOwner, Observer {
            showToast(it)
            comments_submit_comment_et?.text = null
            dismissLoadingDialog()
        })

        viewModel.sendCommentError.observe(viewLifecycleOwner, Observer {
            showToast(it)
            dismissLoadingDialog()
        })
    }

    private fun tryToShowUrl(urlString: String) {
        val intent = Intent(Intent.ACTION_VIEW)
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
                this.putInt(CommentsFragment.COMMENTS_MOVIE_ID,movie?.id ?: -1)
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
            intent.putExtra(Intent.EXTRA_TEXT,"playstopapp.ir/open/movie".plus(movie?.id))
            intent.type = "text/plain"
            activity?.startActivity(
                Intent.createChooser(intent,activity?.getString(R.string.send_by))
            )
        }

        movie_refresh_layout?.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    override fun onItemClick(episode: Episode) {
        showBottomSheetDialogForUrls(episode)
    }

    private fun showBottomSheetDialogForUrls(episode: Episode) {
        context?.let { ctx ->
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
                val adapter = LinkAdapter(urls)
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

}
