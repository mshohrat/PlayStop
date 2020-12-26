package com.ms.playstop.ui.movieLists.adapter

import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ms.playstop.R
import com.ms.playstop.model.Movie
import com.ms.playstop.model.Suggestion
import com.ms.playstop.model.SuggestionMovies
import com.ms.playstop.utils.GestureListener
import com.ms.playstop.utils.RtlLinearLayoutManager
import kotlinx.android.synthetic.main.item_movie_list_layout.view.*

class MovieListAdapter(
    private var movies: List<SuggestionMovies>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<MovieListAdapter.ViewHolder>(),RecyclerView.OnItemTouchListener,
    GestureListener.OnScrollOrientationListener {

    private var recyclerView: RecyclerView? = null
    private var gestureDetector: GestureDetector? = null

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
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = movies[position]
        holder.bind(item)
    }

    fun updateData(list: List<SuggestionMovies>) {
        this.movies = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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
                override fun onItemClick(movie: Movie?, transitionElement: View?) {
                    onItemClickListener.onMovieClick(movie,transitionElement)
                }
            })
            val layoutManager = RtlLinearLayoutManager(rootView.context,RecyclerView.HORIZONTAL,false)
            recycler?.layoutManager = layoutManager
            recycler?.adapter = movieAdapter
            recycler?.addOnItemTouchListener(this@MovieListAdapter)
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
        fun onMovieClick(movie: Movie?,transitionElement : View? = null)
    }

}