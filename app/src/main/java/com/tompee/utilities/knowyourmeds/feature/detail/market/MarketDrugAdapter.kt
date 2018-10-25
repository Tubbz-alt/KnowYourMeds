package com.tompee.utilities.knowyourmeds.feature.detail.market

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.databinding.ListMarketDrugBinding
import com.tompee.utilities.knowyourmeds.model.MarketDrug

class MarketDrugAdapter : RecyclerView.Adapter<MarketDrugAdapter.ViewHolder>() {
    var marketDrugList: List<MarketDrug> = listOf()
        set(value) {
            val result = DiffUtil.calculateDiff(DiffUtilCallback(value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    var listener: (MarketDrug) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ListMarketDrugBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_market_drug,
                parent,
                false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = marketDrugList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            drug = marketDrugList[position]
            root.setOnClickListener {
                listener(marketDrugList[position])
            }
            executePendingBindings()
        }
    }

    private inner class DiffUtilCallback(private val list: List<MarketDrug>) : DiffUtil.Callback() {
        val oldList = this@MarketDrugAdapter.marketDrugList

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldList[oldItemPosition].setId == list[newItemPosition].setId

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = list.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                areItemsTheSame(oldItemPosition, newItemPosition)
    }

    class ViewHolder(val binding: ListMarketDrugBinding) : RecyclerView.ViewHolder(binding.root)
}