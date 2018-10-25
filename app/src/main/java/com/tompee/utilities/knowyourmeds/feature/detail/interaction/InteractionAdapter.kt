package com.tompee.utilities.knowyourmeds.feature.detail.interaction

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.databinding.ListInteractionBinding
import com.tompee.utilities.knowyourmeds.feature.common.TextDrawable
import com.tompee.utilities.knowyourmeds.model.InteractionPair

class InteractionAdapter(private val context: Context) : RecyclerView.Adapter<InteractionAdapter.ViewHolder>() {
    var interactionList: List<InteractionPair> = listOf()
        set(value) {
            val result = DiffUtil.calculateDiff(DiffUtilCallback(value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    var listener: (InteractionPair) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ListInteractionBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_interaction,
                parent,
                false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = interactionList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = interactionList[position]
        current.drawable = TextDrawable(context.resources, current.partner.substring(0, 1), false)
        holder.binding.apply {
            pair = current
            root.setOnClickListener {
                listener(current)
            }
            executePendingBindings()
        }
    }

    private inner class DiffUtilCallback(private val list: List<InteractionPair>) : DiffUtil.Callback() {
        val oldList = this@InteractionAdapter.interactionList

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldList[oldItemPosition].partner == list[newItemPosition].partner

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = list.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                areItemsTheSame(oldItemPosition, newItemPosition)
    }

    class ViewHolder(val binding: ListInteractionBinding) : RecyclerView.ViewHolder(binding.root)
}