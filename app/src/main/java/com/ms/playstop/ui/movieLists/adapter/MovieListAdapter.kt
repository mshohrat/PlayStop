package com.ms.playstop.ui.movieLists.adapter

import android.content.res.ColorStateList
import android.os.Build
import android.view.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.microsoft.appcenter.crashes.Crashes
import com.ms.playstop.R
import com.ms.playstop.extension.getResourceFromThemeAttribute
import com.ms.playstop.model.*
import com.ms.playstop.utils.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_movie_lists.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.item_movie_header_list_layout.view.*
import kotlinx.android.synthetic.main.item_movie_list_layout.view.*
import java.util.concurrent.TimeUnit

class MovieListAdapter(
    movieList: List<MovieListItem>,
    private var movieHeaderList: List<Movie>? = listOf(),
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<MovieListAdapter.ViewHolder>(),RecyclerView.OnItemTouchListener, DayNightModeAwareAdapter,
    GestureListener.OnScrollOrientationListener {

    val TYPE_MOVIE_HEADER = 100
    val TYPE_MOVIE_LIST = 101

    private var recyclerView: RecyclerView? = null
    private var gestureDetector: GestureDetector? = null
    private val boundViewHolders = mutableListOf<ViewHolder>()
    private var movies = arrayListOf<MovieListItem>()
    private var topRecyclerItemDecoration = LinePagerIndicatorDecoration()
    private var isTopMovieListLoaded = false
    private val disposables = CompositeDisposable()
    private var lastHeaderAutoScrollPosition = 0

    init {
        this.movies.addAll(movieList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        gestureDetector = GestureDetector(parent.context,GestureListener(this))
        return if(viewType == TYPE_MOVIE_LIST) {
            MovieListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie_list_layout,parent,false))
        } else {
            val holder = HeaderListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie_header_list_layout,parent,false))
            PagerSnapHelper().attachToRecyclerView(holder.recycler)
            holder.recycler?.addItemDecoration(topRecyclerItemDecoration)
            holder
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0 && movieHeaderList != null) {
            TYPE_MOVIE_HEADER
        } else {
            TYPE_MOVIE_LIST
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
        this.gestureDetector = null
        this.boundViewHolders.clear()
    }

    override fun getItemCount(): Int {
        return (if(movieHeaderList == null) 0 else 1) + movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(holder) {
            is MovieListViewHolder -> {
                val item = movies[if(movieHeaderList == null) position else position-1]
                holder.bind(item)
            }
            is HeaderListViewHolder -> {
                val item = movieHeaderList
                holder.bind(item)
            }
        }
        boundViewHolders.takeIf { it.contains(holder).not() }?.add(holder)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        if(holder is HeaderListViewHolder) {
            lastHeaderAutoScrollPosition = holder.currentPosition()
        }
        boundViewHolders.takeIf { it.contains(holder) }?.remove(holder)
    }

    fun updateHeaderData(list: ArrayList<Movie>) {
        this.movieHeaderList = list
        notifyDataSetChanged()
    }

    fun updateData(list: List<MovieListItem>) {
        this.movies = ArrayList(list)
        notifyDataSetChanged()
    }

    fun addData(list: List<MovieListItem>) {
        val startPosition = (if(movieHeaderList == null) 0 else 1)+ this.movies.size
        this.movies.addAll(list)
        notifyItemRangeInserted(startPosition,list.size)
    }

    abstract inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), DayNightModeAwareViewHolder {
        val rootView = itemView
    }

    inner class HeaderListViewHolder(itemView: View) : ViewHolder(itemView) {
        val recycler = itemView.movie_header_list_recycler

        fun bind(item : List<Movie>?) {
            item?.let {
                if(item.isNotEmpty()) {
                    isTopMovieListLoaded = true
                }
                recycler?.layoutManager = object : RtlLinearLayoutManager(itemView.context,RecyclerView.HORIZONTAL,false) {
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }
                recycler?.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {

                    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                        when(e.action) {
                            MotionEvent.ACTION_DOWN -> cancelAutoScrollOfHeaderRecycler()
                            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                                if(isTopMovieListLoaded){
                                    initAutoScrollForHeaderRecycler(recycler)
                                }
                            }
                            else -> {}
                        }
                        return false
                    }

                    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

                    }

                    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

                    }

                })
                val adapter = MovieHeaderAdapter(item,object : MovieHeaderAdapter.OnItemClickListener {
                    override fun onMovieClick(movie: Movie?) {
                        onItemClickListener.onMovieClick(movie)
                    }
                })
                recycler?.adapter = adapter
                scrollToPosition(lastHeaderAutoScrollPosition,recycler,false)
                initAutoScrollForHeaderRecycler(recycler)
            }
        }
        override fun onDayNightModeChanged(type: Int) {
            recycler?.takeIf { it.itemDecorationCount > 0 }?.removeItemDecoration(topRecyclerItemDecoration)
            topRecyclerItemDecoration = LinePagerIndicatorDecoration()
            recycler?.addItemDecoration(topRecyclerItemDecoration)
            recycler?.adapter?.takeIf { it is DayNightModeAwareAdapter }?.let {
                (it as DayNightModeAwareAdapter).onDayNightModeChanged(type)
            }
        }

        fun currentPosition() : Int {
            return recycler?.layoutManager?.takeIf { it is LinearLayoutManager }?.let {
                (it as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            } ?: 0
        }
    }

    inner class MovieListViewHolder(itemView: View) : ViewHolder(itemView) {
        val titleTv = itemView.movie_list_title_tv
        val showAllBtn = itemView.movie_list_show_all_btn
        val recycler = itemView.movie_list_item_recycler

        fun bind(item : MovieListItem) {
            when (item) {
                is SuggestionMovies -> {
                    titleTv?.text = item.suggestion.name
                    showAllBtn?.setOnClickListener {
                        onItemClickListener.onShowAllClick(item)
                    }
                    val movieAdapter = MovieAdapter(item.movies,object : MovieAdapter.OnItemClickListener{
                        override fun onItemClick(movie: Movie?) {
                            onItemClickListener.onMovieClick(movie)
                        }
                    })
                    val layoutManager = RtlLinearLayoutManager(rootView.context,RecyclerView.HORIZONTAL,false)
                    recycler?.layoutManager = layoutManager
                    recycler?.adapter = movieAdapter
                    recycler?.addOnItemTouchListener(this@MovieListAdapter)
                }
                is GenresSuggestionMovies -> {
                    titleTv?.text = item.genresSuggestion.name
                    showAllBtn?.setOnClickListener {
                        onItemClickListener.onShowAllClick(item)
                    }
                    val movieAdapter = MovieAdapter(item.movies,object : MovieAdapter.OnItemClickListener{
                        override fun onItemClick(movie: Movie?) {
                            onItemClickListener.onMovieClick(movie)
                        }
                    })
                    val layoutManager = RtlLinearLayoutManager(rootView.context,RecyclerView.HORIZONTAL,false)
                    recycler?.layoutManager = layoutManager
                    recycler?.adapter = movieAdapter
                    recycler?.addOnItemTouchListener(this@MovieListAdapter)
                }
                else -> {}
            }

        }

        override fun onDayNightModeChanged(type: Int) {
            rootView.context?.let { ctx ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    titleTv?.setTextAppearance(ctx.getResourceFromThemeAttribute(R.attr.textAppearanceHeadline3,R.style.Headline3_FixSize))
                } else {
                    titleTv?.setTextAppearance(ctx,ctx.getResourceFromThemeAttribute(R.attr.textAppearanceHeadline3,R.style.Headline3_FixSize))
                }
                with(ContextCompat.getColor(ctx,R.color.colorAccentDark)){
                    showAllBtn?.setTextColor(this)
                    showAllBtn?.iconTint = ColorStateList.valueOf(this)
                }
                recycler?.adapter?.takeIf { it is DayNightModeAwareAdapter }?.let {
                    (it as DayNightModeAwareAdapter).onDayNightModeChanged(type)
                }
            }
        }
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        gestureDetector?.onTouchEvent(e)
        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    }

    override fun onVerticalScroll() {
        recyclerView?.requestDisallowInterceptTouchEvent(false)
    }

    override fun onHorizontalScroll() {
        recyclerView?.requestDisallowInterceptTouchEvent(true)
    }

    interface OnItemClickListener {
        fun onShowAllClick(movieListItem: MovieListItem)
        fun onMovieClick(movie: Movie?)
    }

    override fun onDayNightModeChanged(type: Int) {
        for (item in boundViewHolders) {
            item.onDayNightModeChanged(type)
        }
    }

    private fun initAutoScrollForHeaderRecycler(recyclerView: RecyclerView?) {
        cancelAutoScrollOfHeaderRecycler()
        disposables.add(
            Observable.interval(5L, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                recyclerView?.layoutManager?.takeIf { it is LinearLayoutManager }?.let {
                    val pos = (it as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                    if(pos == recyclerView.adapter?.itemCount?.minus(1) ?: 0) {
                        recyclerView.scrollToPosition(0)
                    } else {
                        scrollToPosition(pos + 1, recyclerView)
                    }
                }
            },{

            }))
    }

    private fun cancelAutoScrollOfHeaderRecycler() {
        disposables.clear()
    }

    private fun scrollToPosition(position: Int,recyclerView: RecyclerView?, withAnimation: Boolean = true) {
        try {
            if(withAnimation) {
                recyclerView?.smoothScrollToPosition(position)
            } else {
                recyclerView?.scrollToPosition(position)
            }
        } catch (e: Exception) {
            Crashes.trackError(e)
        }
    }

}