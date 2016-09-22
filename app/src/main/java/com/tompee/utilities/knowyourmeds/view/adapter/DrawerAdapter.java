package com.tompee.utilities.knowyourmeds.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.model.DrawerItemHolder;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerItemHolder> {
    private final int[] mOptionIds;

    public DrawerAdapter(int[] optionIds) {
        mOptionIds = optionIds;
    }

    @Override
    public DrawerItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == DrawerItemHolder.HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header,
                    parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_option,
                    parent, false);
        }
        return new DrawerItemHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(DrawerItemHolder holder, int position) {
        if (holder.getType() == DrawerItemHolder.HEADER) {
            /* TODO add header items */
        } else {
            holder.getOptionText().setText(mOptionIds[position - 1]);
        }
    }

    @Override
    public int getItemCount() {
        return mOptionIds.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return DrawerItemHolder.HEADER;
        }
        return DrawerItemHolder.OPTIONS;
    }
}
