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
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.*
import com.ms.playstop.model.Movie
import com.ms.playstop.model.Suggestion
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
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.android.synthetic.main.fragment_movie_lists.*
import kotlinx.android.synthetic.main.fragment_movie_lists.movies_recycler
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlin.math.abs

class MovieListsFragment : BaseFragment(), MovieListAdapter.OnItemClickListener,
    MovieHeaderAdapter.OnItemClickListener {

    companion object {
        fun newInstance() = MovieListsFragment()
        const val TAG = "Movie Lists Fragment"
    }

    private lateinit var viewModel: MovieListsViewModel
    private var specialMoviesName = ""
    private var appbarHeight = 0

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
            appbarHeight = movies_appbar.measuredHeight
        }
    }

    private fun subscribeToViewModel() {
//        viewModel.suggestions.observe(viewLifecycleOwner, Observer {
//            for (suggestion in it) {
//                viewModel.fetchMovies(suggestion)
//            }
//        })

        viewModel.moviesList.observe(viewLifecycleOwner, Observer { list ->
            movies_recycler?.adapter?.takeIf { it is MovieListAdapter }?.let {
                (it as MovieListAdapter).updateData(list)
            } ?: kotlin.run {
                movies_recycler?.layoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
                val adapter = MovieListAdapter(list,this)
                movies_recycler?.adapter = adapter
            }
        })

        viewModel.specialMoviesList.observe(viewLifecycleOwner, Observer {
            //specialMoviesName = it.name
            //movies_top_title_tv?.text = specialMoviesName
            movies_top_layout?.show()
            val adapter = MovieHeaderAdapter(it,this)
            movies_top_recycler?.adapter = adapter
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
//        movies_top_show_all_btn?.setOnClickListener {
//            val moviesFragment = MoviesFragment.newInstance()
//            moviesFragment.arguments = Bundle().apply {
//                this.putInt(MoviesFragment.MOVIES_REQUEST_TYPE,RequestType.SPECIAL.type)
//                this.putInt(MoviesFragment.MOVIES_REQUEST_ID,-1)
//                this.putString(MoviesFragment.MOVIES_REQUEST_NAME,specialMoviesName)
//            }
//            add(containerId(),moviesFragment)
//        }

        movies_appbar?.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            //var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                appBarLayout?.let {
//                    if (scrollRange == -1) {
//                        scrollRange = appbarHeight.minus(movies_toolbar?.bottom ?: 0)
//                    }
                    if (verticalOffset == 0) {
                        movies_toolbar?.setBackgroundResource(android.R.color.transparent)
                    } else{
                        movies_toolbar?.setBackgroundResource(R.color.colorAccentDarkOpacity50)
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
            movieFragment.arguments = Bundle().apply { this.putInt(MovieFragment.MOVIE_ID_KEY,it.id) }
            add(containerId(),movieFragment)
        }
    }

    override fun containerId() : Int{
        return R.id.movie_list_frame
    }

    override fun handleBack(): Boolean {
        return passHandleBackToParent()
    }

}
