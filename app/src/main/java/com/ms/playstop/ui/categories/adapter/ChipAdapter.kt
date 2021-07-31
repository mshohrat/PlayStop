package com.ms.playstop.ui.categories.adapter

import android.content.res.ColorStateList
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.ms.playstop.R
import com.ms.playstop.extension.getResourceFromThemeAttribute
import com.ms.playstop.model.Category
import com.ms.playstop.model.Genre
import com.ms.playstop.model.Year
import com.ms.playstop.utils.DayNightModeAwareAdapter
import com.ms.playstop.utils.DayNightModeAwareViewHolder

class ChipAdapter<in T>(
    private val items: List<T>,
    private val onItemClickListener: OnItemClickListener<T>? = null
) : RecyclerView.Adapter<ChipAdapter<T>.ViewHolder>(),DayNightModeAwareAdapter {

    private var recyclerView: RecyclerView? = null
    private val boundViewHolders = mutableListOf<ViewHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chip_layout,parent,false))
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

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        boundViewHolders.takeIf { it.contains(holder) }?.remove(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        boundViewHolders.takeIf { it.contains(holder).not() }?.add(holder)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), DayNightModeAwareViewHolder {

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

        override fun onDayNightModeChanged(type: Int) {
            chipBtn?.context?.let { ctx ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    chipBtn.setTextAppearance(ctx.getResourceFromThemeAttribute(R.attr.textAppearanceBody1,R.style.Body1_FixSize))
                } else {
                    chipBtn.setTextAppearance(ctx,ctx.getResourceFromThemeAttribute(R.attr.textAppearanceBody1,R.style.Body1_FixSize))
                }
                chipBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.grayLight))
            }
        }

    }

    interface OnItemClickListener<in T> {
        fun onItemClick(item: T)
    }

    override fun onDayNightModeChanged(type: Int) {
        for (item in boundViewHolders) {
            item.onDayNightModeChanged(type)
        }
    }
}