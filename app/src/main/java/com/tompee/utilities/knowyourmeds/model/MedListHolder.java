package com.tompee.utilities.knowyourmeds.model;

import android.widget.ImageView;
import android.widget.TextView;

public class MedListHolder extends ListSwipeHolder {
    private TextView mTextView;
    private ImageView mImageView;

    public TextView getTextView() {
        return mTextView;
    }

    public void setTextView(TextView textView) {
        mTextView = textView;
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public void setImageView(ImageView resource) {
        mImageView = resource;
    }
}
