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
import com.ms.playstop.MainActivity

import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.*
import com.ms.playstop.model.Host
import com.ms.playstop.model.Movie
import com.ms.playstop.model.PathType
import com.ms.playstop.model.Scheme
import com.ms.playstop.ui.movie.MovieFragment
import com.ms.playstop.ui.movieLists.adapter.MovieAdapter
import com.ms.playstop.utils.GridSpacingItemDecoration
import com.ms.playstop.utils.RtlGridLayoutManager
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment(), MovieAdapter.OnItemClickListener {

    companion object {
        fun newInstance() = SearchFragment()
        const val TAG = "Search Fragment"
    }

    private lateinit var viewModel: SearchViewModel
    private val DELAY_THRESHOLD = 800L
    private val handler = Handler()
    private val searchRunnable = Runnable {
        search_et?.text?.toString()?.takeIf { it.isNotEmpty() }?.let {
            showLoading()
            viewModel.searchMovie(it)
        }
    }
    private var searchPhraseFromDeepLink : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun tag(): String {
        return TAG
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        subscribeToViewModel()
        subscribeToViewEvents()
        initViews()
    }

    private fun initViews() {
        //search_et?.requestFocus()
    }

    private fun subscribeToViewModel() {
        viewModel.searchResult.observe(viewLifecycleOwner, Observer {
            dismissLoading()
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
                endLogAndIndexMovie(search_et?.text?.toString())
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

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        searchPhraseFromDeepLink?.let {
            search_et?.setText(it)
            searchPhraseFromDeepLink = null
        }
    }

    override fun containerId(): Int {
        return R.id.search_frame
    }

    private fun showLoading() {
        search_loading?.show()
    }

    private fun dismissLoading() {
        search_loading?.hide()
    }

    override fun onHandleDeepLink() {
        super.onHandleDeepLink()
        activity?.takeIf { it is MainActivity }?.let { act ->
            (act as MainActivity).deepLink?.takeIf {
                it.scheme == Scheme.Http
                        && it.host == Host.PlayStop
                        && it.path1?.pathType == PathType.Search }?.let {
                it.path1?.value?.let {
                    act.consumeDeepLink()
                    searchPhraseFromDeepLink = it
                }
            }
        }
    }

}
