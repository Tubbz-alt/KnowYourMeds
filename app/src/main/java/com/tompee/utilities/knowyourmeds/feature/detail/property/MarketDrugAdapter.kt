package com.tompee.utilities.knowyourmeds.feature.detail.property

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.model.MarketDrug

class MarketDrugAdapter(private val list: List<MarketDrug>) : RecyclerView.Adapter<MarketDrugAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.list_market_drug, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position])
    }

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.name)
        private val setId = view.findViewById<TextView>(R.id.setId)
        private val date = view.findViewById<TextView>(R.id.date)

        fun bind(drug: MarketDrug) {
            name.text = drug.name
            setId.text = drug.setId
            date.text = drug.publishedDate
        }
    }
}