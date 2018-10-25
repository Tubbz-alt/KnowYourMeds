package com.tompee.utilities.knowyourmeds.feature.detail.market

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tompee.utilities.knowyourmeds.Constants
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseFragment
import com.tompee.utilities.knowyourmeds.databinding.FragmentListBinding
import com.tompee.utilities.knowyourmeds.feature.common.DividerDecorator
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class MarketDrugFragment : BaseFragment<FragmentListBinding>() {

    @Inject
    lateinit var factory: MarketDrugViewModel.Factory

    @Inject
    lateinit var marketAdapter: MarketDrugAdapter

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun setupBindingAndViewModel(binding: FragmentListBinding) {
        val vm = ViewModelProviders.of(this, factory)[MarketDrugViewModel::class.java]
        binding.viewModel = vm

        binding.list.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerDecorator(ContextCompat.getDrawable(context, R.drawable.list_divider)!!))
            adapter = marketAdapter
        }

        marketAdapter.listener = {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.DAILY_MED_PAGE_URL + it.setId))
            startActivity(browserIntent)
        }

        vm.list.observe(this, Observer {
            marketAdapter.marketDrugList = it
        })
    }

    override val layoutId: Int
        get() = R.layout.fragment_list
}