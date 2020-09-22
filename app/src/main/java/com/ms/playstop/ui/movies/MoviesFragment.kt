package com.ms.playstop.ui.movies

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView

import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.add
import com.ms.playstop.extension.hide
import com.ms.playstop.model.Movie
import com.ms.playstop.ui.movie.MovieFragment
import com.ms.playstop.ui.movieLists.adapter.MovieAdapter
import com.ms.playstop.ui.movies.adapter.MovieDateSource
import com.ms.playstop.ui.movies.adapter.MoviePagedAdapter
import com.ms.playstop.ui.movies.adapter.RequestType
import com.ms.playstop.utils.GridSpacingItemDecoration
import com.ms.playstop.utils.LoadingDialog
import com.ms.playstop.utils.RtlGridLayoutManager
import kotlinx.android.synthetic.main.fragment_movies.*

class MoviesFragment : BaseFragment(), MoviePagedAdapter.OnItemClickListener,
    MovieAdapter.OnItemClickListener {

    companion object {
        fun newInstance() = MoviesFragment()
        const val MOVIES_REQUEST_TYPE = "MOVIES REQUEST TYPE"
        const val MOVIES_REQUEST_ID = "MOVIES REQUEST ID"
        const val MOVIES_REQUEST_NAME = "MOVIES REQUEST NAME"
    }

    private lateinit var viewModel: MoviesViewModel

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
                else -> {}
            }
        }
        viewModel.setRequestType(moviesRequestType,moviesRequestId)
        subscribeToViewEvents()
        subscribeToViewModel()
    }

    private fun initViews() {
        arguments?.takeIf { it.containsKey(MOVIES_REQUEST_NAME) }?.let {
            movies_toolbar_name_tv?.text = it.getString(MOVIES_REQUEST_NAME)
        }

        val lm = RtlGridLayoutManager(activity,3, RecyclerView.VERTICAL,false)
        movies_loading_recycler?.layoutManager = lm
        val spacing = activity?.resources?.getDimensionPixelSize(R.dimen.margin_medium) ?: 0
        movies_loading_recycler?.addItemDecoration(GridSpacingItemDecoration(3,spacing,true))
        val adapter = MovieAdapter(listOf(),this,9,false)
        movies_loading_recycler?.adapter = adapter

        val layoutManager = RtlGridLayoutManager(activity,3, RecyclerView.VERTICAL,false)
        movies_recycler?.layoutManager = layoutManager
    }

    private fun subscribeToViewEvents() {
        movies_back_btn?.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun subscribeToViewModel() {
        viewModel.movies.observe(viewLifecycleOwner, Observer { list ->
//            val itemWidth = activity?.resources?.getDimensionPixelSize(R.dimen.item_movie_width) ?: 0
//            val spacing = movies_recycler?.measuredWidth?.minus(itemWidth*2)?.div(3) ?: 0
            val spacing = activity?.resources?.getDimensionPixelSize(R.dimen.margin_medium) ?: 0
            movies_recycler?.addItemDecoration(GridSpacingItemDecoration(3,spacing,true))
            movies_recycler?.adapter?.takeIf { it is MoviePagedAdapter }?.let {
                (it as MoviePagedAdapter).submitList(list)
            } ?: kotlin.run {
                movies_recycler?.adapter = MoviePagedAdapter(this).apply {
                    submitList(list)
                }
            }
        })

        viewModel.moviesError.observe(viewLifecycleOwner, Observer {
            it.messageResId?.let {
                Toast.makeText(activity,it,Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.moviesRequestState.observe(viewLifecycleOwner, Observer {
            if(it != MovieDateSource.STATE_LOADING) {
                movies_loading_recycler?.adapter = null
                movies_loading_recycler?.hide()
            }
        })
    }

    override fun onItemClick(movie: Movie?) {
        movie?.let {
            val movieFragment = MovieFragment.newInstance()
            movieFragment.arguments = Bundle().apply { this.putInt(MovieFragment.MOVIE_ID_KEY,it.id) }
            parentFragment?.takeIf { it is BaseFragment }?.let {
                (it as BaseFragment).add(it.containerId(),movieFragment)
            }
        }
    }

}
