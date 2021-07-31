package com.ms.playstop.ui.search

import android.content.res.ColorStateList
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.ms.playstop.MainActivity

import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.*
import com.ms.playstop.model.*
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.ui.movie.MovieFragment
import com.ms.playstop.ui.movieLists.adapter.MovieAdapter
import com.ms.playstop.ui.search.filter.SearchFilterFragment
import com.ms.playstop.utils.DayNightModeAwareAdapter
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

    override fun getEnterAnimation(): Animation? {
        return activity?.let { AnimationUtils.loadAnimation(it,R.anim.fade_in) } ?: kotlin.run { null }
    }

    override fun isExitAnimationEnabled(): Boolean {
        return false
    }

    override fun isEnterAnimationEnabled(): Boolean {
        return false
    }

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
    }

    override fun onViewLoaded() {
        super.onViewLoaded()
        subscribeToViewModel()
        subscribeToViewEvents()
        initViews()
    }

    override fun onDayNightModeApplied(type: Int) {
        activity?.let { ctx ->
            view?.setBackgroundColor(ContextCompat.getColor(ctx,R.color.colorPrimary))
            with(MaterialShapeDrawable(ShapeAppearanceModel.builder()
                .setAllCornerSizes(ctx.resources.getDimensionPixelSize(R.dimen.background_card_radius).toFloat())
                .build()
            ).apply {
                fillColor = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.white_opacity_10))
            }) {
                search_et?.background = this
            }
            search_et?.setTextColor(ContextCompat.getColor(ctx,R.color.white))
            search_et?.setHintTextColor(ContextCompat.getColor(ctx,R.color.gray))
            search_et?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                AppCompatResources.getDrawable(ctx,R.drawable.ic_search_dark_blue),
                null
            )
            search_filter_btn?.setImageDrawable(AppCompatResources.getDrawable(ctx,R.drawable.ic_filter))
            search_clear_btn?.setImageDrawable(AppCompatResources.getDrawable(ctx,R.drawable.ic_clear_white))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                search_no_result_tv?.setTextAppearance(ctx.getResourceFromThemeAttribute(R.attr.textAppearanceCaption,R.style.Caption_FixSize))
            } else {
                search_no_result_tv?.setTextAppearance(ctx,ctx.getResourceFromThemeAttribute(R.attr.textAppearanceCaption,R.style.Caption_FixSize))
            }
            search_recycler?.adapter?.takeIf { it is DayNightModeAwareAdapter }?.let {
                (it as DayNightModeAwareAdapter).onDayNightModeChanged(type)
            }
        }
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
        viewModel.searchError.observe(viewLifecycleOwner, Observer {
            dismissLoading()
            showToast(it)
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
        search_filter_btn?.setOnClickListener {
            add(containerId(),SearchFilterFragment.newInstance().apply {
                arguments = Bundle().apply {
                    putParcelable(SearchFilterFragment.PARAM_SEARCH_FILTER,viewModel.searchFilter)
                }
            })
        }
    }

    private fun showToast(response: GeneralResponse) {
        response.message?.takeIf { it.isNotEmpty() }?.let {
            Toast.makeText(activity,it, Toast.LENGTH_SHORT).show()
        } ?: kotlin.run {
            response.messageResId?.let {
                Toast.makeText(activity,it, Toast.LENGTH_SHORT).show()
            }
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
        if(hidden){
            search_et?.hideSoftKeyboard()
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

    fun updateSearchFilter(searchFilter: SearchFilter) {
        viewModel.searchFilter = searchFilter
        search_et?.text = search_et?.text
    }

    fun getSearchFilter() : SearchFilter? {
        return viewModel.searchFilter
    }

}
