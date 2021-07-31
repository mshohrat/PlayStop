package com.ms.playstop.ui.character

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.*
import com.ms.playstop.model.Movie
import com.ms.playstop.ui.movie.MovieFragment
import com.ms.playstop.ui.movieLists.adapter.MovieAdapter
import com.ms.playstop.ui.movies.adapter.MovieDateSource
import com.ms.playstop.ui.movies.adapter.MoviePagedAdapter
import com.ms.playstop.ui.movies.adapter.RequestType
import com.ms.playstop.utils.DayNightModeAwareAdapter
import com.ms.playstop.utils.GridSpacingItemDecoration
import com.ms.playstop.utils.RtlGridLayoutManager
import kotlinx.android.synthetic.main.fragment_character.*
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlin.math.max

class CharacterFragment : BaseFragment(), MovieAdapter.OnItemClickListener,
    MoviePagedAdapter.OnItemClickListener {

    companion object {
        fun newInstance() = CharacterFragment()
        const val CHARACTER_TYPE_ACTOR = 100
        const val CHARACTER_TYPE_DIRECTOR = 101
        const val CHARACTER_TYPE_WRITER = 102
        const val CHARACTER_TYPE_KEY = "Character Type Key"
        const val CHARACTER_ID_KEY = "Character ID Key"
        const val TAG = "Character Fragment"
    }

    private lateinit var viewModel: CharacterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_character, container, false)
    }

    override fun tag(): String {
        return TAG
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CharacterViewModel::class.java)
    }

    override fun onDayNightModeApplied(type: Int) {
        activity?.let { ctx ->
            with(ctx.getResourceFromThemeAttribute(R.attr.textAppearanceHeadline3,R.style.Headline3_FixSize)) {
                character_collapsing_toolbar_layout?.setCollapsedTitleTextAppearance(this)
                character_collapsing_toolbar_layout?.setExpandedTitleTextAppearance(this)
            }
            with(ContextCompat.getColor(ctx,R.color.colorAccentDark)){
                character_collapsing_toolbar_layout?.contentScrim = ColorDrawable(this)
                character_collapsing_toolbar_root?.setBackgroundColor(this)
            }
            character_shimmer_name_tv?.background = MaterialShapeDrawable(
                ShapeAppearanceModel.builder()
                    .setAllCornerSizes(ctx.resources.getDimensionPixelSize(R.dimen.shimmer_radius).toFloat())
                    .build()
            ).apply {
                fillColor = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.gray))
            }
            character_movies_frame?.setBackgroundColor(ContextCompat.getColor(ctx,R.color.colorPrimary))
            character_shimmer_image?.background = MaterialShapeDrawable(
                ShapeAppearanceModel.builder()
                    .setAllCornerSizes(max(character_shimmer_image?.measuredWidth ?: 0 , character_shimmer_image?.measuredHeight ?: 0).toFloat().div(2))
                    .build()
            ).apply {
                fillColor = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.white))
            }
            character_movies_recycler.adapter?.takeIf { it is DayNightModeAwareAdapter }?.let {
                (it as DayNightModeAwareAdapter).onDayNightModeChanged(type)
            }
            character_movies_loading_recycler.adapter?.takeIf { it is DayNightModeAwareAdapter }?.let {
                (it as DayNightModeAwareAdapter).onDayNightModeChanged(type)
            }
        }
    }

    override fun onViewLoaded() {
        super.onViewLoaded()
        initViews()
        arguments?.let {
            val characterId = it.takeIf { it.containsKey(CHARACTER_ID_KEY) }?.getInt(CHARACTER_ID_KEY) ?: -1
            val characterType = it.takeIf { it.containsKey(CHARACTER_TYPE_KEY) }?.getInt(CHARACTER_TYPE_KEY) ?: -1
            val requestType = when(characterType) {
                CHARACTER_TYPE_DIRECTOR -> RequestType.DIRECTOR
                CHARACTER_TYPE_WRITER -> RequestType.WRITER
                else -> RequestType.ACTOR
            }
            viewModel.setRequestType(requestType,characterId)
            viewModel.fetchCharacterInfo()
        }
        subscribeToViewModel()
        subscribeToViewEvents()
    }

    private fun initViews() {
        val lm = RtlGridLayoutManager(activity, 3, RecyclerView.VERTICAL, false)
        character_movies_loading_recycler?.layoutManager = lm
        val spacing = activity?.resources?.getDimensionPixelSize(R.dimen.margin_medium) ?: 0
        character_movies_loading_recycler?.addItemDecoration(GridSpacingItemDecoration(3, spacing, true))
        val adapter = MovieAdapter(listOf(), this, 15, false)
        character_movies_loading_recycler?.adapter = adapter

        val layoutManager = RtlGridLayoutManager(activity, 3, RecyclerView.VERTICAL, false)
        character_movies_recycler?.layoutManager = layoutManager
    }

    @SuppressLint("CheckResult")
    private fun subscribeToViewModel() {
        viewModel.movies.observe(viewLifecycleOwner, Observer { list ->
            character_movies_recycler?.itemDecorationCount?.takeIf { it == 0 }?.let {
                val spacing = activity?.resources?.getDimensionPixelSize(R.dimen.margin_medium) ?: 0
                character_movies_recycler?.addItemDecoration(GridSpacingItemDecoration(3, spacing, true))
            }
            character_movies_recycler?.adapter?.takeIf { it is MoviePagedAdapter }?.let {
                (it as MoviePagedAdapter).submitList(list)
            } ?: kotlin.run {
                character_movies_recycler?.adapter = MoviePagedAdapter(this).apply {
                    submitList(list)
                }
            }
            character_refresh_layout?.isRefreshing = false
        })

        viewModel.moviesError.observe(viewLifecycleOwner, Observer {
            it.messageResId?.let {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            }
            character_refresh_layout?.isRefreshing = false
        })

        viewModel.characterInfo.observe(viewLifecycleOwner, Observer {
            it?.let { character ->
                character_shimmer_info_group?.hide()
                character_info_group?.show()
                character_name_tv?.text = character.name
                character_toolbar_name_tv?.text = character.name
                character.image?.takeIf { it.isNotEmpty() }?.let {
                    character_image_iv?.let { iv ->
                        context?.let { ctx ->
                            Glide.with(ctx).load(it).circleCrop().into(iv)
                        }
                    }
                    character_toolbar_image_iv?.let { iv ->
                        context?.let { ctx ->
                            Glide.with(ctx).load(it).circleCrop().into(iv)
                        }
                    }
                }
                character.bio?.takeIf { it.isNotEmpty() }?.let {
                    character_bio_tv?.show()
                    character_bio_tv?.text = it
                } ?: kotlin.run {
                    character_bio_tv?.hide()
                }
            }

        })

        viewModel.characterInfoError.observe(viewLifecycleOwner, Observer {
            it.messageResId?.let {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            } ?: kotlin.run {
                it.message?.takeIf { it.isNotEmpty() }?.let {
                    Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.moviesRequestState.observe(viewLifecycleOwner, Observer {
            if (it != MovieDateSource.STATE_LOADING) {
                character_movies_loading_recycler?.adapter = null
                character_movies_loading_recycler?.hide()
                character_refresh_layout?.isRefreshing = false
            }
        })

        viewModel.selectedSortName.observe(viewLifecycleOwner, Observer {
            character_movies_sort_name_tv?.setText(it)
        })
    }

    private fun subscribeToViewEvents() {
        character_back_btn?.setOnClickListener {
            activity?.onBackPressed()
        }
        character_refresh_layout?.setOnRefreshListener {
            viewModel.refresh()
        }
        character_movies_sort_layout?.setOnClickListener {
            showSortDialog()
        }
        character_appbar?.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var scrollRange = -1
            var isShow = true
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                appBarLayout?.let {
                    if (scrollRange == -1) {
                        scrollRange = it.totalScrollRange
                    }
                    if (scrollRange + verticalOffset <= 0) {
                        character_toolbar_name_tv?.show()
                        character_toolbar_image_iv?.show()
                        isShow = true
                    } else if(isShow) {
                        character_toolbar_name_tv?.hide()
                        character_toolbar_image_iv?.hide()
                        isShow = false
                    }
                }

            }

        })
    }

    private fun showSortDialog() {
        activity?.let { ctx ->

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                PopupMenu(ctx, character_toolbar, Gravity.START,R.attr.popupMenuStyle ,0)
            } else {
                PopupMenu(ctx, character_toolbar)
            }.apply {
                setOnMenuItemClickListener { item ->
                    viewModel.updateSort(item?.itemId)
                    true
                }
                inflate(R.menu.menu_sort)
                menu.findItem(viewModel.getSelectedSortMenuItemId()).isChecked = true
                show()
            }
        }
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

}