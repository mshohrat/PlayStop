package com.ms.playstop.ui.movie.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.ms.playstop.R
import com.ms.playstop.extension.convertDpToPixel
import com.ms.playstop.model.Comment
import com.ms.playstop.utils.DayNightModeAwareAdapter
import com.ms.playstop.utils.DayNightModeAwareViewHolder
import kotlinx.android.synthetic.main.item_comment_layout.view.*

class CommentAdapter(private val comments: List<Comment>)
    : RecyclerView.Adapter<CommentAdapter.ViewHolder>(), DayNightModeAwareAdapter {

    private var recyclerView: RecyclerView? = null
    private var boundViewHolders = mutableListOf<ViewHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_comment_layout,parent,false))
    }

    override fun getItemCount(): Int {
         return comments.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(comments.size > position) {
            val comment = comments[position]
            holder.bind(comment)
            boundViewHolders.takeIf { it.contains(holder).not() }?.add(holder)
        }
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

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), DayNightModeAwareViewHolder {
        val rootView = itemView
        val userNameTv = itemView.comment_user_name_tv
        val textTv = itemView.comment_text_tv
        fun bind(comment: Comment) {
            userNameTv?.text = comment.user.name
            textTv?.text = comment.text
        }

        override fun onDayNightModeChanged(type: Int) {
            rootView.context?.let { ctx ->
                rootView.background = MaterialShapeDrawable(
                    ShapeAppearanceModel.builder()
                        .setAllCornerSizes(ctx.resources.getDimensionPixelSize(R.dimen.shimmer_radius).toFloat())
                        .build()
                ).apply {
                    fillColor = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.colorPrimaryDark))
                    strokeColor = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.gray))
                    strokeWidth = convertDpToPixel(1f).toFloat()
                }
                userNameTv?.setTextColor(ContextCompat.getColor(ctx,R.color.gray))
                textTv?.setTextColor(ContextCompat.getColor(ctx,R.color.white))
            }
        }
    }

    override fun onDayNightModeChanged(type: Int) {
        for (item in boundViewHolders) {
            item.onDayNightModeChanged(type)
        }
    }
}