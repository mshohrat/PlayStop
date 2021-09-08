package com.ms.playstop.ui.playVideo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ms.playstop.R
import com.ms.playstop.extension.hide
import kotlinx.android.synthetic.main.item_link_radio_layout.view.*
import kotlinx.android.synthetic.main.item_link_radio_layout.view.link_name_tv

class RadioLinkAdapter(private val type: Int, urls: List<String>, selectedPosition: Int = 0, private val onItemClickListener: OnItemClickListener? = null) : RecyclerView.Adapter<RadioLinkAdapter.ViewHolder>() {

    companion object {
        const val TYPE_SUBTITLE = 101
        const val TYPE_AUDIO = 102
    }

    private val items =  mutableListOf<Pair<Boolean,String>>()
    private val DISABLED = "غیر فعال"
    init {
        when(type) {
            TYPE_SUBTITLE -> {
                items.add(0,false to DISABLED)
                for (i in urls.indices) {
                    items.add(i+1,false to urls[i])
                }
                selectPosition(selectedPosition + 1)
            }
            TYPE_AUDIO -> {
                for (i in urls.indices) {
                    items.add(i,false to urls[i])
                }
                selectPosition(selectedPosition)
            }
        }
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
        val icon = itemView.link_icon_iv
        fun bind(position: Int,item: Pair<Boolean, String>) {
            icon?.hide()
            when(type) {
                TYPE_SUBTITLE -> {
                    if(position == 0) {
                        nameTv?.text = item.second
                    } else {
                        rootView.context?.getString(R.string.subtitle_x)?.let {
                            nameTv?.text = String.format(it,position)
                        }
                    }
                }
                TYPE_AUDIO -> {
                    nameTv?.text = item.second
                }
            }
            radio?.isChecked = item.first
            rootView.setOnClickListener {
                selectPosition(position,true)
                val finalPosition = when (type) {
                    TYPE_AUDIO -> position
                    else -> position - 1
                }
                onItemClickListener?.onItemClick(
                    type,
                    finalPosition,
                    if(item.second == DISABLED) null else item.second
                )
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(type: Int,position: Int,item: String? = null)
    }
}