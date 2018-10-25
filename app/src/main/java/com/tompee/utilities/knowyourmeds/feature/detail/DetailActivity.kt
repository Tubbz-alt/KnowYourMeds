package com.tompee.utilities.knowyourmeds.feature.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseActivity
import com.tompee.utilities.knowyourmeds.databinding.ActivityMedDetailBinding
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

class DetailActivity : BaseActivity<ActivityMedDetailBinding>() {

    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var pageAdapter: DetailPageAdapter

    @Inject
    lateinit var factory: DetailViewModel.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun setupBindingAndViewModel(binding: ActivityMedDetailBinding) {
        setToolbar(binding.include.toolbar, true)

        val vm = ViewModelProviders.of(this, factory)[DetailViewModel::class.java]
        binding.viewModel = vm


        binding.viewPager.apply {
            offscreenPageLimit = pageAdapter.count
            adapter = pageAdapter
        }
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        observeViewModel(vm)
    }

    private fun observeViewModel(vm: DetailViewModel) {
        vm.propertyFragment.observe(this, Observer {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.propertyContainer, it)
            transaction.commit()
            supportFragmentManager.executePendingTransactions()
        })
    }

    override val layoutId: Int
        get() = R.layout.activity_med_detail

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector
}