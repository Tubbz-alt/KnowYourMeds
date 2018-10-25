package com.tompee.utilities.knowyourmeds.feature.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.databinding.ListTextBinding

class ListTextAdapter : RecyclerView.Adapter<ListTextAdapter.ViewHolder>() {
    var list: List<String> = listOf()
        set(value) {
            val result = DiffUtil.calculateDiff(DiffUtilCallback(value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    var listener: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ListTextBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_text,
                parent,
                false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            text = list[position]
            root.setOnClickListener {
                listener(list[position])
            }
            executePendingBindings()
        }
    }

    private inner class DiffUtilCallback(private val list: List<String>) : DiffUtil.Callback() {
        val oldList = this@ListTextAdapter.list

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldList[oldItemPosition] == list[newItemPosition]

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = list.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                areItemsTheSame(oldItemPosition, newItemPosition)
    }

    class ViewHolder(val binding: ListTextBinding) : RecyclerView.ViewHolder(binding.root)
}