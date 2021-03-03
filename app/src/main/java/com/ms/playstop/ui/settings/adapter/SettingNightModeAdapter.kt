package com.ms.playstop.ui.settings.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ms.playstop.R
import com.ms.playstop.extension.hide
import com.ms.playstop.extension.show
import kotlinx.android.synthetic.main.item_setting_night_mode_layout.view.*

class SettingNightModeAdapter(private val onItemClickListener: OnItemClickListener? = null) : RecyclerView.Adapter<SettingNightModeAdapter.ViewHolder>() {

    private var selectedPosition = 0

    companion object {
        const val TYPE_DAY_MODE = 101
        const val TYPE_NIGHT_MODE = 102
        const val TYPE_DAY_NIGHT_MODE_AUTO = 103
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_setting_night_mode_layout,parent,false))
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            1 -> TYPE_NIGHT_MODE
            2 -> TYPE_DAY_NIGHT_MODE_AUTO
            else -> TYPE_DAY_MODE
        }
    }

    fun setSelectedType(selectedType: Int = TYPE_DAY_MODE) {
        selectedPosition = when(selectedType) {
            TYPE_NIGHT_MODE -> 1
            TYPE_DAY_NIGHT_MODE_AUTO -> 2
            else -> 0
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(getItemViewType(position)) {
            TYPE_NIGHT_MODE -> {
                holder.titleTv?.setText(R.string.on)
                holder.descTv?.hide()
                holder.rootView.setOnClickListener {
                    selectedPosition = position
                    notifyDataSetChanged()
                    onItemClickListener?.onItemClick(position, TYPE_NIGHT_MODE)
                }
            }
            TYPE_DAY_NIGHT_MODE_AUTO -> {
                holder.titleTv?.setText(R.string.auto)
                holder.descTv?.setText(R.string.day_night_mode_auto_desc)
                holder.descTv?.show()
                holder.rootView.setOnClickListener {
                    selectedPosition = position
                    notifyDataSetChanged()
                    onItemClickListener?.onItemClick(position, TYPE_DAY_NIGHT_MODE_AUTO)
                }
            }
            else -> {
                holder.titleTv?.setText(R.string.off)
                holder.descTv?.hide()
                holder.rootView.setOnClickListener {
                    selectedPosition = position
                    notifyDataSetChanged()
                    onItemClickListener?.onItemClick(position, TYPE_DAY_MODE)
                }
            }
        }
        holder.radio?.isChecked = selectedPosition == position
    }

    override fun getItemCount(): Int {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) 3 else 2
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rootView = itemView
        val titleTv = itemView.setting_night_mode_title_tv
        val descTv = itemView.setting_night_mode_desc_tv
        val radio = itemView.setting_night_mode_radio
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, type: Int)
    }
}