package com.tompee.utilities.knowyourmeds.feature.detail.type

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseFragment
import com.tompee.utilities.knowyourmeds.databinding.FragmentListBinding
import com.tompee.utilities.knowyourmeds.feature.common.DividerDecorator
import com.tompee.utilities.knowyourmeds.model.Brand
import com.tompee.utilities.knowyourmeds.model.MedicineType
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class TypeFragment : BaseFragment<FragmentListBinding>() {

    @Inject
    lateinit var factory: TypeViewModel.Factory

    @Inject
    lateinit var typeAdapter: TypeAdapter

    companion object {
        private const val TAG_TYPE = "type_tag"

        fun newInstance(type: MedicineType): TypeFragment {
            val fragment = TypeFragment()
            val bundle = Bundle()
            bundle.putString(TAG_TYPE, type.tag)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun setupBindingAndViewModel(binding: FragmentListBinding) {
        val vm = ViewModelProviders.of(this, factory)[TypeViewModel::class.java]
        binding.viewModel = vm
        binding.list.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerDecorator(ContextCompat.getDrawable(context, R.drawable.list_divider)!!))
            adapter = typeAdapter
        }

        typeAdapter.listener = {
            // TODO: Add search?
        }

        vm.list.observe(this, Observer {
            typeAdapter.medicineList = it
        })
        vm.onLoad(MedicineType[arguments?.getString(TAG_TYPE) ?: Brand.tag])
    }

    override val layoutId: Int
        get() = R.layout.fragment_list
}