package com.ms.playstop.ui.movie.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.ms.playstop.R
import com.ms.playstop.extension.convertDpToPixel
import com.ms.playstop.model.Episode
import com.ms.playstop.utils.DayNightModeAwareAdapter
import com.ms.playstop.utils.DayNightModeAwareViewHolder
import kotlinx.android.synthetic.main.item_episode_layout.view.*

class EpisodeAdapter(private val episodes: List<Episode>,private val onItemClickListener: OnItemClickListener? = null,private val seasonName: String? = null)
    : RecyclerView.Adapter<EpisodeAdapter.ViewHolder>(), DayNightModeAwareAdapter {

    private var recyclerView: RecyclerView? = null
    private var boundViewHolders = mutableListOf<ViewHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_episode_layout,parent,false))
    }

    override fun getItemCount(): Int {
         return episodes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val episode = episodes[position]
        holder.bind(episode)
        boundViewHolders.takeIf { it.contains(holder).not() }?.add(holder)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        boundViewHolders.takeIf { it.contains(holder) }?.remove(holder)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
        this.boundViewHolders.clear()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), DayNightModeAwareViewHolder {
        val rootView = itemView
        val nameTv = itemView.episode_name_tv
        fun bind(episode: Episode) {
            nameTv?.text = episode.name
            rootView.setOnClickListener {
                onItemClickListener?.onItemClick(episode,seasonName)
            }
        }

        override fun onDayNightModeChanged(type: Int) {
            rootView.context?.let { ctx ->
                rootView.background = MaterialShapeDrawable(
                    ShapeAppearanceModel.builder()
                        .setAllCornerSizes(ctx.resources.getDimensionPixelSize(R.dimen.shimmer_radius).toFloat())
                        .build()
                ).apply {
                    fillColor = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.colorPrimaryDark))
                }
                nameTv?.background = MaterialShapeDrawable(
                    ShapeAppearanceModel.builder()
                        .setAllCornerSizes(ctx.resources.getDimensionPixelSize(R.dimen.shimmer_radius).toFloat())
                        .build()
                ).apply {
                    strokeColor = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.white))
                    strokeWidth = convertDpToPixel(1f).toFloat()
                }
                nameTv?.setTextColor(ContextCompat.getColor(ctx,R.color.white))
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(episode: Episode,seasonName: String? = null)
    }

    override fun onDayNightModeChanged(type: Int) {
        for (item in boundViewHolders) {
            item.onDayNightModeChanged(type)
        }
    }
}