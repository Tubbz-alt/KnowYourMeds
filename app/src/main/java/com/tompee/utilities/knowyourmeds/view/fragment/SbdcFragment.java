package com.tompee.utilities.knowyourmeds.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.MedDetailActivity;
import com.tompee.utilities.knowyourmeds.view.adapter.StringListAdapter;

import java.util.List;

public class SbdcFragment extends Fragment {
    private static SbdcFragment mSingleton;

    public static SbdcFragment getInstance() {
        if (mSingleton == null) {
            mSingleton = new SbdcFragment();
        }
        return mSingleton;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sbdc, container, false);
        Medicine med = ((MedDetailActivity) getActivity()).getMedicine();
        List<String> sbdcList = med.getSbdc();
        View emptySource = view.findViewById(R.id.sbdc_no_items);
        if (sbdcList == null || sbdcList.isEmpty()) {
            emptySource.setVisibility(View.VISIBLE);
        } else {
            ListView listView = (ListView) view.findViewById(R.id.list_sbdc);
            listView.setAdapter(new StringListAdapter(getContext(), sbdcList));
            emptySource.setVisibility(View.GONE);
        }
        return view;
    }
}
