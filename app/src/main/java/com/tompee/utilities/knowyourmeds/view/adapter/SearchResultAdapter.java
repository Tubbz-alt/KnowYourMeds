package com.tompee.utilities.knowyourmeds.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.model.Medicine;

import java.util.List;

public class SearchResultAdapter extends ArrayAdapter<Medicine> {
    private final Context mContext;

    public SearchResultAdapter(Context context, int resource, List<Medicine> medList) {
        super(context, resource, medList);
        mContext = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_search_result, parent, false);
        }

        TextView name = (TextView) view.findViewById(R.id.med_name);
        name.setText(getItem(position).getName());
        return view;
    }
}
