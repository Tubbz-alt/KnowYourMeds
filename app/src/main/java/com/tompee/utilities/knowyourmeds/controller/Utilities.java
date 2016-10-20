package com.tompee.utilities.knowyourmeds.controller;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.networkinterface.RxNavWrapper;

import java.io.IOException;
import java.io.InputStream;

public class Utilities {

    public static int convertDPtoPixel(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static Drawable getDrawableFromAsset(Context context, String filename) {
        Drawable drawable;
        try {
            InputStream ims = context.getAssets().open(filename);
            drawable = Drawable.createFromStream(ims, null);
        } catch (IOException ex) {
            drawable = null;
        }
        return drawable;
    }
    
    
    public static String getTtyString(Context context, String tty) {
        switch (tty) {
            case RxNavWrapper.BRAND:
                tty = context.getString(R.string.property_brands);
                break;
            case RxNavWrapper.INGREDIENT:
                tty = context.getString(R.string.property_ingredient);
                break;
            case RxNavWrapper.SCDC:
                tty = context.getString(R.string.tab_scdc);
                break;
            case RxNavWrapper.SBDC:
                tty = context.getString(R.string.tab_sbdc);
                break;
            case RxNavWrapper.SBD:
                tty = context.getString(R.string.tab_sbd);
                break;
            case RxNavWrapper.SBDG:
                tty = context.getString(R.string.tab_sbdg);
                break;
            case RxNavWrapper.SCD:
                tty = context.getString(R.string.tab_scd);
                break;
            case RxNavWrapper.SCDG:
                tty = context.getString(R.string.tab_scdg);
                break;
        }
        return tty;
    }
}
