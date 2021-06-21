package com.ms.playstop.ui.character.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ms.playstop.R
import com.ms.playstop.extension.widthOfDevice
import com.ms.playstop.model.Character
import kotlinx.android.synthetic.main.item_character_layout.view.*

class CharacterAdapter(val characters: List<Character>,val type: Int,val onItemClickListener: OnItemClickListener? = null): RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {

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
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rootView = itemView
        val imageIv = itemView.character_avatar_iv
        val nameTv = itemView.character_name_tv
    }

    interface OnItemClickListener {
        fun onItemClick(character: Character, type: Int,position: Int)
    }
}