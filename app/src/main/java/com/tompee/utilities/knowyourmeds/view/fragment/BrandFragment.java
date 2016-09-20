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

public class BrandFragment extends Fragment {
    private List<String> mBrandList;

    public static BrandFragment newInstance() {
        return new BrandFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brands, container, false);
        Medicine med = ((MedDetailActivity) getActivity()).getMedicine();
        mBrandList = med.getBrands();
        Collections.sort(mBrandList);
        View emptySource = view.findViewById(R.id.brands_no_items);
        if (mBrandList == null || mBrandList.isEmpty()) {
            emptySource.setVisibility(View.VISIBLE);
        } else {
            ListView listView = (ListView) view.findViewById(R.id.list_brands);
            listView.setAdapter(new StringListAdapter(getContext(), mBrandList));
            emptySource.setVisibility(View.GONE);
        }
        return view;
    }
}
