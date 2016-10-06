package com.tompee.utilities.knowyourmeds.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.networkinterface.RxNavWrapper;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.MedDetailActivity;
import com.tompee.utilities.knowyourmeds.view.SPLDetailActivity;
import com.tompee.utilities.knowyourmeds.view.adapter.StringListAdapter;

import java.util.ArrayList;
import java.util.Map;

public class PropertiesFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String DAILY_MED_BASE_URL = "https://dailymed.nlm.nih.gov/dailymed/drugInfo.cfm?setid=";
    private static PropertiesFragment mSingleton;
    private Map<String, String> mSplMap;

    public static PropertiesFragment getInstance() {
        if (mSingleton == null) {
            mSingleton = new PropertiesFragment();
        }
        return mSingleton;
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
        switch (med.getTty()) {
            case RxNavWrapper.BRAND:
                inIcon.setBackgroundResource(R.drawable.shape_tty_brand);
                inText.setText(R.string.property_brands);
                break;
            case RxNavWrapper.INGREDIENT:
                inIcon.setBackgroundResource(R.drawable.shape_tty_ingredients);
                inText.setText(R.string.property_ingredient);
                break;
            case RxNavWrapper.SCDC:
                inIcon.setBackgroundResource(R.drawable.shape_tty_scdc);
                inText.setText(R.string.tab_scdc);
                break;
            case RxNavWrapper.SBDC:
                inIcon.setBackgroundResource(R.drawable.shape_tty_sbdc);
                inText.setText(R.string.tab_sbdc);
                break;
            case RxNavWrapper.SBD:
                inIcon.setBackgroundResource(R.drawable.shape_tty_sbd);
                inText.setText(R.string.tab_sbd);
                break;
            case RxNavWrapper.SBDG:
                inIcon.setBackgroundResource(R.drawable.shape_tty_sbdg);
                inText.setText(R.string.tab_sbdg);
                break;
            case RxNavWrapper.SCD:
                inIcon.setBackgroundResource(R.drawable.shape_tty_scd);
                inText.setText(R.string.tab_scd);
                break;
            case RxNavWrapper.SCDG:
                inIcon.setBackgroundResource(R.drawable.shape_tty_scdg);
                inText.setText(R.string.tab_scdg);
                break;
        }
        ArrayList<String> ingredients = med.getIngredients();
        if (ingredients != null && !ingredients.isEmpty()) {
            ListView listView = (ListView) view.findViewById(R.id.list_ingredients);
            listView.setAdapter(new StringListAdapter(getContext(), med.getIngredients(),
                    R.drawable.shape_tty_ingredients));
        } else {
            View inView = view.findViewById(R.id.ingredients);
            inView.setVisibility(View.GONE);
        }
        View splView = view.findViewById(R.id.spls);
        mSplMap = med.getSplSetId();
        if (mSplMap == null || mSplMap.isEmpty()) {
            splView.setVisibility(View.INVISIBLE);
        } else {
            ListView listView = (ListView) view.findViewById(R.id.list_spls);
            listView.setAdapter(new StringListAdapter(getContext(),
                    new ArrayList<>(mSplMap.values()), 0));
            listView.setOnItemClickListener(this);
            TextView count = (TextView) view.findViewById(R.id.count);
            count.setText(String.valueOf(mSplMap.size()));
        }
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mSplMap != null) {
            Intent intent = new Intent(getContext(), SPLDetailActivity.class);
            String setId = new ArrayList<>(mSplMap.keySet()).get(i);
            intent.putExtra(SPLDetailActivity.NAME, mSplMap.get(setId));
            intent.putExtra(SPLDetailActivity.URL, DAILY_MED_BASE_URL + setId);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
