package com.tompee.utilities.knowyourmeds.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.tompee.utilities.knowyourmeds.BuildConfig;
import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.view.adapter.MainViewPagerAdapter;
import com.tompee.utilities.knowyourmeds.view.base.BaseActivity;
import com.tompee.utilities.knowyourmeds.view.dialog.DisclaimerDialog;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener,
        DisclaimerDialog.DisclaimerDialogListener {
    public static final String SHARED_PREF = "knowyourmedspref";
    private static final String TAG_DISCLAIMER = "disclaimer";
    private static final int[] mTabIconList = {R.drawable.ic_star_white, R.drawable.ic_search_white,
            R.drawable.ic_settings_white};
    private ViewPager mViewPager;
    private InterstitialAd mInterstitialAd;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar(R.id.toolbar, false);
        TextView title = (TextView) findViewById(R.id.toolbar_text);
        title.setText(R.string.app_name);

        mSharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        if (!mSharedPreferences.getBoolean(TAG_DISCLAIMER, false)) {
            showDisclaimer(true);
        }

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest.Builder builder = new AdRequest.Builder();
        if (BuildConfig.DEBUG){
            builder.addTestDevice("3AD737A018BB67E7108FD1836E34DD1C");
        }
        mAdView.loadAd(builder.build());

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.admob_main_interstitial));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();

        mViewPager = (ViewPager) findViewById(R.id.pager_main);
        mViewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(this);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_main);
        tabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            //noinspection ConstantConditions
            tabLayout.getTabAt(i).setIcon(mTabIconList[i]);
        }
    }

    private void requestNewInterstitial() {
        AdRequest.Builder builder = new AdRequest.Builder();
        if (BuildConfig.DEBUG){
            builder.addTestDevice("3AD737A018BB67E7108FD1836E34DD1C");
        }
        mInterstitialAd.loadAd(builder.build());
    }

    public InterstitialAd getInterstitialAd() {
        return mInterstitialAd;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(mViewPager.getWindowToken(), 0);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                Intent intent = new Intent(this, AboutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.menu_disclaimer:
                showDisclaimer(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDisclaimer(boolean firstTime) {
        DisclaimerDialog dialog = DisclaimerDialog.newInstance(firstTime);
        dialog.show(getSupportFragmentManager(), "disclaimer");
    }

    @Override
    public void onUnderstand() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(TAG_DISCLAIMER, true);
        editor.apply();
    }

    @Override
    public void onCancelled() {
        finish();
    }
}
