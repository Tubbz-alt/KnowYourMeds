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

import java.util.Collections;
import java.util.List;

public class SbdgFragment extends Fragment {
    private static SbdgFragment mSingleton;

    public static SbdgFragment getInstance() {
        if (mSingleton == null) {
            mSingleton = new SbdgFragment();
        }
        return mSingleton;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sbdg, container, false);
        Medicine med = ((MedDetailActivity) getActivity()).getMedicine();
        List<String> sbdgList = med.getSbdg();
        View emptySource = view.findViewById(R.id.sbdg_no_items);
        if (sbdgList == null || sbdgList.isEmpty()) {
            emptySource.setVisibility(View.VISIBLE);
        } else {
            Collections.sort(sbdgList);
            ListView listView = (ListView) view.findViewById(R.id.list_sbdg);
            listView.setAdapter(new StringListAdapter(getContext(), sbdgList));
            emptySource.setVisibility(View.GONE);
        }
        return view;
    }
}
