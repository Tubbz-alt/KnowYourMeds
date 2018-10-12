package com.tompee.utilities.knowyourmeds.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.tompee.utilities.knowyourmeds.BuildConfig;
import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.Utilities;
import com.tompee.utilities.knowyourmeds.view.adapter.MainViewPagerAdapter;
import com.tompee.utilities.knowyourmeds.view.base.BaseActivity;
import com.tompee.utilities.knowyourmeds.view.fragment.SearchFragment;

public class MainActivity extends BaseActivity {
    public static final String SHARED_PREF = "knowyourmedspref";
    public static final String TAG_DISCLAIMER = "disclaimer";
    private static final String LAUNCH_COUNT = "launch_count";
    private static final int MIN_LAUNCH_COUNT = 7;

    private InterstitialAd mInterstitialAd;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar(R.id.toolbarInclude, false);
        TextView title = (TextView) findViewById(R.id.toolbar_text);
        title.setText(R.string.app_name);

        ImageView background = (ImageView) findViewById(R.id.background);
        background.setImageDrawable(Utilities.getDrawableFromAsset(this, "search_bg.jpg"));

        mSharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        int launchCount = mSharedPreferences.getInt(LAUNCH_COUNT, 0);
        if (launchCount == MIN_LAUNCH_COUNT) {
            editor.putInt(LAUNCH_COUNT, 0);
            showAppRater();
        } else {
            editor.putInt(LAUNCH_COUNT, ++launchCount);
        }
        editor.apply();

        if (!mSharedPreferences.getBoolean(TAG_DISCLAIMER, false)) {
            showDisclaimer(true);
        }

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.admob_main_interstitial));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, SearchFragment.newInstance());
        transaction.commit();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MainViewPagerAdapter(this, getSupportFragmentManager()));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void showAppRater() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.ids_title_rate);
        builder.setMessage(R.string.ids_message_rate);
        builder.setNeutralButton(R.string.ids_lbl_remind, null);
        builder.setNegativeButton(R.string.ids_lbl_no_rate, null);
        builder.setPositiveButton(R.string.ids_lbl_yes_rate, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" +
                        BuildConfig.APPLICATION_ID));
                startActivity(intent);
            }
        });
        builder.create().show();
    }

    private void requestNewInterstitial() {
        AdRequest.Builder builder = new AdRequest.Builder();
        if (BuildConfig.DEBUG) {
            builder.addTestDevice("3AD737A018BB67E7108FD1836E34DD1C");
        }
        mInterstitialAd.loadAd(builder.build());
    }

    public InterstitialAd getInterstitialAd() {
        return mInterstitialAd;
    }

    private void showDisclaimer(boolean firstTime) {
//        DisclaimerDialog dialog = DisclaimerDialog.newInstance(firstTime);
//        dialog.show(getSupportFragmentManager(), "disclaimer");
    }

//    @Override
//    public void onUnderstand() {
//        SharedPreferences.Editor editor = mSharedPreferences.edit();
//        editor.putBoolean(TAG_DISCLAIMER, true);
//        editor.apply();
//    }
//
//    @Override
//    public void onCancelled() {
//        finish();
//    }
}
