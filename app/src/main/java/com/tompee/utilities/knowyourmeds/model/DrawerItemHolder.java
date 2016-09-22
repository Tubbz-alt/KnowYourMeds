package com.tompee.utilities.knowyourmeds.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;

public class DrawerItemHolder extends RecyclerView.ViewHolder {
    public static final int HEADER = 0;
    public static final int OPTIONS = 1;

    private final TextView mOptionText;
    private final int mType;

    public DrawerItemHolder(View itemView, int viewType) {
        super(itemView);

        if (viewType == HEADER) {
            mOptionText = null;
        } else {
            mOptionText = (TextView) itemView.findViewById(R.id.option_text);
        }
        mType = viewType;
    }

    public TextView getOptionText() {
        return mOptionText;
    }

    public int getType() {
        return mType;
    }
}
