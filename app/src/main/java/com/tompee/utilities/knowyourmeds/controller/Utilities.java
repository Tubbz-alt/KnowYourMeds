package com.tompee.utilities.knowyourmeds.controller;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;

public class Utilities {
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
}
