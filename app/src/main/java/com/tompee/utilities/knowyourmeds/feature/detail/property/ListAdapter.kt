package com.tompee.utilities.knowyourmeds.feature.detail.property

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tompee.utilities.knowyourmeds.R

class ListAdapter(private val list: List<String>,
                  private val listener: ItemClickListener) : RecyclerView.Adapter<ListAdapter.Holder>() {

    interface ItemClickListener {
        fun onItemClick(item: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_text, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position], listener)
    }

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val listText = view.findViewById<TextView>(R.id.listText)

        fun bind(string: String, listener: ItemClickListener) {
            listText.text = string
            itemView.setOnClickListener {
                listener.onItemClick(string)
            }
        }
    }
}