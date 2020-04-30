package com.example.consumelaravelapi.ui.tutorial

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.consumelaravelapi.R
import com.example.consumelaravelapi.entity.Comment
import com.example.consumelaravelapi.entity.Tutorial
import com.example.consumelaravelapi.ui.MainActivity
import kotlinx.android.synthetic.main.item_row.view.*

class TutorialAdapter internal constructor(private val listener: OnItemClickCallback): RecyclerView.Adapter<TutorialAdapter.TutorialViewHolder>() {

    private val tutorials = ArrayList<Tutorial>()


    fun setData(listTutorial: List<Tutorial>) {
        tutorials.clear()
        tutorials.addAll(listTutorial)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutorialViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row,parent,false)
        return TutorialViewHolder(view)
    }

    override fun getItemCount(): Int = tutorials.size

    override fun onBindViewHolder(holder: TutorialViewHolder, position: Int) {
        with(holder) {
            bind(tutorials[position])
            itemView.setOnClickListener{
                listener.onItemClicked(tutorials[holder.adapterPosition], "show")
            }
            itemView.img_delete.setOnClickListener{
            listener.onItemClicked(tutorials[holder.adapterPosition],"delete")
            }
        }

    }

    class TutorialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(tutorial: Tutorial) {
            val id = MainActivity.user.id
            with(itemView) {
                tv_first.text = tutorial.title
                if (tutorial.user_id != id) {
                    img_delete.visibility = View.GONE
                }
            }
        }
    }

    internal interface OnItemClickCallback{
        fun onItemClicked(data: Tutorial, action: String)
    }
}