package com.ms.playstop.ui.movies

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.add
import com.ms.playstop.extension.hide
import com.ms.playstop.extension.startLogMovie
import com.ms.playstop.model.Movie
import com.ms.playstop.ui.movie.MovieFragment
import com.ms.playstop.ui.movieLists.adapter.MovieAdapter
import com.ms.playstop.ui.movies.adapter.MovieDateSource
import com.ms.playstop.ui.movies.adapter.MoviePagedAdapter
import com.ms.playstop.ui.movies.adapter.RequestType
import com.ms.playstop.utils.GridSpacingItemDecoration
import com.ms.playstop.utils.RtlGridLayoutManager
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.android.synthetic.main.fragment_movies.movies_recycler
import kotlinx.android.synthetic.main.fragment_movies.movies_refresh_layout

class MoviesFragment : BaseFragment(), MoviePagedAdapter.OnItemClickListener,
    MovieAdapter.OnItemClickListener {

    companion object {
        fun newInstance() = MoviesFragment()
        const val MOVIES_REQUEST_TYPE = "MOVIES REQUEST TYPE"
        const val MOVIES_REQUEST_ID = "MOVIES REQUEST ID"
        const val MOVIES_REQUEST_NAME = "MOVIES REQUEST NAME"
    }

    private lateinit var viewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun tag(): String {
        return "Movies Fragment"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        viewModel = ViewModelProviders.of(this).get(MoviesViewModel::class.java)
        val moviesRequestId = arguments?.takeIf { it.containsKey(MOVIES_REQUEST_TYPE) }
            ?.getInt(MOVIES_REQUEST_ID) ?: -1
        var moviesRequestType : RequestType = RequestType.SUGGESTION
        arguments?.takeIf { it.containsKey(MOVIES_REQUEST_TYPE) }?.let {
            when(it.getInt(MOVIES_REQUEST_TYPE)) {
                RequestType.SUGGESTION.type -> {
                    moviesRequestType = RequestType.SUGGESTION
                }
                RequestType.CATEGORY.type -> {
                    moviesRequestType = RequestType.CATEGORY
                }
                RequestType.GENRE.type -> {
                    moviesRequestType = RequestType.GENRE
                }
                RequestType.SPECIAL.type -> {
                    moviesRequestType = RequestType.SPECIAL
                }
                RequestType.YEAR.type -> {
                    moviesRequestType = RequestType.YEAR
                }
                RequestType.LIKES.type -> {
                    moviesRequestType = RequestType.LIKES
                }
                else -> {}
            }
        }
        viewModel.setRequestType(moviesRequestType, moviesRequestId)
        subscribeToViewEvents()
        subscribeToViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_sort, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initViews() {
        arguments?.takeIf { it.containsKey(MOVIES_REQUEST_NAME) }?.let {
            movies_toolbar_name_tv?.text = it.getString(MOVIES_REQUEST_NAME)
        }

        val lm = RtlGridLayoutManager(activity, 3, RecyclerView.VERTICAL, false)
        movies_loading_recycler?.layoutManager = lm
        val spacing = activity?.resources?.getDimensionPixelSize(R.dimen.margin_medium) ?: 0
        movies_loading_recycler?.addItemDecoration(GridSpacingItemDecoration(3, spacing, true))
        val adapter = MovieAdapter(listOf(), this, 9, false)
        movies_loading_recycler?.adapter = adapter

        val layoutManager = RtlGridLayoutManager(activity, 3, RecyclerView.VERTICAL, false)
        movies_recycler?.layoutManager = layoutManager
    }

    private fun subscribeToViewEvents() {
        movies_back_btn?.setOnClickListener {
            activity?.onBackPressed()
        }
        movies_refresh_layout?.setOnRefreshListener {
            viewModel.refresh()
        }
        movies_sort_layout?.setOnClickListener {
            showSortDialog()
        }
    }

    private fun subscribeToViewModel() {
        viewModel.movies.observe(viewLifecycleOwner, Observer { list ->
            movies_recycler?.itemDecorationCount?.takeIf { it == 0 }?.let {
                val spacing = activity?.resources?.getDimensionPixelSize(R.dimen.margin_medium) ?: 0
                movies_recycler?.addItemDecoration(GridSpacingItemDecoration(3, spacing, true))
            }
            movies_recycler?.adapter?.takeIf { it is MoviePagedAdapter }?.let {
                (it as MoviePagedAdapter).submitList(list)
            } ?: kotlin.run {
                movies_recycler?.adapter = MoviePagedAdapter(this).apply {
                    submitList(list)
                }
            }
            movies_refresh_layout?.isRefreshing = false
        })

        viewModel.moviesError.observe(viewLifecycleOwner, Observer {
            it.messageResId?.let {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            }
            movies_refresh_layout?.isRefreshing = false
        })

        viewModel.moviesRequestState.observe(viewLifecycleOwner, Observer {
            if (it != MovieDateSource.STATE_LOADING) {
                movies_loading_recycler?.adapter = null
                movies_loading_recycler?.hide()
                movies_refresh_layout?.isRefreshing = false
            }
        })

        viewModel.selectedSortName.observe(viewLifecycleOwner, Observer {
            movies_sort_name_tv?.setText(it)
        })
    }

    override fun onItemClick(movie: Movie?) {
        movie?.let {
            val movieFragment = MovieFragment.newInstance()
            movieFragment.arguments = Bundle().apply { this.putInt(
                MovieFragment.MOVIE_ID_KEY,
                it.id
            ) }
            parentFragment?.takeIf { it is BaseFragment }?.let {
                (it as BaseFragment).add(it.containerId(), movieFragment)
            }
            startLogMovie(it.name)
        }
    }

    private fun showSortDialog() {
        activity?.let { ctx ->

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                PopupMenu(ctx, movies_toolbar,Gravity.START,R.attr.popupMenuStyle ,0)
            } else {
                PopupMenu(ctx, movies_toolbar)
            }.apply {
                setOnMenuItemClickListener { item ->
                    viewModel.updateSort(item?.itemId)
                    true
                }
                inflate(R.menu.menu_sort)
                if(viewModel.isYearMovies){
                    menu.findItem(R.id.action_sort_newest).isVisible = false
                    menu.findItem(R.id.action_sort_oldest).isVisible = false
                }
                menu.findItem(viewModel.getSelectedSortMenuItemId()).isChecked = true
                show()
            }
        }
    }
}
