package com.ms.playstop.ui.movieLists.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ms.playstop.R
import com.ms.playstop.extension.hide
import com.ms.playstop.extension.show
import com.ms.playstop.extension.widthOfDevice
import com.ms.playstop.model.Movie
import com.ms.playstop.utils.RoundedCornersTransformation
import kotlinx.android.synthetic.main.item_movie_layout.view.*

class MovieAdapter(
    private val movies: List<Movie?>,
    private val onItemClickListener: OnItemClickListener? = null,
    private val initialCount: Int = 4,
    private val handleMargin: Boolean = true
): RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private val TYPE_PLACE_HOLDER = 1
    private val TYPE_NORMAL = 2

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
            initialCount
        } else {
            movies.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(movies.size > position) {
            val movie = movies[position]
            holder.bind(movie,position == movies.lastIndex)
        }
        else {
            holder.bind(null,position == itemCount - 1)
        }
    }

    open inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        open fun bind (item: Movie?,isLastItem: Boolean = false) {
            if(handleMargin && isLastItem.not()) {
                val params = itemView.layoutParams as? ViewGroup.MarginLayoutParams
                params?.marginStart = itemView.resources?.getDimensionPixelSize(R.dimen.margin_standard) ?: 0
                itemView.layoutParams = params
            }
        }
    }

    inner class MovieViewHolder(itemView: View): ViewHolder(itemView) {
        val rootView = itemView
        val imageIv = itemView.movie_image_iv
        val nameTv = itemView.movie_name_tv
        val genreTv = itemView.movie_genre_tv
        val scoreTv = itemView.movie_score_tv
        val freeTv = itemView.movie_free_tv

        override fun bind(item: Movie?,isLastItem: Boolean) {
            super.bind(item, isLastItem)
            imageIv?.let {
                Glide.with(it).load(item?.image).apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(16,0))).into(it)
            }
            nameTv?.text = item?.name
            item?.genres?.takeIf { it.isNotEmpty() }?.let {
                genreTv?.text = it.first().name
            }
            item?.score?.toString()?.takeIf { it.isNotEmpty() && it != "0" }?.let {
                itemView.context?.getString(R.string.imdb_score_x)?.let { string ->
                    scoreTv?.text = String.format(string,it)
                }
            } ?: kotlin.run {
                scoreTv?.text = itemView.context?.getString(R.string.imdb_empty)
            }
            if(item?.price == 0f && item.isSeries.not()) {
                freeTv?.show()
            } else {
                freeTv?.hide()
            }
            rootView.setOnClickListener {
                onItemClickListener?.onItemClick(item)
            }
            if(handleMargin.not()) {
                val params = imageIv?.layoutParams as? ConstraintLayout.LayoutParams
                val margins = (rootView.context?.resources?.getDimensionPixelSize(R.dimen.margin_medium) ?: 0 ) * 4
                val width = (widthOfDevice() - margins) / 3
                val height = (width * 4) / 3
                params?.width = width
                params?.height = height
                imageIv?.layoutParams = params
            }
            rootView.animate().alpha(1f).setDuration(250).start()
        }
    }

    interface OnItemClickListener {
        fun onItemClick(movie: Movie?)
    }
}