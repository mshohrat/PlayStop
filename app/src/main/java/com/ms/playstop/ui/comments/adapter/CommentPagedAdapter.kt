package com.ms.playstop.ui.comments.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ms.playstop.R
import com.ms.playstop.model.Comment
import com.ms.playstop.model.Movie
import kotlinx.android.synthetic.main.item_comment_layout.view.*

val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Comment>() {

    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem == newItem
    }

}

class CommentPagedAdapter: PagedListAdapter<Comment,CommentPagedAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_comment_layout,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = getItem(position)
        holder.bind(comment)
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rootView = itemView
        val userNameTv = itemView.comment_user_name_tv
        val textTv = itemView.comment_text_tv
        fun bind(comment: Comment?) {
            userNameTv?.text = comment?.user?.name
            textTv?.text = comment?.text
        }
    }
}