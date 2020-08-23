package com.ms.playstop.ui.search

import android.content.res.Resources
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.convertDpToPixel
import com.ms.playstop.extension.hide
import com.ms.playstop.extension.passHandleBackToParent
import com.ms.playstop.extension.show
import com.ms.playstop.model.Movie
import com.ms.playstop.ui.movieLists.adapter.MovieAdapter
import com.ms.playstop.utils.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment(), MovieAdapter.OnItemClickListener {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel

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
            if(it.isEmpty()) {
                search_recycler?.hide()
                search_no_result_group?.show()
            } else {
                search_no_result_group?.hide()
                search_recycler?.show()
                if(search_recycler?.layoutManager == null) {
                    val layoutManager = GridLayoutManager(activity,2, RecyclerView.VERTICAL,false)
                    search_recycler?.layoutManager = layoutManager
                    val itemWidth = activity?.resources?.getDimensionPixelSize(R.dimen.item_movie_width) ?: 0
                    val itemMargin = activity?.resources?.getDimensionPixelSize(R.dimen.margin_standard) ?: 0
                    val spacing = search_recycler?.measuredWidth?.minus(itemWidth*2)?.minus(itemMargin*2)?.div(3) ?: 0
                    search_recycler?.addItemDecoration(GridSpacingItemDecoration(2,spacing,true))
                }
                val adapter = MovieAdapter(it,this)
                search_recycler?.adapter = adapter
            }
        })
    }

    private fun subscribeToViewEvents() {
        search_et?.doOnTextChanged { text, start, before, count ->
            text?.toString()?.takeIf { it.isNotEmpty() }?.let {
                viewModel.searchMovie(it)
                search_clear_btn?.show()
            } ?: kotlin.run {
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
        //todo route to movie fragment
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(hidden){
            search_et?.text = null
            search_no_result_group?.hide()
        }
    }

    override fun handleBack(): Boolean {
        if(search_et?.text?.isNotEmpty() == true) {
            search_et?.text = null
            return true
        } else {
            return passHandleBackToParent()
        }
    }

}
