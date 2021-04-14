package com.ms.playstop.ui.playVideo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ms.playstop.R
import kotlinx.android.synthetic.main.item_link_radio_layout.view.*

class RadioLinkAdapter(urls: List<String>, selectedPosition: Int = 0, private val onItemClickListener: OnItemClickListener? = null) : RecyclerView.Adapter<RadioLinkAdapter.ViewHolder>() {

    private val items =  mutableListOf<Pair<Boolean,String>>()
    private val DISABLED = "غیر فعال"
    init {
        items.add(0,false to DISABLED)
        for (i in urls.indices) {
            items.add(i+1,false to urls[i])
        }
        selectPosition(selectedPosition + 1)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_link_radio_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun selectPosition(position: Int,updateView: Boolean = false) {
        for (i in items.indices) {
            if(i == position) {
                items[i] = true to items[i].second
            } else {
                items[i] = false to items[i].second
            }
        }
        if(updateView) {
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(items.size > position) {
            val item = items[position]
            holder.bind(position,item)
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rootView = itemView
        val nameTv = itemView.link_name_tv
        val radio = itemView.link_radio
        fun bind(position: Int,item: Pair<Boolean, String>) {
            if(position == 0) {
                nameTv?.text = item.second
            } else {
                rootView.context?.getString(R.string.subtitle_x)?.let {
                    nameTv?.text = String.format(it,position)
                }
            }
            radio?.isChecked = item.first
            rootView.setOnClickListener {
                selectPosition(position,true)
                onItemClickListener?.onItemClick(
                    position - 1,
                    if(item.second == DISABLED) null else item.second
                )
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int,item: String? = null)
    }
}