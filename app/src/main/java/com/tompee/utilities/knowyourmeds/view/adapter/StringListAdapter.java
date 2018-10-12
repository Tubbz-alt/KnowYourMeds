package com.tompee.utilities.knowyourmeds.view.adapter;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.model.ListSwipeHolder;
import com.tompee.utilities.knowyourmeds.feature.search.persist.SwipeListItemView;
import com.tompee.utilities.knowyourmeds.view.custom.TextDrawable;

import java.util.List;

public class StringListAdapter extends ArrayAdapter<String> implements
        SwipeListItemView.SwipeListItemViewListener {
    private final Context mContext;
    private final boolean mWithDrawable;
    private final String mSubtext;

    public StringListAdapter(Context context, List<String> stringList, String subtext) {
        super(context, R.layout.list_plain, stringList);
        mContext = context;
        mWithDrawable = false;
        mSubtext = subtext;
    }

    public StringListAdapter(Context context, List<String> stringList, boolean withDrawable,
                             String subtext) {
        super(context, R.layout.list_plain, stringList);
        mContext = context;
        mWithDrawable = withDrawable;
        mSubtext = subtext;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ListSwipeHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_plain, parent, false);
            holder = new ListSwipeHolder();
            holder.setFrontView(view.findViewById(R.id.front_view));
            holder.setBackView(view.findViewById(R.id.back_view));
            view.setTag(holder);
        } else {
            holder = (ListSwipeHolder) view.getTag();
        }
        holder.getBackView().setVisibility(View.GONE);
        ((SwipeListItemView) view).setEnableSwipeDetection(false);
        ((SwipeListItemView) view).setOnClickBackgroundColor(R.color.colorListBackground);

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(getItem(position));

        FloatingActionButton image = (FloatingActionButton) view.findViewById(R.id.icon);
        if (!mWithDrawable) {
            image.setVisibility(View.GONE);
        } else {
            image.setImageDrawable(new TextDrawable(getContext().getResources(),
                    getItem(position).substring(0, 1), false));
        }

        TextView subtext = (TextView) view.findViewById(R.id.subtext);
        if (mSubtext == null) {
            subtext.setVisibility(View.GONE);
        } else {
            subtext.setText(mSubtext);
        }
        return view;
    }

    @Override
    public void onBackViewClicked(int position) {
    }
}
