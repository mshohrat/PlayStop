package com.ms.playstop.ui.movie.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.ms.playstop.R
import com.ms.playstop.extension.hide
import com.ms.playstop.extension.show
import com.ms.playstop.model.Season
import com.ms.playstop.utils.DayNightModeAwareAdapter
import com.ms.playstop.utils.DayNightModeAwareViewHolder
import kotlinx.android.synthetic.main.item_season_layout.view.*

class SeasonAdapter(seasons: List<Season>,private val onItemClickListener: EpisodeAdapter.OnItemClickListener)
    : RecyclerView.Adapter<SeasonAdapter.ViewHolder>(), DayNightModeAwareAdapter {

    private var recyclerView: RecyclerView? = null
    private var boundViewHolders = mutableListOf<ViewHolder>()
    private var items = mutableListOf<Pair<Season,Boolean>>()

    init {
        items = seasons.map { it to false }.toMutableList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_season_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(position,item)
        boundViewHolders.takeIf { it.contains(holder).not() }?.add(holder)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        boundViewHolders.takeIf { it.contains(holder) }?.remove(holder)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), DayNightModeAwareViewHolder {

        val rootView = itemView as ViewGroup
        val nameTv = itemView.season_name_tv
        val episodesRecycler = itemView.season_episodes_recycler

        fun bind(position: Int,item: Pair<Season,Boolean>) {
            nameTv?.text = item.first.name
            item.first.episodes?.let {
                val layoutManager = LinearLayoutManager(itemView.context,RecyclerView.VERTICAL,false)
                val adapter = EpisodeAdapter(it,onItemClickListener,item.first.name)
                episodesRecycler?.layoutManager = layoutManager
                episodesRecycler?.adapter = adapter
            }
            if(item.second) {
                TransitionManager.beginDelayedTransition(rootView,Fade())
                episodesRecycler.show()
            } else {
                TransitionManager.beginDelayedTransition(rootView,Fade())
                episodesRecycler.hide()
            }
            nameTv?.setOnClickListener {
                if(item.second.not()) {
                    items[position] = item.first to true
                } else {
                    items[position] = item.first to false
                }
                notifyDataSetChanged()
            }
        }

        override fun onDayNightModeChanged(type: Int) {
            rootView.context?.let { ctx ->
                nameTv?.setTextColor(ContextCompat.getColor(ctx,R.color.colorPrimary))
                nameTv?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.colorAccent))
                episodesRecycler?.adapter?.takeIf { it is DayNightModeAwareAdapter }?.let {
                    (it as DayNightModeAwareAdapter).onDayNightModeChanged(type)
                }
            }
        }
    }

    override fun onDayNightModeChanged(type: Int) {
        for (item in boundViewHolders) {
            item.onDayNightModeChanged(type)
        }
    }
}