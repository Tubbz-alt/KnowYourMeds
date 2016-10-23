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

public class SourceFragment extends Fragment {
    private static SourceFragment mSingleton;

    public static SourceFragment getInstance() {
        if (mSingleton == null) {
            mSingleton = new SourceFragment();
        }
        return mSingleton;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sources, container, false);
        Medicine med = ((MedDetailActivity) getActivity()).getMedicine();
        List<String> sources = med.getSources();
        View emptySource = view.findViewById(R.id.source_no_items);
        if (sources == null || sources.isEmpty()) {
            emptySource.setVisibility(View.VISIBLE);
        } else {
            ListView listView = (ListView) view.findViewById(R.id.list_sources);
            listView.setAdapter(new StringListAdapter(getContext(), sources, ""));
            emptySource.setVisibility(View.GONE);
        }
        return view;
    }
}
