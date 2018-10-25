package com.tompee.utilities.knowyourmeds.feature.license

import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseActivity
import com.tompee.utilities.knowyourmeds.databinding.ActivityLicenseBinding
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

@Suppress("DEPRECATION")
class LicenseActivity : BaseActivity<ActivityLicenseBinding>() {

    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var factory: LicenseViewModel.Factory

    companion object {
        const val TAG_MODE = "mode"
        const val LICENSE = 1
        const val PRIVACY = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun setupBindingAndViewModel(binding: ActivityLicenseBinding) {
        setToolbar(binding.include.toolbar, true)
        binding.include.toolbarBg.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLauncher))

        val vm = ViewModelProviders.of(this, factory)[LicenseViewModel::class.java]
        binding.viewModel = vm

        vm.title.observe(this, Observer {
            binding.include.toolbarTitle.text = it
        })

        vm.message.observe(this, Observer {
            binding.content.movementMethod = LinkMovementMethod()
        })

        vm.onLoaded(intent.getIntExtra(TAG_MODE, LICENSE) == LICENSE)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector

    override val layoutId: Int
        get() = R.layout.activity_license

}