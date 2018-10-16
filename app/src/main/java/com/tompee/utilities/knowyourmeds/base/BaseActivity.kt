package com.tompee.utilities.knowyourmeds.base

import android.support.v7.widget.Toolbar
import android.view.MenuItem
import dagger.android.HasFragmentInjector
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity(), HasFragmentInjector {

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    protected fun setToolbar(toolbar: Toolbar, homeButtonEnable: Boolean = false) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(homeButtonEnable)
    }
}