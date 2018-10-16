package com.tompee.utilities.knowyourmeds.feature.detail.page

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.gms.ads.AdRequest
import com.tompee.utilities.knowyourmeds.BuildConfig
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseFragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_webview.*
import javax.inject.Inject

class PageFragment : BaseFragment(), PageView, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var pagePresenter: PagePresenter

    companion object {
        fun getInstance(): PageFragment = PageFragment()
    }

    //region PageFragment
    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val builder = AdRequest.Builder()
        if (BuildConfig.DEBUG) {
            builder.addTestDevice("3AD737A018BB67E7108FD1836E34DD1C")
        }
        adView.loadAd(builder.build())

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

        container.setColorSchemeColors(ContextCompat.getColor(context!!, R.color.colorPrimary))
        pagePresenter.attachView(this)
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_webview, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.menu_refresh) {
            container.isRefreshing = true
            refresh()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun refresh() {
        webView.reload()
    }

    private inner class GenericWebClient : WebViewClient() {
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progressbar?.progress = 0
            progressbar?.visibility = View.VISIBLE
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            progressbar?.visibility = View.INVISIBLE
            container?.isRefreshing = false
        }
    }

    private inner class GenericWebChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView, progress: Int) {
            progressbar?.progress = progress
        }
    }

    //endregion

    //region BaseFragment
    override fun layoutId(): Int = R.layout.fragment_webview
    //endregion

    //region OnRefreshListener
    override fun onRefresh() {
        refresh()
    }
    //endregion

    //region
    override fun loadUrl(url: String) {
        webView.loadUrl(url)
    }
    //endregion
}