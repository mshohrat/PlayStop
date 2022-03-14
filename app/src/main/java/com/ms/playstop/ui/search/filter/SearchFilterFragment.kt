package com.ms.playstop.ui.search.filter

import android.content.res.ColorStateList
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.hideSoftKeyboard
import com.ms.playstop.model.SearchFilter
import com.ms.playstop.ui.search.SearchFragment
import com.ms.playstop.ui.search.filter.adapter.SearchFilterAdapter
import com.ms.playstop.utils.DayNightModeAwareAdapter
import com.ms.playstop.utils.DayNightModeAwareViewHolder
import kotlinx.android.synthetic.main.fragment_search_filter.*

class SearchFilterFragment : BaseFragment(), SearchFilterAdapter.OnDataChangedListener {

    companion object {
        fun newInstance() = SearchFilterFragment()
        const val TAG = "Search Filter Fragment"
        const val PARAM_SEARCH_FILTER = "PARAM_SEARCH_FILTER"
    }

    private lateinit var viewModel: SearchFilterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_filter, container, false)
    }

    override fun tag(): String {
        return TAG
    }

    override fun getExitAnimation(): Animation? {
        return activity?.let { AnimationUtils.loadAnimation(it,R.anim.slide_out_top) }
    }

    override fun getEnterAnimation(): Animation? {
        return activity?.let { AnimationUtils.loadAnimation(it,R.anim.slide_in_top) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchFilterViewModel::class.java)
        search_filter_back_btn?.hideSoftKeyboard()
    }

    override fun onDayNightModeApplied(type: Int) {
        activity?.let { ctx ->
            with(ContextCompat.getColor(ctx,R.color.colorPrimary)) {
                view?.setBackgroundColor(this)
                search_filter_clear_btn?.setTextColor(this)
                search_filter_submit_btn?.setTextColor(this)
            }
            search_filter_appbar?.setBackgroundColor(ContextCompat.getColor(ctx,R.color.colorAccentDark))
            search_filter_submit_btn?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.colorAccentDark))
            search_filter_recycler?.adapter?.takeIf { it is DayNightModeAwareAdapter }?.let {
                (it as DayNightModeAwareAdapter).onDayNightModeChanged(type)
            }
        }
    }

    override fun onViewLoaded() {
        super.onViewLoaded()
        viewModel.initTypes(activity)
        viewModel.initSorts(activity)
        parentFragment?.takeIf { it is SearchFragment }?.let {
            viewModel.updateFilter((it as SearchFragment).getSearchFilter() ?: SearchFilter())
        } ?: kotlin.run {
            viewModel.updateFilter(SearchFilter())
        }
        subscribeToViewModel()
        subscribeToViewEvents()
        initViews()
    }

    private fun subscribeToViewModel() {

    }

    private fun subscribeToViewEvents() {
        search_filter_back_btn?.setOnClickListener {
            activity?.onBackPressed()
        }
        search_filter_submit_btn?.setOnClickListener {
            parentFragment?.takeIf { it is SearchFragment }?.let {
                (it as SearchFragment).updateSearchFilter(viewModel.filter)
            }
            activity?.onBackPressed()
        }
        search_filter_clear_btn?.setOnClickListener {
            (search_filter_recycler?.adapter as? SearchFilterAdapter)?.clear()
        }
    }

    private fun initViews() {
        activity?.let { ctx ->
            val adapter = SearchFilterAdapter(ctx,viewModel.categories,viewModel.genres,viewModel.sorts,viewModel.languages,viewModel.years,viewModel.countries,viewModel.types,viewModel.filter,this)
            search_filter_recycler?.layoutManager = LinearLayoutManager(ctx,RecyclerView.VERTICAL,false)
            search_filter_recycler?.adapter = adapter
        }
    }

    override fun onDataChanged(filter: SearchFilter) {
        viewModel.updateFilter(filter)
    }

}