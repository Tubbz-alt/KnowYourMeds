package com.tompee.utilities.knowyourmeds.view.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.MedDetailActivity;

public class WebViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static WebViewFragment mSingleton;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private WebView mWebview;

    public static WebViewFragment getInstance() {
        if (mSingleton == null) {
            mSingleton = new WebViewFragment();
        }
        return mSingleton;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.container);
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(),
                R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        Medicine med = ((MedDetailActivity) getActivity()).getMedicine();
        if (med.getUrl() != null) {
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);
            mWebview = (WebView) view.findViewById(R.id.webview);
            mWebview.setWebViewClient(new GenericWebClient());
            mWebview.setWebChromeClient(new GenericWebChromeClient());
            mWebview.clearCache(true);
            WebSettings webSettings = mWebview.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
            mWebview.loadUrl(med.getUrl());
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_webview, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            mSwipeRefreshLayout.setRefreshing(true);
            refresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
