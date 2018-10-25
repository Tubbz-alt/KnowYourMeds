package com.tompee.utilities.knowyourmeds.feature.detail.type

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.databinding.ListMedicineBinding
import com.tompee.utilities.knowyourmeds.feature.common.TextDrawable
import com.tompee.utilities.knowyourmeds.model.Medicine


class TypeAdapter(private val context: Context) : RecyclerView.Adapter<TypeAdapter.ViewHolder>() {
    var medicineList: List<Medicine> = listOf()
        set(value) {
            val result = DiffUtil.calculateDiff(DiffUtilCallback(value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    var listener: (Medicine) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ListMedicineBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_medicine,
                parent,
                false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = medicineList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = medicineList[position]
        current.drawable = TextDrawable(context.resources, current.name.substring(0, 1), false)
        holder.binding.apply {
            medicine = current
            root.setOnClickListener {
                listener(current)
            }
            executePendingBindings()
        }
    }

    private inner class DiffUtilCallback(private val list: List<Medicine>) : DiffUtil.Callback() {
        val oldList = this@TypeAdapter.medicineList

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldList[oldItemPosition].id == list[newItemPosition].id

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = list.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                areItemsTheSame(oldItemPosition, newItemPosition)
    }

    class ViewHolder(val binding: ListMedicineBinding) : RecyclerView.ViewHolder(binding.root)
}