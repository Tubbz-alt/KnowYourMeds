package com.tompee.utilities.knowyourmeds.feature.main.cache

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseFragment
import com.tompee.utilities.knowyourmeds.databinding.FragmentListBinding
import com.tompee.utilities.knowyourmeds.feature.common.DividerDecorator
import com.tompee.utilities.knowyourmeds.feature.detail.DetailActivity
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class CacheFragment : BaseFragment<FragmentListBinding>() {

    @Inject
    lateinit var factory: CacheViewModel.Factory

    @Inject
    lateinit var cacheListAdapter: CacheListAdapter

    companion object {
        private const val TAG = "is_recent"

        fun newInstance(isRecent: Boolean): CacheFragment {
            val fragment = CacheFragment()
            val bundle = Bundle()
            bundle.putBoolean(TAG, isRecent)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun setupBindingAndViewModel(binding: FragmentListBinding) {
        val vm = ViewModelProviders.of(this, factory)[CacheViewModel::class.java]
        binding.viewModel = vm

        val isRecent = arguments?.getBoolean(TAG, true) ?: true
        binding.list.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerDecorator(ContextCompat.getDrawable(context, R.drawable.list_divider)!!))
            adapter = cacheListAdapter
        }

        cacheListAdapter.tapListener = {
            vm.searchNew(it)
            val intent = Intent(context, DetailActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        cacheListAdapter.deleteListener = { vm.delete(isRecent, it) }

        observeViewModel(vm)
        vm.onLoad(isRecent)
    }

    private fun observeViewModel(vm: CacheViewModel) {
        vm.list.observe(this, Observer {
            cacheListAdapter.medicineList = it
        })
    }

    override val layoutId: Int
        get() = R.layout.fragment_list
}