package com.ms.playstop.ui.movie.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.ms.playstop.R
import com.ms.playstop.extension.hide
import com.ms.playstop.extension.show
import com.ms.playstop.model.Season
import kotlinx.android.synthetic.main.item_season_layout.view.*

class SeasonAdapter(private val seasons: List<Season>,private val onItemClickListener: EpisodeAdapter.OnItemClickListener): RecyclerView.Adapter<SeasonAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_season_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return seasons.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val season = seasons[position]
        holder.bind(season)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val rootView = itemView as ViewGroup
        val nameTv = itemView.season_name_tv
        val episodesRecycler = itemView.season_episodes_recycler

        fun bind(season: Season) {
            nameTv?.text = season.name
            season.episodes?.let {
                val layoutManager = LinearLayoutManager(itemView.context,RecyclerView.VERTICAL,false)
                val adapter = EpisodeAdapter(it,onItemClickListener,season.name)
                episodesRecycler?.layoutManager = layoutManager
                episodesRecycler?.adapter = adapter
                episodesRecycler?.hide()
            }
            nameTv?.setOnClickListener {
                if(episodesRecycler?.visibility == View.GONE) {
                    TransitionManager.beginDelayedTransition(rootView,Fade())
                    episodesRecycler.show()
                } else {
                    TransitionManager.beginDelayedTransition(rootView,Fade())
                    episodesRecycler.hide()
                }
            }
        }
    }
}