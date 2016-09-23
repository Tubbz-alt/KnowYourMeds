package com.tompee.utilities.knowyourmeds.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.MedDetailActivity;
import com.tompee.utilities.knowyourmeds.view.adapter.StringListAdapter;

import java.util.List;

public class ScdFragment extends Fragment {
    private static ScdFragment mSingleton;

    public static ScdFragment getInstance() {
        if (mSingleton == null) {
            mSingleton = new ScdFragment();
        }
        return mSingleton;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scd, container, false);
        Medicine med = ((MedDetailActivity) getActivity()).getMedicine();
        List<String> scdList = med.getScd();
        View emptySource = view.findViewById(R.id.scd_no_items);
        if (scdList == null || scdList.isEmpty()) {
            emptySource.setVisibility(View.VISIBLE);
        } else {
            ListView listView = (ListView) view.findViewById(R.id.list_scd);
            listView.setAdapter(new StringListAdapter(getContext(), scdList));
            emptySource.setVisibility(View.GONE);
            TextView countView = (TextView) view.findViewById(R.id.count);
            countView.setText(String.valueOf(scdList.size()));
        }
        return view;
    }
}
