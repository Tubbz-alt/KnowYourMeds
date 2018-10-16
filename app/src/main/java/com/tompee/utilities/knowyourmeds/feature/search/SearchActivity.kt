package com.tompee.utilities.knowyourmeds.feature.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseActivity
import com.tompee.utilities.knowyourmeds.core.asset.AssetManager
import com.tompee.utilities.knowyourmeds.feature.help.HelpActivity
import com.tompee.utilities.knowyourmeds.feature.search.disclaimer.DisclaimerDialog
import com.tompee.utilities.knowyourmeds.feature.search.disclaimer.DisclaimerDialogListener
import com.tompee.utilities.knowyourmeds.feature.search.rater.AppRaterDialog
import com.tompee.utilities.knowyourmeds.feature.search.search.SearchBarFragment
import com.tompee.utilities.knowyourmeds.view.SettingsActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject


class SearchActivity : BaseActivity(), SearchView, DisclaimerDialogListener {
    @Inject
    lateinit var searchPresenter: SearchPresenter

    @Inject
    lateinit var assetManager: AssetManager

    companion object {
        operator fun get(activity: Activity): SearchActivity {
            return activity as SearchActivity
        }
    }

    // region SearchActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setToolbar(toolbar, false)
        toolbar_text.setText(R.string.app_name)
        background.setImageDrawable(assetManager.getDrawableFromAsset("search_bg.jpg"))

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, SearchBarFragment.newInstance())
        transaction.commit()

        searchPresenter.attachView(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent: Intent
        when (item.itemId) {
            R.id.menu_settings -> {
                intent = Intent(this, SettingsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                return true
            }
            R.id.menu_about -> {
                intent = Intent(this, HelpActivity::class.java)
                intent.putExtra(HelpActivity.TAG_MODE, HelpActivity.ABOUT)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                return true
            }
            R.id.menu_policy -> {
                intent = Intent(this, HelpActivity::class.java)
                intent.putExtra(HelpActivity.TAG_MODE, HelpActivity.PRIVACY)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                return true
            }
            R.id.menu_license -> {
                intent = Intent(this, HelpActivity::class.java)
                intent.putExtra(HelpActivity.TAG_MODE, HelpActivity.LICENSE)
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

    override fun onDestroy() {
        super.onDestroy()
        searchPresenter.detachView()
    }
    //endregion

    //region SearchView

    override fun setAdapter(adapter: SearchViewPagerAdapter) {
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun showDisclaimer(isFirst: Boolean) {
        val dialog = DisclaimerDialog.newInstance(isFirst)
        dialog.show(supportFragmentManager, "disclaimer")
    }

    override fun showAppRater() {
        val dialog = AppRaterDialog.newInstance()
        dialog.show(supportFragmentManager, "appRater")
    }
    //endregion

    //region DisclaimerDialogListener

    override fun onUnderstand() {
    }

    override fun onCancelled() {
    }

    //endregion
}