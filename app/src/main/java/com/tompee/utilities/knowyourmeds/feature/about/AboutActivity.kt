package com.tompee.utilities.knowyourmeds.feature.about

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseActivity
import com.tompee.utilities.knowyourmeds.databinding.ActivityAboutBinding
import com.tompee.utilities.knowyourmeds.databinding.ActivityLicenseBinding
import com.tompee.utilities.knowyourmeds.feature.license.LicenseViewModel
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

@Suppress("DEPRECATION")
class AboutActivity : BaseActivity<ActivityAboutBinding>() {

    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var factory: AboutViewModel.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun setupBindingAndViewModel(binding: ActivityAboutBinding) {
        setToolbar(binding.include.toolbar, true)
        binding.include.toolbarBg.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLauncher))

        val vm = ViewModelProviders.of(this, factory)[AboutViewModel::class.java]
        binding.viewModel = vm

        vm.title.observe(this, Observer {
            binding.include.toolbarTitle.text = it
        })
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector

    override val layoutId: Int
        get() = R.layout.activity_about

}