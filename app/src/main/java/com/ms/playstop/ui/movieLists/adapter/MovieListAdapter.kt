package com.ms.playstop.ui.movieLists.adapter

import android.content.res.ColorStateList
import android.os.Build
import android.view.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ms.playstop.R
import com.ms.playstop.extension.getResourceFromThemeAttribute
import com.ms.playstop.model.Movie
import com.ms.playstop.model.Suggestion
import com.ms.playstop.model.SuggestionMovies
import com.ms.playstop.utils.DayNightModeAwareAdapter
import com.ms.playstop.utils.DayNightModeAwareViewHolder
import com.ms.playstop.utils.GestureListener
import com.ms.playstop.utils.RtlLinearLayoutManager
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.item_movie_list_layout.view.*

class MovieListAdapter(
    private var movies: List<SuggestionMovies>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<MovieListAdapter.ViewHolder>(),RecyclerView.OnItemTouchListener, DayNightModeAwareAdapter,
    GestureListener.OnScrollOrientationListener {

    private var recyclerView: RecyclerView? = null
    private var gestureDetector: GestureDetector? = null
    private val boundViewHolders = mutableListOf<ViewHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        gestureDetector = GestureDetector(parent.context,GestureListener(this))
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie_list_layout,parent,false))
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
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = movies[position]
        holder.bind(item)
        boundViewHolders.takeIf { it.contains(holder).not() }?.add(holder)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        boundViewHolders.takeIf { it.contains(holder) }?.remove(holder)
    }

    fun updateData(list: List<SuggestionMovies>) {
        this.movies = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), DayNightModeAwareViewHolder {

        val rootView = itemView
        val titleTv = itemView.movie_list_title_tv
        val showAllBtn = itemView.movie_list_show_all_btn
        val recycler = itemView.movie_list_item_recycler

        fun bind(item : SuggestionMovies) {
            titleTv?.text = item.suggestion.name
            showAllBtn?.setOnClickListener {
                onItemClickListener.onShowAllClick(item.suggestion)
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
        fun onShowAllClick(suggestion: Suggestion)
        fun onMovieClick(movie: Movie?)
    }

    override fun onDayNightModeChanged(type: Int) {
        for (item in boundViewHolders) {
            item.onDayNightModeChanged(type)
        }
    }

}