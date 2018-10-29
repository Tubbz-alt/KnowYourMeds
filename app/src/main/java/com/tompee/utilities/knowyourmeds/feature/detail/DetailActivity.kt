package com.tompee.utilities.knowyourmeds.feature.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

    private lateinit var viewModel: DetailViewModel
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun setupBindingAndViewModel(binding: ActivityMedDetailBinding) {
        setToolbar(binding.include.toolbar, true)

        val vm = ViewModelProviders.of(this, factory)[DetailViewModel::class.java]
        binding.viewModel = vm
        viewModel = vm

        binding.viewPager.apply {
            offscreenPageLimit = pageAdapter.count
            adapter = pageAdapter
        }
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        observeViewModel(vm)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        menu?.findItem(R.id.menu_favorite)?.setIcon(if (isFavorite) R.drawable.ic_star_white else R.drawable.ic_star_border_white)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (isFavorite) {
            viewModel.deleteFromFavorites()
        } else {
            viewModel.addToFavorites()
        }
        invalidateOptionsMenu()
        return super.onOptionsItemSelected(item)
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