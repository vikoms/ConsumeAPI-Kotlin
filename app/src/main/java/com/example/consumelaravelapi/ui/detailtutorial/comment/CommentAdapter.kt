package com.example.consumelaravelapi.ui.detailtutorial.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.consumelaravelapi.R
import com.example.consumelaravelapi.entity.Comment
import kotlinx.android.synthetic.main.item_row.view.*

class CommentAdapter: RecyclerView.Adapter<CommentAdapter.CommentHolder>() {

    val comments = ArrayList<Comment>()
    fun setData(commentList : List<Comment>) {
        comments.clear()
        comments.addAll(commentList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row,parent,false)
        return CommentHolder(
            view
        )
    }

    override fun getItemCount(): Int =  comments.size

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        holder.bind(comments[position])
    }


    class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(comment: Comment) {
            with(itemView) {
                tv_first.text = comment.body
                img_delete.visibility = View.GONE
            }
        }
    }
}