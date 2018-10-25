package com.tompee.utilities.knowyourmeds.feature.detail.interaction

import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseFragment
import com.tompee.utilities.knowyourmeds.databinding.FragmentInteractionBinding
import com.tompee.utilities.knowyourmeds.feature.common.DividerDecorator
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class InteractionFragment : BaseFragment<FragmentInteractionBinding>() {

    @Inject
    lateinit var factory: InteractionViewModel.Factory

    @Inject
    lateinit var interactionAdapter: InteractionAdapter

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun setupBindingAndViewModel(binding: FragmentInteractionBinding) {
        val vm = ViewModelProviders.of(this, factory)[InteractionViewModel::class.java]
        binding.viewModel = vm

        binding.list.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerDecorator(ContextCompat.getDrawable(context, R.drawable.list_divider)!!))
            adapter = interactionAdapter
        }

        interactionAdapter.listener = {
        }

        vm.list.observe(this, Observer {
            interactionAdapter.interactionList = it
        })
    }

    override val layoutId: Int
        get() = R.layout.fragment_interaction
}