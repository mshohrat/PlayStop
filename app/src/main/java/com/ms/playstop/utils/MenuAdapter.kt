package com.ms.playstop.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.ms.playstop.R
import kotlinx.android.synthetic.main.item_menu_layout.view.*

class MenuAdapter(private val items: List<Pair<Int,Int>>,
                  private val itemClickListener: MenuAdapter.OnItemClickListener): RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    companion object {
        const val ITEM_TYPE_PLAY = 1
        const val ITEM_TYPE_DOWNLOAD = 2
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_menu_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(items.size > position) {
            val item = items[position]
            holder.bind(item)
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rootView = itemView
        val imageIv = itemView.menu_item_image_iv
        val nameTv = itemView.menu_item_name_tv
        fun bind(pair: Pair<Int,Int>) {
            rootView.context?.let { ctx ->
                when(pair.first) {
                    ITEM_TYPE_PLAY -> {
                        imageIv?.setImageDrawable(AppCompatResources.getDrawable(ctx,R.drawable.ic_play))
                    }
                    ITEM_TYPE_DOWNLOAD -> {
                        imageIv?.setImageDrawable(AppCompatResources.getDrawable(ctx,R.drawable.ic_download))
                    }
                    else -> {}
                }
            }
            nameTv?.setText(pair.second)
            rootView.setOnClickListener {
                itemClickListener.onItemClick(items.indexOf(pair),pair.first)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int,itemType: Int)
    }
}