package com.ms.playstop.ui.movieLists

import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
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
import com.ms.playstop.utils.LinePagerIndicatorDecoration
import com.ms.playstop.utils.RtlLinearLayoutManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_movie_lists.*
import kotlinx.android.synthetic.main.fragment_movie_lists.movies_recycler
import kotlinx.android.synthetic.main.fragment_movie_lists.movies_refresh_layout
import kotlinx.android.synthetic.main.fragment_movies.*
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
    private val disposables = CompositeDisposable()
    private var topRecyclerItemDecoration = LinePagerIndicatorDecoration()
    private var isTopMovieListLoaded = false

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

            //movies_collapsing_toolbar?.contentScrim = ColorDrawable(ContextCompat.getColor(ctx,R.color.colorAccentDark))

            movies_recycler?.adapter?.takeIf { it is DayNightModeAwareAdapter }?.let {
                (it as DayNightModeAwareAdapter).onDayNightModeChanged(type)
            }
            movies_top_recycler?.adapter?.takeIf { it is DayNightModeAwareAdapter }?.let {
                (it as DayNightModeAwareAdapter).onDayNightModeChanged(type)
            }
            movies_top_recycler?.takeIf { it.itemDecorationCount > 0 }?.removeItemDecoration(topRecyclerItemDecoration)
            topRecyclerItemDecoration = LinePagerIndicatorDecoration()
            movies_top_recycler?.addItemDecoration(topRecyclerItemDecoration)
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
        movies_top_recycler?.layoutManager = object : RtlLinearLayoutManager(activity,RecyclerView.HORIZONTAL,false) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        movies_top_recycler?.addItemDecoration(topRecyclerItemDecoration)
        movies_top_recycler?.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when(e.action) {
                    MotionEvent.ACTION_DOWN -> cancelAutoScrollOfHeaderRecycler()
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        if(isTopMovieListLoaded){
                            initAutoScrollForHeaderRecycler()
                        }
                    }
                    else -> {}
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

            }

        })
        PagerSnapHelper().attachToRecyclerView(movies_top_recycler)

        movies_list_toolbar?.post{
            appbarHeight = movies_list_toolbar?.measuredHeight ?: 0
        }
        showSequenceGuide(listOf(
            (R.string.user_account to R.string.see_your_account_data) to (SHOW_CASE_ACCOUNT_KEY to getAccountTabButtonFromParent())
            ,(R.string.search to R.string.search_favorite_movies) to (SHOW_CASE_SEARCH_KEY to movies_search_btn)
            ,(R.string.categories to R.string.access_movies_by_categories) to (SHOW_CASE_CATEGORIES_KEY to getCategoriesTabButtonFromParent())
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

    private fun subscribeToViewModel() {
//        viewModel.suggestions.observe(viewLifecycleOwner, Observer {
//            for (suggestion in it) {
//                viewModel.fetchMovies(suggestion)
//            }
//        })

        viewModel.moviesList.observe(viewLifecycleOwner, Observer { list ->
            movies_refresh_layout?.isRefreshing = false
            movies_recycler?.adapter?.takeIf { it is MovieListAdapter }?.let {
                (it as MovieListAdapter).updateData(list)
            } ?: kotlin.run {
                movies_recycler?.layoutManager = object :LinearLayoutManager(activity,RecyclerView.VERTICAL,false){
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }
                val adapter = MovieListAdapter(list,this)
                movies_recycler?.adapter = adapter
            }
            isTopMovieListLoaded = true
        })

        viewModel.specialMoviesList.observe(viewLifecycleOwner, Observer {
            it?.let {
                //movies_top_layout?.show()
                val adapter = MovieHeaderAdapter(it,this)
                movies_top_recycler?.adapter = adapter
                initAutoScrollForHeaderRecycler()
            } ?: kotlin.run {
                //movies_top_layout?.hide()
                cancelAutoScrollOfHeaderRecycler()
            }
        })

        viewModel.moviesError.observe(viewLifecycleOwner, Observer {
            //todo show error
//            it.messageResId?.let {
//                Toast.makeText(activity,it, Toast.LENGTH_SHORT).show()
//            }
            movies_recycler?.hide()
            //movies_top_layout?.hide()
            movies_refresh_layout?.isRefreshing = false
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

//        movies_appbar?.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
//            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
//                appBarLayout?.let {
//                    if (verticalOffset == 0) {
//                        movies_list_toolbar?.setBackgroundResource(android.R.color.transparent)
//                    } else{
//                        movies_list_toolbar?.setBackgroundResource(R.color.colorAccentDarkOpacity50)
//                    }
//                }
//
//            }
//
//        })

        movies_refresh_layout?.setOnRefreshListener {
            viewModel.refresh()
        }

        movies_nested_scroll?.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                movies_top_recycler?.measuredHeight?.times(8)?.div(10)?.let { height ->
                    val alpha = if (scrollY >= height) 1f else (scrollY.toFloat()/height.toFloat())
                    movies_list_toolbar?.alpha = alpha
                    activity?.let { ctx ->
                        val foreground = ColorUtils.setAlphaComponent(ContextCompat.getColor(ctx,R.color.colorAccentDark),alpha.times(255f).toInt())
                        val background = ContextCompat.getColor(ctx,R.color.colorPrimary)
                        ctx.setStatusBarColor(ColorUtils.compositeColors(foreground,background))
                    }
                }
            }

        })
    }

    override fun onShowAllClick(suggestion: Suggestion) {
        val moviesFragment = MoviesFragment.newInstance()
        moviesFragment.arguments = Bundle().apply {
            this.putInt(MoviesFragment.MOVIES_REQUEST_TYPE,RequestType.SUGGESTION.type)
            this.putInt(MoviesFragment.MOVIES_REQUEST_ID,suggestion.id)
            this.putString(MoviesFragment.MOVIES_REQUEST_NAME,suggestion.name)
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

//    override fun onDestroyView() {
//        super.onDestroyView()
//        disposables.takeIf { it.isDisposed.not() }?.dispose()
//    }


    private fun initAutoScrollForHeaderRecycler() {
        cancelAutoScrollOfHeaderRecycler()
        disposables.add(Observable.interval(5L,TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                movies_top_recycler?.layoutManager?.takeIf { it is LinearLayoutManager }?.let {
                    val pos = (it as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                    if(pos == movies_top_recycler?.adapter?.itemCount?.minus(1) ?: 0) {
                        movies_top_recycler?.scrollToPosition(0)
                    } else {
                        movies_top_recycler?.smoothScrollToPosition(pos+1)
                    }
                }
            },{

            }))
    }

    private fun cancelAutoScrollOfHeaderRecycler() {
        disposables.clear()
    }

}
