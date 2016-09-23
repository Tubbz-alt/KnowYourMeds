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

import java.util.Collections;
import java.util.List;

public class BrandFragment extends Fragment {
    private static BrandFragment mSingleton;

    public static BrandFragment getInstance() {
        if (mSingleton == null) {
            mSingleton = new BrandFragment();
        }
        return mSingleton;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brands, container, false);
        Medicine med = ((MedDetailActivity) getActivity()).getMedicine();
        List<String> brandList = med.getBrands();
        View emptySource = view.findViewById(R.id.brands_no_items);
        if (brandList == null || brandList.isEmpty()) {
            emptySource.setVisibility(View.VISIBLE);
        } else {
            Collections.sort(brandList);
            ListView listView = (ListView) view.findViewById(R.id.list_brands);
            listView.setAdapter(new StringListAdapter(getContext(), brandList));
            emptySource.setVisibility(View.GONE);
            TextView countView = (TextView) view.findViewById(R.id.count);
            countView.setText(String.valueOf(brandList.size()));
        }
        return view;
    }
}
