package com.tompee.utilities.knowyourmeds.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.Utilities;
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
        TextView prescText = (TextView) view.findViewById(R.id.presc_text);
        if (med.isPrescribable()) {
            prescText.setText(R.string.property_prescribable_yes);
        } else {
            prescText.setText(R.string.property_prescribable_no);
        }
        TextView inText = (TextView) view.findViewById(R.id.in_text);
        inText.setText(Utilities.getTtyString(getContext(), med.getTty()));
        ArrayList<String> ingredients = med.getIngredients();
        if (ingredients != null && !ingredients.isEmpty()) {
            final ArrayList<String> inList = med.getIngredients();
            ListView listView = (ListView) view.findViewById(R.id.list_ingredients);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(String.format(getString(R.string.search_item_message), inList.get(position)));
                    builder.setNegativeButton(R.string.control_cancel, null);
                    builder.setPositiveButton(R.string.control_search, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getContext(), MedDetailActivity.class);
                            intent.putExtra(MedDetailActivity.TAG_NAME, inList.get(position));
                            intent.putExtra(MedDetailActivity.TAG_ID, "");
                            intent.putExtra(MedDetailActivity.TAG_ORIGIN, MedDetailActivity.FROM_SEARCH);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    builder.create().show();
                }
            });
            listView.setAdapter(new StringListAdapter(getContext(), inList, true,
                    getString(R.string.property_ingredient)));
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
                    new ArrayList<>(mSplMap.values()), true, getString(R.string.tab_spl)));
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
