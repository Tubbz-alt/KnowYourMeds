package com.tompee.utilities.knowyourmeds.feature.spl

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.gms.ads.AdRequest
import com.tompee.utilities.knowyourmeds.BuildConfig
import com.tompee.utilities.knowyourmeds.Constants.DAILY_MED_PAGE_URL
import com.tompee.utilities.knowyourmeds.KnowYourMedsApplication
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseActivity
import com.tompee.utilities.knowyourmeds.core.asset.AssetManager
import com.tompee.utilities.knowyourmeds.di.component.DaggerMarketDrugComponent
import kotlinx.android.synthetic.main.activity_spl_detail.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class MarketDrugActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var assetManager: AssetManager

    companion object {
        const val TAG_SET_ID = "set_id"
        const val TAG_NAME = "name"
    }

    //region MarketDrugActivity
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        background.setImageDrawable(assetManager.getDrawableFromAsset("search_bg.jpg"))
        setToolbar(toolbar, true)
        container.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary))
        container.setOnRefreshListener(this)

        toolbar_text.text = intent.getStringExtra(TAG_NAME)

        webView.webViewClient = GenericWebClient()
        webView.webChromeClient = GenericWebChromeClient()
        webView.clearCache(true)
        webView.settings.apply {
            javaScriptEnabled = true
            builtInZoomControls = true
            displayZoomControls = true
            javaScriptCanOpenWindowsAutomatically = true
            useWideViewPort = true
            loadWithOverviewMode = true
            domStorageEnabled = true
            layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        }

        val builder = AdRequest.Builder()
        if (BuildConfig.DEBUG) {
            builder.addTestDevice("3AD737A018BB67E7108FD1836E34DD1C")
        }
        adView.loadAd(builder.build())
        webView.loadUrl(DAILY_MED_PAGE_URL + intent.getStringExtra(TAG_SET_ID))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_webview, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_refresh -> {
                container.isRefreshing = true
                refresh()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun refresh() {
        webView.reload()
    }

    private inner class GenericWebClient : WebViewClient() {
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progressbar.progress = 0
            progressbar.visibility = View.VISIBLE
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            progressbar.visibility = View.INVISIBLE
            container.isRefreshing = false
        }
    }

    private inner class GenericWebChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView, progress: Int) {
            progressbar.progress = progress
        }
    }
    //endregion

    //region OnRefreshListener
    override fun onRefresh() {
        refresh()
    }
    //endregion

    //region BaseActivity
    override fun setupComponent() {
        DaggerMarketDrugComponent.builder()
                .appComponent(KnowYourMedsApplication[this].component)
                .build()
                .inject(this)
    }

    override fun layoutId(): Int = R.layout.activity_spl_detail
    //endregion
}