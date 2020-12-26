package com.ms.playstop.ui.movie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ms.playstop.R
import com.ms.playstop.model.Comment
import kotlinx.android.synthetic.main.item_comment_layout.view.*

class CommentAdapter(private val comments: List<Comment>): RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

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
        }
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rootView = itemView
        val userNameTv = itemView.comment_user_name_tv
        val textTv = itemView.comment_text_tv
        fun bind(comment: Comment) {
            userNameTv?.text = comment.user.name
            textTv?.text = comment.text
        }
    }
}