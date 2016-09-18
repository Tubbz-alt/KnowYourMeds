package com.tompee.utilities.knowyourmeds.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.MedDetailActivity;
import com.tompee.utilities.knowyourmeds.view.adapter.StringListAdapter;

import java.util.List;

public class PropertiesFragment extends Fragment {

    public static PropertiesFragment newInstance() {
        return new PropertiesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_properties, container, false);
        Medicine med = ((MedDetailActivity) getActivity()).getMedicine();
        ImageView prescIcon = (ImageView) view.findViewById(R.id.presc_icon);
        TextView prescText = (TextView) view.findViewById(R.id.presc_text);
        if (med.isPrescribable()) {
            prescIcon.setBackgroundResource(R.drawable.ic_rx_on);
            prescText.setText(R.string.property_prescribable_yes);
        } else {
            prescIcon.setBackgroundResource(R.drawable.ic_rx_off);
            prescText.setText(R.string.property_prescribable_no);
        }
        ImageView inIcon = (ImageView) view.findViewById(R.id.in_icon);
        TextView inText = (TextView) view.findViewById(R.id.in_text);
        if (med.isIngredient()) {
            inIcon.setBackgroundResource(R.drawable.ic_ingredient);
            inText.setText(R.string.property_ingredient);
        } else {
            inIcon.setBackgroundResource(R.drawable.ic_brand);
            inText.setText(R.string.property_brands);
        }
        List<String> sources = med.getSources();
        View emptySource = view.findViewById(R.id.source_no_items);
        if (sources == null || sources.isEmpty()) {
            emptySource.setVisibility(View.VISIBLE);
        } else {
            ListView listView = (ListView) view.findViewById(R.id.list_sources);
            listView.setAdapter(new StringListAdapter(getContext(), sources));
            emptySource.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_property, menu);
    }

}
