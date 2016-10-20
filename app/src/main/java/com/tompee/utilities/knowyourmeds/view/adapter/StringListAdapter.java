package com.tompee.utilities.knowyourmeds.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.model.ListSwipeHolder;
import com.tompee.utilities.knowyourmeds.view.custom.SwipeListItemView;

import java.util.List;

public class StringListAdapter extends ArrayAdapter<String> implements
        SwipeListItemView.SwipeListItemViewListener {
    private final Context mContext;
    private final int mIconResource;

    public StringListAdapter(Context context, List<String> stringList) {
        super(context, R.layout.list_plain, stringList);
        mContext = context;
        mIconResource = 0;
    }

    public StringListAdapter(Context context, List<String> stringList, int icon) {
        super(context, R.layout.list_plain, stringList);
        mContext = context;
        mIconResource = icon;
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

        ImageView image = (ImageView) view.findViewById(R.id.icon);
        if (mIconResource == 0) {
            image.setVisibility(View.GONE);
        } else {
            image.setBackgroundResource(mIconResource);
        }

        return view;
    }

    @Override
    public void onBackViewClicked(int position) {
    }
}
