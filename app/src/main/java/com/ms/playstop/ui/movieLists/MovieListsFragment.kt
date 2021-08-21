package com.ms.playstop.ui.movieLists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.ms.playstop.MainActivity
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.*
import com.ms.playstop.model.*
import com.ms.playstop.ui.account.AccountFragment
import com.ms.playstop.ui.enrerPhoneNumber.EnterPhoneNumberFragment
import com.ms.playstop.ui.home.HomeFragment
import com.ms.playstop.ui.movie.MovieFragment
import com.ms.playstop.ui.movieLists.adapter.MovieHeaderAdapter
import com.ms.playstop.ui.movieLists.adapter.MovieListAdapter
import com.ms.playstop.ui.movies.MoviesFragment
import com.ms.playstop.ui.movies.adapter.RequestType
import com.ms.playstop.ui.search.SearchFragment
import com.ms.playstop.utils.DayNightModeAwareAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_movie_lists.*
import java.util.concurrent.TimeUnit

class MovieListsFragment : BaseFragment(), MovieListAdapter.OnItemClickListener,
    MovieHeaderAdapter.OnItemClickListener {

    companion object {
        fun newInstance() = MovieListsFragment()
        const val TAG = "Movie Lists Fragment"
        const val SHOW_CASE_ACCOUNT_KEY = "Show Case Account Key"
        const val SHOW_CASE_SEARCH_KEY = "Show Case Search Key"
        const val SHOW_CASE_CATEGORIES_KEY = "Show Case Categories Key"
    }

    private lateinit var viewModel: MovieListsViewModel
    private var appbarHeight = 0
    private var loadingExtraMovies = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_lists, container, false)
    }

    override fun getEnterAnimation(): Animation? {
        return activity?.let { AnimationUtils.loadAnimation(it,R.anim.fade_in) }
    }

    override fun isEnterAnimationEnabled(): Boolean {
        return false
    }

    override fun isExitAnimationEnabled(): Boolean {
        return false
    }

    override fun tag(): String {
        return TAG
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MovieListsViewModel::class.java)
    }

    override fun onViewLoaded() {
        super.onViewLoaded()
        initViews()
        subscribeToViewModel()
        subscribeToViewEvents()
    }

    override fun onDayNightModeApplied(type: Int) {
        activity?.let { ctx ->
            with(ContextCompat.getColor(ctx,R.color.colorAccentDark)){
                movies_list_toolbar?.setBackgroundColor(this)
                val foreground = ColorUtils.setAlphaComponent(this,(movies_list_toolbar?.alpha ?: 0f).times(255f).toInt())
                val background = ContextCompat.getColor(ctx,R.color.colorPrimary)
                ctx.setStatusBarColor(ColorUtils.compositeColors(foreground,background))
            }
            movies_recycler?.adapter?.takeIf { it is DayNightModeAwareAdapter }?.let {
                (it as DayNightModeAwareAdapter).onDayNightModeChanged(type)
            }
        }
    }

    override fun onSetStatusBarColor() {
        activity?.let { ctx ->
            val foreground = ColorUtils.setAlphaComponent(ContextCompat.getColor(ctx,R.color.colorAccentDark),(movies_list_toolbar?.alpha ?: 0f).times(255f).toInt())
            val background = ContextCompat.getColor(ctx,R.color.colorPrimary)
            ctx.setStatusBarColor(ColorUtils.compositeColors(foreground,background))
        }
    }

    private fun initViews() {
        movies_list_toolbar?.post{
            appbarHeight = movies_list_toolbar?.measuredHeight ?: 0
        }
        showSequenceGuide(listOf(
            (R.string.search to R.string.search_favorite_movies) to (SHOW_CASE_SEARCH_KEY to getSearchTabButtonFromParent())
            ,(R.string.categories to R.string.access_movies_by_categories) to (SHOW_CASE_CATEGORIES_KEY to getCategoriesTabButtonFromParent())
            ,(R.string.user_account to R.string.see_your_account_data) to (SHOW_CASE_ACCOUNT_KEY to getAccountTabButtonFromParent())
        ))
    }

    private fun getCategoriesTabButtonFromParent() : View? {
        val tabLayout = parentFragment?.view?.findViewById<TabLayout>(R.id.home_tab_layout)
        return tabLayout?.getTabAt(1)?.view
    }

    private fun getAccountTabButtonFromParent() : View? {
        val tabLayout = parentFragment?.view?.findViewById<TabLayout>(R.id.home_tab_layout)
        return tabLayout?.getTabAt(0)?.view
    }

    private fun getSearchTabButtonFromParent() : View? {
        val tabLayout = parentFragment?.view?.findViewById<TabLayout>(R.id.home_tab_layout)
        return tabLayout?.getTabAt(2)?.view
    }

    private fun subscribeToViewModel() {
        viewModel.moviesList.observe(viewLifecycleOwner, Observer { list ->
            movies_refresh_layout?.isRefreshing = false
            movies_recycler?.adapter?.takeIf { it is MovieListAdapter }?.let {
                (it as MovieListAdapter).updateData(list)
            } ?: kotlin.run {
                movies_recycler?.layoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
                val adapter = MovieListAdapter(list,onItemClickListener = this)
                movies_recycler?.adapter = adapter
            }
        })

        viewModel.specialMoviesList.observe(viewLifecycleOwner, Observer { headerList ->
            headerList?.let {
                movies_recycler?.adapter?.takeIf { it is MovieListAdapter }?.let {
                    (it as MovieListAdapter).updateHeaderData(headerList)
                } ?: kotlin.run {
                    movies_recycler?.layoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
                    val adapter = MovieListAdapter(viewModel.moviesList.value ?: listOf(),headerList,onItemClickListener = this)
                    movies_recycler?.adapter = adapter
                }
            }
        })

        viewModel.moviesError.observe(viewLifecycleOwner, Observer {
            //todo show error
//            it.messageResId?.let {
//                Toast.makeText(activity,it, Toast.LENGTH_SHORT).show()
//            }
            movies_recycler?.hide()
            movies_refresh_layout?.isRefreshing = false
        })

        viewModel.moviesExtraList.observe(viewLifecycleOwner, { data ->
            loadingExtraMovies = true
            movies_recycler?.adapter?.takeIf { it is MovieListAdapter }?.let {
                (it as MovieListAdapter).addData(data)
            } ?: kotlin.run {
                movies_recycler?.layoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
                val adapter = MovieListAdapter(data,viewModel.specialMoviesList.value ?: arrayListOf(),this@MovieListsFragment)
                movies_recycler?.adapter = adapter
            }
        })
    }

    private fun subscribeToViewEvents() {
        movies_search_btn?.setOnClickListener {
            parentFragment?.takeIf { it is HomeFragment }?.let {
                (it as HomeFragment).selectTabBy(SearchFragment.newInstance())
            }
        }
        movies_account_btn?.setOnClickListener {
            if(isUserLoggedIn()) {
                add(containerId(),AccountFragment.newInstance())
            } else {
                val enterPhoneNumberFragment = EnterPhoneNumberFragment.newInstance()
                val args = Bundle().apply {
                    putInt(EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE, EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE_LOGIN)
                }
                enterPhoneNumberFragment.arguments = args
                add(containerId(),enterPhoneNumberFragment)
            }
        }

        movies_refresh_layout?.setOnRefreshListener {
            viewModel.refresh()
        }

        movies_recycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var pastVisibleItems = 0
            private var visibleItemCount:Int = 0
            private var totalItemCount:Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val width = widthOfDevice()
                val height = width.times(35).div(50)
                val y = recyclerView.computeVerticalScrollOffset()
                val alpha = if (y >= height) 1f else (y.toFloat()/height.toFloat())
                movies_list_toolbar?.alpha = alpha
                activity?.let { ctx ->
                    val foreground = ColorUtils.setAlphaComponent(ContextCompat.getColor(ctx,R.color.colorAccentDark),alpha.times(255f).toInt())
                    val background = ContextCompat.getColor(ctx,R.color.colorPrimary)
                    ctx.setStatusBarColor(ColorUtils.compositeColors(foreground,background))
                }

                if(dy > 0) {
                    visibleItemCount = movies_recycler?.layoutManager?.childCount ?: 0
                    totalItemCount = movies_recycler?.layoutManager?.itemCount ?: 0
                    pastVisibleItems = (movies_recycler?.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition() ?: 0

                    if (loadingExtraMovies) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loadingExtraMovies = false
                            viewModel.fetchMovies()
                        }
                    }
                }
            }

        })
    }

    override fun onShowAllClick(movieListItem: MovieListItem) {
        val moviesFragment = MoviesFragment.newInstance()
        when (movieListItem) {
            is SuggestionMovies -> {
                moviesFragment.arguments = Bundle().apply {
                    this.putInt(MoviesFragment.MOVIES_REQUEST_TYPE,RequestType.SUGGESTION.type)
                    this.putInt(MoviesFragment.MOVIES_REQUEST_ID,movieListItem.suggestion.id)
                    this.putString(MoviesFragment.MOVIES_REQUEST_NAME, movieListItem.suggestion.name)
                }
            }
            is GenresSuggestionMovies -> {
                moviesFragment.arguments = Bundle().apply {
                    this.putInt(MoviesFragment.MOVIES_REQUEST_TYPE,RequestType.GENRES_SUGGESTION.type)
                    this.putIntArray(MoviesFragment.MOVIES_REQUEST_IDS,movieListItem.genresSuggestion.genreIds.toIntArray())
                    this.putString(MoviesFragment.MOVIES_REQUEST_NAME, movieListItem.genresSuggestion.name)
                }
            }
            else -> {}
        }
        add(containerId(),moviesFragment)
    }

    override fun onMovieClick(movie: Movie?) {
        movie?.let {
            val movieFragment = MovieFragment.newInstance()
            movieFragment.arguments = Bundle().apply {
                this.putInt(MovieFragment.MOVIE_ID_KEY,it.id)
            }
            add(containerId(),movieFragment)
            startLogMovie(it.name)
        }
    }

    override fun containerId() : Int{
        return R.id.movie_list_frame
    }

    override fun handleBack(): Boolean {
        return passHandleBackToParent()
    }

    override fun onHandleDeepLink() {
        super.onHandleDeepLink()
        activity?.takeIf { it is MainActivity }?.let { act ->
            (act as MainActivity).deepLink?.takeIf {
                it.scheme == Scheme.Http
                        && it.host == Host.PlayStopApp
                        && it.path1?.pathType == PathType.Open
                        && it.path2?.pathType == PathType.Movie }?.let {
                it.path2?.value?.let {
                    act.consumeDeepLink()
                    val movieFragment = MovieFragment.newInstance()
                    movieFragment.arguments = Bundle().apply { this.putInt(MovieFragment.MOVIE_ID_KEY,it.toInt()) }
                    add(containerId(),movieFragment)
                }
            }
            act.deepLink?.takeIf {
                it.scheme == Scheme.Http
                        && it.host == Host.PlayStop
                        && it.path1?.pathType == PathType.Search }?.let {
                parentFragment?.takeIf { it is HomeFragment }?.let {
                    (it as HomeFragment).selectTabBy(SearchFragment.newInstance())
                }
            }
        }
    }
}
