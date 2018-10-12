package com.tompee.utilities.knowyourmeds.view.adapter;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.Utilities;
import com.tompee.utilities.knowyourmeds.model.ListSwipeHolder;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.feature.search.persist.SwipeListItemView;
import com.tompee.utilities.knowyourmeds.view.custom.TextDrawable;

import java.util.List;

public class MedListAdapter extends ArrayAdapter<Medicine> implements SwipeListItemView.SwipeListItemViewListener {
    private final Context mContext;
    private final boolean mSwipeable;

    public MedListAdapter(Context context, List<Medicine> medList, boolean swipeable) {
        super(context, R.layout.list_main, medList);
        mContext = context;
        mSwipeable = swipeable;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ListSwipeHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_main, parent, false);
            holder = new ListSwipeHolder();
            holder.setFrontView(view.findViewById(R.id.front_view));
            holder.setBackView(view.findViewById(R.id.back_view));
            view.setTag(holder);
        } else {
            holder = (ListSwipeHolder) view.getTag();
        }
        holder.getBackView().setVisibility(View.GONE);
        ((SwipeListItemView) view).setEnableSwipeDetection(mSwipeable);
        ((SwipeListItemView) view).setOnClickBackgroundColor(R.color.colorListBackground);

        Medicine med = getItem(position);
        TextView name = (TextView) view.findViewById(R.id.med_name);
        name.setText(med.getName());
        TextView tty = (TextView) view.findViewById(R.id.tty);
        tty.setText(Utilities.getTtyString(getContext(), med.getTty()));

        FloatingActionButton imageView = (FloatingActionButton) view.findViewById(R.id.list_icon);
        imageView.setImageDrawable(new TextDrawable(getContext().getResources(),
                med.getName().substring(0, 1), false));
        return view;
    }

    @Override
    public void onBackViewClicked(int position) {
    }
}
