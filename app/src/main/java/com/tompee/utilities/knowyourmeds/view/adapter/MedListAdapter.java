package com.tompee.utilities.knowyourmeds.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.model.Medicine;

import java.util.List;

public class MedListAdapter extends ArrayAdapter<Medicine> {
    private final Context mContext;
    private final boolean mWithIcon;

    public MedListAdapter(Context context, List<Medicine> medList, boolean withIcon) {
        super(context, R.layout.list_med, medList);
        mContext = context;
        mWithIcon = withIcon;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_med, parent, false);
        }

        TextView name = (TextView) view.findViewById(R.id.med_name);
        name.setText(getItem(position).getName());

        ImageView image = (ImageView) view.findViewById(R.id.presc_icon);
        if (mWithIcon) {
            if (getItem(position).isPrescribable()) {
                image.setBackgroundResource(R.drawable.ic_rx_on);
            }
        } else {
            image.setVisibility(View.GONE);
        }
        return view;
    }
}
