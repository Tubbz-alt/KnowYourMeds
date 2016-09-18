package com.tompee.utilities.knowyourmeds.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;

import java.util.List;

public class StringListAdapter extends ArrayAdapter<String> {
    private final Context mContext;

    public StringListAdapter(Context context, List<String> stringList) {
        super(context, R.layout.list_med, stringList);
        mContext = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_med, parent, false);
        }

        TextView name = (TextView) view.findViewById(R.id.med_name);
        name.setText(getItem(position));

        ImageView image = (ImageView) view.findViewById(R.id.presc_icon);
        image.setVisibility(View.GONE);
        return view;
    }
}
