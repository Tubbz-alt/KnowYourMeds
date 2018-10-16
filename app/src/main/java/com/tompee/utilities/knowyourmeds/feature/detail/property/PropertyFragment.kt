package com.tompee.utilities.knowyourmeds.feature.detail.property

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseFragment
import com.tompee.utilities.knowyourmeds.feature.spl.MarketDrugActivity
import com.tompee.utilities.knowyourmeds.model.MarketDrug
import com.tompee.utilities.knowyourmeds.model.Type
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_properties.*
import javax.inject.Inject

class PropertyFragment : BaseFragment(), PropertyView {

    @Inject
    lateinit var propertyPresenter: PropertyPresenter

    companion object {
        fun getInstance(): PropertyFragment = PropertyFragment()
    }

    //region PropertyFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        propertyPresenter.attachView(this)
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onDestroy() {
        super.onDestroy()
        propertyPresenter.detachView()
    }

    //endregion

    //region BaseFragment
    override fun layoutId(): Int = R.layout.fragment_properties
    //endregion

    //region PropertyView

    override fun setIsPrescribable(isPrescribable: Boolean) {
        if (isPrescribable) {
            prescribableText.setText(R.string.property_prescribable_yes)
        } else {
            prescribableText.setText(R.string.property_prescribable_no)
        }
    }

    override fun setType(type: Type) {
        ttyText.text = type.name(context!!)
    }

    override fun setIngredientAdapter(adapter: ListAdapter?) {
        if (adapter != null) {
            ingredientList.setHasFixedSize(true)
            ingredientList.layoutManager = LinearLayoutManager(activity)
            ingredientList.addItemDecoration(DividerDecorator(20))
            ingredientList.adapter = adapter
        } else {
            ingredients.visibility = View.GONE
        }
    }

    override fun setSplAdapter(adapter: MarketDrugAdapter?) {
        if (adapter != null) {
            splList.setHasFixedSize(true)
            splList.layoutManager = LinearLayoutManager(context)
            splCount.text = adapter.itemCount.toString()
            val divider = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            divider.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.list_divider)!!)
            splList.addItemDecoration(divider)
            splList.adapter = adapter
        } else {
            spls.visibility = View.GONE
        }
    }

    override fun moveToMarketDrugActivity(marketDrug: MarketDrug) {
        val intent = Intent(context, MarketDrugActivity::class.java)
        intent.putExtra(MarketDrugActivity.TAG_SET_ID, marketDrug.setId)
        intent.putExtra(MarketDrugActivity.TAG_NAME, marketDrug.name)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    //endregion
}