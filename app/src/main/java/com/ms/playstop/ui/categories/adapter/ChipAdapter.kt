package com.ms.playstop.ui.categories.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.ms.playstop.R
import com.ms.playstop.model.Category
import com.ms.playstop.model.Genre
import com.ms.playstop.model.Year

class ChipAdapter<in T>(
    private val items: List<T>,
    private val onItemClickListener: OnItemClickListener<T>? = null
) : RecyclerView.Adapter<ChipAdapter<T>.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chip_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val chipBtn = itemView as? MaterialButton

        fun bind(item: T) {
            when(item) {
                is Category -> {
                    chipBtn?.text = (item as? Category)?.name
                    chipBtn?.setOnClickListener {
                        onItemClickListener?.onItemClick(item)
                    }
                }
                is Genre -> {
                    chipBtn?.text = (item as? Genre)?.name
                    chipBtn?.setOnClickListener {
                        onItemClickListener?.onItemClick(item)
                    }
                }
                is Year -> {
                    chipBtn?.text = (item as? Year)?.value.toString()
                    chipBtn?.setOnClickListener {
                        onItemClickListener?.onItemClick(item)
                    }
                }
            }
        }

    }

    interface OnItemClickListener<in T> {
        fun onItemClick(item: T)
    }
}