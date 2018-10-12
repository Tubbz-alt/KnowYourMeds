package com.tompee.utilities.knowyourmeds.feature.detail.type

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseFragment
import com.tompee.utilities.knowyourmeds.feature.detail.DetailActivity
import com.tompee.utilities.knowyourmeds.model.Type
import kotlinx.android.synthetic.main.fragment_tty.*
import javax.inject.Inject

class TypeFragment : BaseFragment(), TypeView {

    @Inject
    lateinit var typePresenter: TypePresenter

    companion object {
        private const val TAB_NAME = "tab_name"
        private const val TYPE = "type"

        fun newInstance(tabName: String, type: String): TypeFragment {
            val fragment = TypeFragment()
            val bundle = Bundle()
            bundle.putString(TAB_NAME, tabName)
            bundle.putString(TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }

    //region TypeFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabName.text = arguments?.getString(TAB_NAME)
        typePresenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        typePresenter.detachView()
    }
    //endregion

    //region BaseFragment
    override fun setupComponent() {
        DetailActivity[activity!!].detailComponent.inject(this)
    }

    override fun layoutId(): Int = R.layout.fragment_tty
    //endregion

    //region TypeView
    override fun getType(): Type = Type.getType(arguments?.getString(TYPE)!!)

    override fun setAdapter(adapter: ListAdapter) {
        list.setHasFixedSize(true)
        list.layoutManager = LinearLayoutManager(context)
        val divider = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.list_divider)!!)
        list.addItemDecoration(divider)
        list.adapter = adapter
    }
    //endregion
}