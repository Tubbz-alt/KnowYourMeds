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

public class ScdcFragment extends Fragment {
    private static ScdcFragment mSingleton;

    public static ScdcFragment getInstance() {
        if (mSingleton == null) {
            mSingleton = new ScdcFragment();
        }
        return mSingleton;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scdc, container, false);
        Medicine med = ((MedDetailActivity) getActivity()).getMedicine();
        List<String> scdcList = med.getScdc();
        View emptySource = view.findViewById(R.id.scdc_no_items);
        if (scdcList == null || scdcList.isEmpty()) {
            emptySource.setVisibility(View.VISIBLE);
        } else {
            ListView listView = (ListView) view.findViewById(R.id.list_scdc);
            listView.setAdapter(new StringListAdapter(getContext(), scdcList));
            emptySource.setVisibility(View.GONE);
            TextView countView = (TextView) view.findViewById(R.id.count);
            countView.setText(String.valueOf(scdcList.size()));
        }
        return view;
    }
}
