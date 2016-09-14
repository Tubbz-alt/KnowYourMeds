package com.tompee.utilities.knowyourmeds.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tompee.utilities.knowyourmeds.R;

public class PropertiesFragment extends Fragment {
    private static final String TAG_ID = "id";
    private String mRxcui;

    public static PropertiesFragment newInstance(String rxcui) {
        Bundle args = new Bundle();
        args.putString(TAG_ID, rxcui);
        PropertiesFragment fragment = new PropertiesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxcui = getArguments().getString(TAG_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_properties, container, false);
        return view;
    }
}
