package com.tompee.utilities.knowyourmeds.controller;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.networkinterface.RxNavWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

    public static String getStringFromAsset(Context context, String filename) {
        StringBuilder buffer = new StringBuilder();
        InputStream inputStream;
        try {
            inputStream = context.getAssets().open(filename);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public static String getTtyString(Context context, String tty) {
        switch (tty) {
            case RxNavWrapper.BRAND:
                tty = context.getString(R.string.property_brands);
                break;
            case RxNavWrapper.MIN:
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
            case RxNavWrapper.PIN:
                tty = context.getString(R.string.tab_pin);
                break;
        }
        return tty;
    }
}
