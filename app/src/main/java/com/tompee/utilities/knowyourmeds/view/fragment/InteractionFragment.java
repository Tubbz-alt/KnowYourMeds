package com.tompee.utilities.knowyourmeds.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.view.MedDetailActivity;
import com.tompee.utilities.knowyourmeds.view.adapter.StringListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InteractionFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private ListView mListView;
    private Map<String, Map<String, String>> mInteractionMap;

    public static InteractionFragment newInstance() {
        return new InteractionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interaction, container, false);
        mInteractionMap = ((MedDetailActivity) getActivity()).getMedicine().getInteractions();
        AppCompatSpinner spinner = (AppCompatSpinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter;
        if (mInteractionMap != null && !mInteractionMap.isEmpty()) {
            List<String> dropDownList = new ArrayList<>();
            dropDownList.add(getString(R.string.control_select));
            dropDownList.addAll(mInteractionMap.keySet());

            adapter = new ArrayAdapter<>(getContext(), android.R.layout.
                    simple_spinner_item, dropDownList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setOnItemSelectedListener(this);
        } else {
            List<String> dropDownList = new ArrayList<>();
            dropDownList.add(getString(R.string.tab_interaction));
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.
                    simple_spinner_item, dropDownList);
        }
        spinner.setAdapter(adapter);
        mListView = (ListView) view.findViewById(R.id.list);
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0) {
            return;
        }
        String key = new ArrayList<>(mInteractionMap.keySet()).get(i - 1);
        StringListAdapter adapter = new StringListAdapter(getContext(),
                new ArrayList<>(mInteractionMap.get(key).keySet()));
        mListView.setAdapter(adapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
