package com.tompee.utilities.knowyourmeds.feature.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseActivity
import com.tompee.utilities.knowyourmeds.databinding.ActivityMainBinding
import com.tompee.utilities.knowyourmeds.feature.about.AboutActivity
import com.tompee.utilities.knowyourmeds.feature.license.LicenseActivity
import com.tompee.utilities.knowyourmeds.feature.main.disclaimer.DisclaimerDialog
import com.tompee.utilities.knowyourmeds.feature.main.rater.AppRaterDialog
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityMainBinding>() {

    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var factory: MainViewModel.Factory

    @Inject
    lateinit var pagerAdapter: MainPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent: Intent
        when (item.itemId) {
            R.id.menu_about -> {
                intent = Intent(this, AboutActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                return true
            }
            R.id.menu_policy -> {
                intent = Intent(this, LicenseActivity::class.java)
                intent.putExtra(LicenseActivity.TAG_MODE, LicenseActivity.PRIVACY)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                return true
            }
            R.id.menu_license -> {
                intent = Intent(this, LicenseActivity::class.java)
                intent.putExtra(LicenseActivity.TAG_MODE, LicenseActivity.LICENSE)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                return true
            }
            R.id.menu_disclaimer -> {
                showDisclaimer(false)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDisclaimer(isFirst: Boolean) {
        val dialog = DisclaimerDialog.newInstance(isFirst)
        dialog.show(supportFragmentManager, "disclaimer")
    }

    override fun setupBindingAndViewModel(binding: ActivityMainBinding) {
        setToolbar(binding.include.toolbar, false)
        binding.viewPager.apply {
            offscreenPageLimit = pagerAdapter.count
            adapter = pagerAdapter
        }

        val vm = ViewModelProviders.of(this, factory)[MainViewModel::class.java]
        binding.viewModel = vm
        observeViewModel(vm)
    }

    private fun observeViewModel(vm: MainViewModel) {
        vm.isShowDisclaimer.observe(this, Observer {
            if (it) {
                val dialog = DisclaimerDialog.newInstance(true)
                dialog.show(supportFragmentManager, "disclaimer")
            }
        })
        vm.isShowAppRater.observe(this, Observer {
            if (it) {
                val dialog = AppRaterDialog.newInstance()
                dialog.show(supportFragmentManager, "appRater")
            }
        })
        vm.needsClose.observe(this, Observer {
            if (it) finish()
        })
    }

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector
}