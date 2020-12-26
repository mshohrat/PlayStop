package com.ms.playstop.ui.movieLists.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ms.playstop.R
import com.ms.playstop.extension.hide
import com.ms.playstop.extension.show
import com.ms.playstop.extension.widthOfDevice
import com.ms.playstop.model.Movie
import com.ms.playstop.utils.RoundedCornersTransformation
import kotlinx.android.synthetic.main.item_movie_header_layout.view.*

class MovieHeaderAdapter(
    private val movies: List<Movie>,
    private val onItemClickListener: OnItemClickListener? = null
): RecyclerView.Adapter<MovieHeaderAdapter.ViewHolder>() {

    private val TYPE_PLACE_HOLDER = 1
    private val TYPE_NORMAL = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if(viewType == TYPE_PLACE_HOLDER) {
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie_header_place_holder_layout,parent,false))
        } else {
            MovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie_header_layout,parent,false))
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
            1
        } else {
            movies.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(movies.size > position) {
            val movie = movies[position]
            holder.bind(movie)
        }
        else {
            holder.bind(null)
        }
    }

    open inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        open fun bind (item: Movie?) {
            val width = widthOfDevice()
            val height = width.times(3).div(5)
            val params = itemView.layoutParams as? RecyclerView.LayoutParams
            params?.width = width
            params?.height = height
            params?.let {
                itemView.layoutParams = it
            }
        }
    }

    inner class MovieViewHolder(itemView: View): ViewHolder(itemView) {
        val rootView = itemView
        val imageIv = itemView.movie_header_image_iv
        val nameTv = itemView.movie_header_name_tv
        val scoreTv = itemView.movie_header_score_tv
        val freeTv = itemView.movie_header_free_tv

        override fun bind(item: Movie?) {
            super.bind(item)
            imageIv?.let {
                Glide.with(it).load(item?.header).apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(16,0))).into(it)
            }
            nameTv?.text = item?.name
            item?.score?.toString()?.takeIf { it.isNotEmpty() && it != "0" }?.let {
                itemView.context?.getString(R.string.imdb_score_x)?.let { string ->
                    scoreTv?.text = String.format(string,it)
                }
            } ?: kotlin.run {
                //scoreTv?.text = itemView.context?.getString(R.string.imdb_empty)
                scoreTv?.hide()
            }
//            if(item?.price == 0f && item.isSeries.not()) {
//                freeTv?.show()
//            } else {
//                freeTv?.hide()
//            }
            rootView.setOnClickListener {
                onItemClickListener?.onMovieClick(item)
            }
            rootView.animate().alpha(1f).setDuration(250).start()
        }
    }

    interface OnItemClickListener {
        fun onMovieClick(movie: Movie?)
    }
}