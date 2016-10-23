package com.tompee.utilities.knowyourmeds.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.view.MedDetailActivity;
import com.tompee.utilities.knowyourmeds.view.adapter.StringListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InteractionFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        AdapterView.OnItemClickListener {
    private ListView mListView;
    private Map<String, Map<String, String>> mInteractionMap;
    private String mKey;

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
            spinner.setEnabled(false);
            TextView tv = (TextView) view.findViewById(R.id.tv_no_items);
            tv.setText(String.format(getString(R.string.tty_no_items),
                    getString(R.string.tab_interaction)));
            tv.setVisibility(View.VISIBLE);
        }
        spinner.setAdapter(adapter);
        mListView = (ListView) view.findViewById(R.id.list);
        mListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0) {
            mListView.setAdapter(null);
            return;
        }
        mKey = new ArrayList<>(mInteractionMap.keySet()).get(i - 1);
        StringListAdapter adapter = new StringListAdapter(getContext(),
                new ArrayList<>(mInteractionMap.get(mKey).keySet()), "");
        mListView.setAdapter(adapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String key = new ArrayList<>(mInteractionMap.get(mKey).keySet()).get(i);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(key);
        builder.setMessage(mInteractionMap.get(mKey).get(key));
        builder.setPositiveButton(R.string.control_ok, null);
        builder.create().show();
    }
}
