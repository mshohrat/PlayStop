package com.ms.playstop.ui.movieLists

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.ms.playstop.ui.home.HomeFragment
import com.ms.playstop.ui.login.LoginFragment
import com.ms.playstop.ui.movie.MovieFragment
import com.ms.playstop.ui.movieLists.adapter.MovieHeaderAdapter
import com.ms.playstop.ui.movieLists.adapter.MovieListAdapter
import com.ms.playstop.ui.movies.MoviesFragment
import com.ms.playstop.ui.movies.adapter.RequestType
import com.ms.playstop.ui.search.SearchFragment
import com.ms.playstop.utils.LinePagerIndicatorDecoration
import com.ms.playstop.utils.RtlLinearLayoutManager
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
    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_lists, container, false)
    }

    override fun tag(): String {
        return TAG
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MovieListsViewModel::class.java)
        initViews()
        subscribeToViewModel()
        subscribeToViewEvents()
    }

    private fun initViews() {
        movies_top_recycler?.layoutManager = RtlLinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
        movies_top_recycler?.addItemDecoration(LinePagerIndicatorDecoration())
        PagerSnapHelper().attachToRecyclerView(movies_top_recycler)

        movies_appbar?.post{
            appbarHeight = movies_appbar?.measuredHeight ?: 0
        }
        showSequenceGuide(listOf(
            (R.string.user_account to R.string.see_your_account_data) to (SHOW_CASE_ACCOUNT_KEY to movies_account_btn)
            ,(R.string.search to R.string.search_favorite_movies) to (SHOW_CASE_SEARCH_KEY to movies_search_btn)
            ,(R.string.categories to R.string.access_movies_by_categories) to (SHOW_CASE_CATEGORIES_KEY to getCategoriesTabButtonFromParent())
        ))
    }

    private fun getCategoriesTabButtonFromParent() : View? {
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
                movies_recycler?.layoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
                val adapter = MovieListAdapter(list,this)
                movies_recycler?.adapter = adapter
            }
        })

        viewModel.specialMoviesList.observe(viewLifecycleOwner, Observer {
            it?.let {
                movies_top_layout?.show()
                val adapter = MovieHeaderAdapter(it,this)
                movies_top_recycler?.adapter = adapter
                //initAutoScrollForHeaderRecycler()
            } ?: kotlin.run {
                movies_top_layout?.hide()
                //cancelAutoScrollOfHeaderRecycler()
            }
        })

        viewModel.moviesError.observe(viewLifecycleOwner, Observer {
            //todo show error
//            it.messageResId?.let {
//                Toast.makeText(activity,it, Toast.LENGTH_SHORT).show()
//            }
            movies_recycler?.hide()
            movies_top_layout?.hide()
            movies_refresh_layout?.isRefreshing = false
        })
    }

    private fun subscribeToViewEvents() {
        movies_search_btn?.setOnClickListener {
            parentFragment?.takeIf { it is HomeFragment }?.addOrShow(SearchFragment.newInstance())
        }
        movies_account_btn?.setOnClickListener {
            if(isUserLoggedIn()) {
                add(containerId(),AccountFragment.newInstance())
            } else {
                add(containerId(),LoginFragment.newInstance())
            }
        }

        movies_appbar?.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                appBarLayout?.let {
                    if (verticalOffset == 0) {
                        movies_toolbar?.setBackgroundResource(android.R.color.transparent)
                    } else{
                        movies_toolbar?.setBackgroundResource(R.color.colorAccentDarkOpacity50)
                    }
                }

            }

        })

        movies_refresh_layout?.setOnRefreshListener {
            viewModel.refresh()
        }
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
                        movies_top_recycler?.smoothScrollToPosition(0)
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
