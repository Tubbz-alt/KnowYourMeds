package com.tompee.utilities.knowyourmeds.feature.main.search

import android.content.Intent
import android.view.animation.BounceInterpolator
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.google.android.material.snackbar.Snackbar
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseFragment
import com.tompee.utilities.knowyourmeds.core.helper.AnimationHelper
import com.tompee.utilities.knowyourmeds.databinding.FragmentSearchBinding
import com.tompee.utilities.knowyourmeds.feature.common.DividerDecorator
import com.tompee.utilities.knowyourmeds.feature.common.ListTextAdapter
import com.tompee.utilities.knowyourmeds.feature.detail.DetailActivity
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SearchFragment : BaseFragment<FragmentSearchBinding>() {
    @Inject
    lateinit var vmFactory: SearchViewModel.Factory

    @Inject
    lateinit var listTextAdapter: ListTextAdapter

    companion object {
        private const val ANIMATION_DURATION = 700
        private const val VERTICAL_POSITION_BOUNCE = 152
    }

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun setupBindingAndViewModel(binding: FragmentSearchBinding) {
        val vm = ViewModelProviders.of(this, vmFactory)[SearchViewModel::class.java]
        binding.viewModel = vm

        binding.suggestionList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerDecorator(ContextCompat.getDrawable(context, R.drawable.list_divider)!!))
            adapter = listTextAdapter
        }

        binding.checkout.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        listTextAdapter.listener = {
            binding.searchView.setSearchText(it)
            vm.searchMedicine(it)
        }

        binding.searchView.setOnSearchListener(object : FloatingSearchView.OnSearchListener {
            override fun onSearchAction(currentQuery: String) {
                if (currentQuery.isNotEmpty()) {
                    vm.searchMedicine(currentQuery)
                }
            }

            override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {
            }
        })
        observeViewModel(vm, binding)
    }

    private fun observeViewModel(vm: SearchViewModel, binding: FragmentSearchBinding) {
        vm.showResult.observe(this, Observer {
            if (it) {
                AnimationHelper.animateVerticalPosition(binding.checkout,
                        (AnimationHelper.convertDPtoPixel(context!!, VERTICAL_POSITION_BOUNCE)).toFloat(),
                        ANIMATION_DURATION, BounceInterpolator())
            } else {
                AnimationHelper.animateVerticalPosition(binding.checkout, -(AnimationHelper.convertDPtoPixel(context!!,
                        VERTICAL_POSITION_BOUNCE)).toFloat(), 0)
            }
        })
        vm.suggestionList.observe(this, Observer {
            listTextAdapter.list = it
        })
        vm.showError.observe(this, Observer {
            if (it) {
                Snackbar.make(activity?.findViewById(android.R.id.content)!!,
                        getString(R.string.message_no_suggestions, binding.searchView.query), Snackbar.LENGTH_LONG).show()
            }
        })
    }

    override val layoutId: Int
        get() = R.layout.fragment_search
}