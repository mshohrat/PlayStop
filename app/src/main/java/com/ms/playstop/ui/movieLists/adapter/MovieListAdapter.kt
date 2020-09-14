package com.ms.playstop.ui.movieLists.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ms.playstop.R
import com.ms.playstop.model.Movie
import com.ms.playstop.model.Suggestion
import com.ms.playstop.model.SuggestionMovies
import com.ms.playstop.utils.RtlLinearLayoutManager
import kotlinx.android.synthetic.main.item_movie_list_layout.view.*

class MovieListAdapter(
    private var movies: List<SuggestionMovies>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie_list_layout,parent,false))
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
                override fun onItemClick(movie: Movie?) {
                    onItemClickListener.onMovieClick(movie)
                }
            })
            val layoutManager = RtlLinearLayoutManager(rootView.context,RecyclerView.HORIZONTAL,false)
            recycler?.layoutManager = layoutManager
            recycler?.adapter = movieAdapter
        }
    }

    interface OnItemClickListener {
        fun onShowAllClick(suggestion: Suggestion)
        fun onMovieClick(movie: Movie?)
    }

}