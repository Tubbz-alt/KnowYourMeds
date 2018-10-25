package com.tompee.utilities.knowyourmeds.feature.detail.property

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseFragment
import com.tompee.utilities.knowyourmeds.databinding.FragmentPropertiesBinding
import com.tompee.utilities.knowyourmeds.feature.common.DividerDecorator
import com.tompee.utilities.knowyourmeds.feature.common.ListTextAdapter
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class PropertyFragment : BaseFragment<FragmentPropertiesBinding>() {

    @Inject
    lateinit var factory: PropertyViewModel.Factory

    @Inject
    lateinit var listAdapter: ListTextAdapter

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun setupBindingAndViewModel(binding: FragmentPropertiesBinding) {
        val vm = ViewModelProviders.of(this, factory)[PropertyViewModel::class.java]
        binding.viewModel = vm

        binding.ingredientList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerDecorator(ContextCompat.getDrawable(context, R.drawable.list_divider)!!))
            adapter = listAdapter
        }

        binding.page.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(vm.url.value))
            startActivity(browserIntent)
        }

        observeViewModel(vm)
    }

    private fun observeViewModel(vm: PropertyViewModel) {
        vm.medicine.observe(this, Observer {
            listAdapter.list = it.ingredientList
        })
    }

    override val layoutId: Int
        get() = R.layout.fragment_properties

}