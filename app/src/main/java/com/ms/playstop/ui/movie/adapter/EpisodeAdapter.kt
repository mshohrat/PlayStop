package com.ms.playstop.ui.movie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ms.playstop.R
import com.ms.playstop.model.Episode
import kotlinx.android.synthetic.main.item_episode_layout.view.*

class EpisodeAdapter(private val episodes: List<Episode>,private val onItemClickListener: OnItemClickListener? = null,private val seasonName: String? = null): RecyclerView.Adapter<EpisodeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_episode_layout,parent,false))
    }

    override fun getItemCount(): Int {
         return episodes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val episode = episodes[position]
        holder.bind(episode)
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rootView = itemView
        val nameTv = itemView.episode_name_tv
        fun bind(episode: Episode) {
            nameTv?.text = episode.name
            rootView.setOnClickListener {
                onItemClickListener?.onItemClick(episode,seasonName)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(episode: Episode,seasonName: String? = null)
    }
}