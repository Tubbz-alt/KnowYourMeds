package com.tompee.utilities.knowyourmeds.view;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tompee.utilities.knowyourmeds.BuildConfig;
import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.Utilities;
import com.tompee.utilities.knowyourmeds.view.base.BaseActivity;

public class SPLDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final String NAME = "name";
    public static final String URL = "url";

    private WebView mWebview;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spl_detail);
        setToolbar(R.id.toolbar, true);

        ImageView imageView = (ImageView) findViewById(R.id.background);
        imageView.setImageDrawable(Utilities.getDrawableFromAsset(this, "search_bg.jpg"));

        TextView title = (TextView) findViewById(R.id.toolbar_text);
        title.setText(getIntent().getStringExtra(NAME));

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container);
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mWebview = (WebView) findViewById(R.id.webview);
        mWebview.setWebViewClient(new GenericWebClient());
        mWebview.setWebChromeClient(new GenericWebChromeClient());
        mWebview.clearCache(true);
        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        mWebview.loadUrl(getIntent().getStringExtra(URL));

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest.Builder builder = new AdRequest.Builder();
        if (BuildConfig.DEBUG) {
            builder.addTestDevice("3AD737A018BB67E7108FD1836E34DD1C");
        }
        mAdView.loadAd(builder.build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_webview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                mSwipeRefreshLayout.setRefreshing(true);
                refresh();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mWebview.canGoBack()) {
            mWebview.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    private void refresh() {
        if (mWebview != null) {
            mWebview.reload();
        }
    }

    private class GenericWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressBar.setProgress(0);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressBar.setVisibility(View.INVISIBLE);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private class GenericWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int progress) {
            mProgressBar.setProgress(progress);
        }
    }
}