package com.ms.playstop.ui.character.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ms.playstop.R
import com.ms.playstop.extension.widthOfDevice
import com.ms.playstop.model.Character
import com.ms.playstop.utils.DayNightModeAwareAdapter
import com.ms.playstop.utils.DayNightModeAwareViewHolder
import kotlinx.android.synthetic.main.item_character_layout.view.*

class CharacterAdapter(val characters: List<Character>,val type: Int,val onItemClickListener: OnItemClickListener? = null)
    : RecyclerView.Adapter<CharacterAdapter.ViewHolder>(), DayNightModeAwareAdapter {

    private var recyclerView: RecyclerView? = null
    private var boundViewHolders = mutableListOf<ViewHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_character_layout,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = characters[position]
        holder.imageIv?.let { iv ->
            iv.context?.let { ctx ->
                Glide.with(ctx).load(character.image)
                    .placeholder(AppCompatResources.getDrawable(ctx,R.drawable.ic_person))
                    .error(AppCompatResources.getDrawable(ctx,R.drawable.ic_person))
                    .circleCrop()
                    .into(iv)
            }
        }
        holder.nameTv?.text = character.name
        val params = holder.rootView.layoutParams as? RecyclerView.LayoutParams
        val margins = (holder.rootView.context?.resources?.getDimensionPixelSize(R.dimen.margin_medium) ?: 0 ) * 4
        val outerMargins = (holder.rootView.context?.resources?.getDimensionPixelSize(R.dimen.padding_standard) ?: 0 ) * 2
        val width = (widthOfDevice() - outerMargins - margins) / 3
        params?.width = width
        holder.rootView.layoutParams = params
        holder.rootView.setOnClickListener {
            onItemClickListener?.onItemClick(character,type,position)
        }
        boundViewHolders.takeIf { it.contains(holder).not() }?.add(holder)
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

    override fun getItemCount(): Int {
        return characters.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), DayNightModeAwareViewHolder {
        val rootView = itemView
        val imageIv = itemView.character_avatar_iv
        val nameTv = itemView.character_name_tv
        override fun onDayNightModeChanged(type: Int) {
            nameTv?.context?.let { ctx ->
                nameTv.setTextColor(ContextCompat.getColor(ctx,R.color.white))
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(character: Character, type: Int,position: Int)
    }

    override fun onDayNightModeChanged(type: Int) {
        for (item in boundViewHolders) {
            item.onDayNightModeChanged(type)
        }
    }
}