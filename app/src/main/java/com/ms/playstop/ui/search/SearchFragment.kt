package com.ms.playstop.ui.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView

import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.*
import com.ms.playstop.model.Movie
import com.ms.playstop.ui.movie.MovieFragment
import com.ms.playstop.ui.movieLists.adapter.MovieAdapter
import com.ms.playstop.utils.GridSpacingItemDecoration
import com.ms.playstop.utils.LoadingDialog
import com.ms.playstop.utils.RtlGridLayoutManager
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment(), MovieAdapter.OnItemClickListener {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel
    private var loadingDialog: LoadingDialog? = null
    private val DELAY_THRESHOLD = 800L
    private val handler = Handler()
    private val searchRunnable = Runnable {
        search_et?.text?.toString()?.takeIf { it.isNotEmpty() }?.let {
            showLoadingDialog()
            viewModel.searchMovie(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun tag(): String {
        return "Search Fragment"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        subscribeToViewModel()
        subscribeToViewEvents()
    }

    private fun subscribeToViewModel() {
        viewModel.searchResult.observe(viewLifecycleOwner, Observer {
            dismissLoadingDialog()
            if(it.isEmpty()) {
                search_recycler?.hide()
                search_no_result_group?.show()
            } else {
                search_no_result_group?.hide()
                search_recycler?.show()
                if(search_recycler?.layoutManager == null) {
                    val layoutManager = RtlGridLayoutManager(activity,3, RecyclerView.VERTICAL,false)
                    search_recycler?.layoutManager = layoutManager
                    val spacing = activity?.resources?.getDimensionPixelSize(R.dimen.margin_medium) ?: 0
                    search_recycler?.addItemDecoration(GridSpacingItemDecoration(3,spacing,true))
                }
                val adapter = MovieAdapter(it,this,0,false)
                search_recycler?.adapter = adapter
            }
        })
    }

    private fun subscribeToViewEvents() {
        search_et?.doOnTextChanged { text, start, before, count ->
            text?.toString()?.takeIf { it.isNotEmpty() }?.let {
                handler.removeCallbacks(searchRunnable)
                handler.postDelayed(searchRunnable,DELAY_THRESHOLD)
                search_clear_btn?.show()
            } ?: kotlin.run {
                handler.removeCallbacks(searchRunnable)
                search_clear_btn?.hide()
                search_no_result_group?.hide()
                search_recycler?.adapter = null
            }
        }
        search_clear_btn?.setOnClickListener {
            search_et?.text = null
        }
    }

    override fun onItemClick(movie: Movie?) {
        movie?.let {
            val movieFragment = MovieFragment.newInstance()
            movieFragment.arguments = Bundle().apply {
                this.putInt(MovieFragment.MOVIE_ID_KEY,it.id)
            }
            add(containerId(),movieFragment)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
//        if(hidden){
//            search_et?.text.takeIf { it.isNullOrEmpty() }?.let {
//                search_no_result_group?.hide()
//            }
//        }
    }

    override fun handleBack(): Boolean {
        if(search_et?.text?.isNotEmpty() == true) {
            search_et?.text = null
            return true
        } else {
            return passHandleBackToParent()
        }
    }

    override fun containerId(): Int {
        return R.id.search_frame
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
