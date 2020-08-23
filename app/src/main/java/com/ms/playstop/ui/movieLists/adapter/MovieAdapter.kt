package com.ms.playstop.ui.movieLists.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ms.playstop.R
import com.ms.playstop.model.Movie
import com.ms.playstop.utils.RoundedCornersTransformation
import kotlinx.android.synthetic.main.item_movie_layout.view.*

class MovieAdapter(
    private val movies: List<Movie?>,
    private val onItemClickListener: OnItemClickListener
): RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    val TYPE_PLACE_HOLDER = 1
    val TYPE_NORMAL = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if(viewType == TYPE_PLACE_HOLDER) {
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie_place_holder_layout,parent,false))
        } else {
            MovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie_layout,parent,false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (movies.isEmpty()) {
            TYPE_PLACE_HOLDER
        } else {
            TYPE_NORMAL
        }
    }

    override fun getItemCount(): Int {
        return if(movies.isEmpty()) {
            4
        } else {
            movies.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(movies.size > position) {
            val movie = movies[position]
            holder.bind(movie)
        }
    }

    open class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        open fun bind (item: Movie?) {}
    }

    inner class MovieViewHolder(itemView: View): ViewHolder(itemView) {
        val rootView = itemView
        val imageIv = itemView.movie_image_iv
        val nameTv = itemView.movie_name_tv
        val genreTv = itemView.movie_genre_tv
        val scoreTv = itemView.movie_score_tv
        val freeTv = itemView.movie_free_tv

        override fun bind(item: Movie?) {
            imageIv?.let {
                Glide.with(it).load(item?.image).apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(16,0))).into(it)
            }
            nameTv?.text = item?.name
            item?.genres?.takeIf { it.isNotEmpty() }?.let {
                genreTv?.text = it.first().name
            }
            scoreTv?.text = item?.score?.toString()
            if(item?.price == 0f) {
                freeTv?.visibility = View.VISIBLE
            } else {
                freeTv?.visibility = View.GONE
            }
            rootView.setOnClickListener {
                onItemClickListener.onItemClick(item)
            }
            rootView.animate().alpha(1f).setDuration(250).start()
        }
    }

    interface OnItemClickListener {
        fun onItemClick(movie: Movie?)
    }
}