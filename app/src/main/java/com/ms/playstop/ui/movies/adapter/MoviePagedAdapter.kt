package com.ms.playstop.ui.movies.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.ms.playstop.R
import com.ms.playstop.extension.hide
import com.ms.playstop.extension.widthOfDevice
import com.ms.playstop.model.Movie
import com.ms.playstop.utils.DayNightModeAwareAdapter
import com.ms.playstop.utils.DayNightModeAwareViewHolder
import com.ms.playstop.utils.RoundedCornersTransformation
import kotlinx.android.synthetic.main.item_movie_layout.view.*

val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {

    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }

}

class MoviePagedAdapter(private val onItemClickListener: OnItemClickListener? = null)
    : PagedListAdapter<Movie,MoviePagedAdapter.ViewHolder>(DIFF_CALLBACK),DayNightModeAwareAdapter {

    private var recyclerView: RecyclerView? = null
    private val boundViewHolders = mutableListOf<ViewHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie_layout,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
        boundViewHolders.takeIf { it.contains(holder).not() }?.add(holder)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
        this.boundViewHolders.clear()
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        boundViewHolders.takeIf { it.contains(holder) }?.remove(holder)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), DayNightModeAwareViewHolder {

        val rootView = itemView
        val imageIv = itemView.movie_image_iv
        val nameTv = itemView.movie_name_tv
        val genreTv = itemView.movie_genre_tv
        val scoreTv = itemView.movie_score_tv
        val freeTv = itemView.movie_free_tv

        fun bind(item: Movie?) {
            imageIv?.let {
                Glide.with(it).load(item?.image).apply(
                    RequestOptions.bitmapTransform(
                        RoundedCornersTransformation(16,0)
                    )).into(it)
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
                //scoreTv?.text = itemView.context?.getString(R.string.imdb_empty)
                scoreTv?.hide()
            }
//            if(item?.price == 0f && item.isSeries.not()) {
//                freeTv?.show()
//            } else {
//                freeTv?.hide()
//            }
            rootView.setOnClickListener {
                onItemClickListener?.onItemClick(item)
            }
            val params = imageIv?.layoutParams as? ConstraintLayout.LayoutParams
            val margins = (rootView.context?.resources?.getDimensionPixelSize(R.dimen.margin_medium) ?: 0 ) * 4
            val width = (widthOfDevice() - margins) / 3
            val height = (width * 4) / 3
            params?.width = width
            params?.height = height
            imageIv?.layoutParams = params
            rootView.animate().alpha(1f).setDuration(250).start()
        }

        override fun onDayNightModeChanged(type: Int) {
            rootView.context?.let { ctx ->
                with(ContextCompat.getColor(ctx,R.color.white)){
                    nameTv?.setTextColor(this)
                    genreTv?.setTextColor(this)
                    scoreTv?.setTextColor(this)
                }
                scoreTv?.background = MaterialShapeDrawable(
                    ShapeAppearanceModel.builder()
                        .setAllCornerSizes(ctx.resources.getDimensionPixelSize(R.dimen.shimmer_radius).toFloat())
                        .build()
                ).apply {
                    fillColor = ColorStateList.valueOf(ContextCompat.getColor(ctx, R.color.yellow))
                }
                freeTv?.background = MaterialShapeDrawable(
                    ShapeAppearanceModel.builder()
                        .setAllCornerSizes(ctx.resources.getDimensionPixelSize(R.dimen.badge_radius).toFloat())
                        .build()
                ).apply {
                    fillColor = ColorStateList.valueOf(ContextCompat.getColor(ctx, R.color.redLight))
                }
                freeTv?.setTextColor(ContextCompat.getColor(ctx,R.color.colorPrimaryDark))
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(movie: Movie?)
    }

    override fun onDayNightModeChanged(type: Int) {
        for (item in boundViewHolders) {
            item.onDayNightModeChanged(type)
        }
    }
}