package com.tompee.utilities.knowyourmeds.feature.main.cache

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.databinding.ListCacheBinding
import com.tompee.utilities.knowyourmeds.model.Medicine

class CacheListAdapter : RecyclerView.Adapter<CacheListAdapter.ViewHolder>() {
    var medicineList: List<Medicine> = listOf()
        set(value) {
            val result = DiffUtil.calculateDiff(DiffUtilCallback(value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    var tapListener: (Medicine) -> Unit = {}
    var deleteListener: (Medicine) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ListCacheBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_cache,
                parent,
                false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = medicineList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = medicineList[position]
        holder.binding.apply {
            medicine = medicineList[position]
            root.setOnClickListener {
                tapListener(current)
            }
            delete.setOnClickListener {
                deleteListener(current)
            }
            executePendingBindings()
        }
    }

    private inner class DiffUtilCallback(private val list: List<Medicine>) : DiffUtil.Callback() {
        val oldList = this@CacheListAdapter.medicineList

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldList[oldItemPosition].id == list[newItemPosition].id

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = list.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                areItemsTheSame(oldItemPosition, newItemPosition)
    }

    class ViewHolder(val binding: ListCacheBinding) : RecyclerView.ViewHolder(binding.root)
}