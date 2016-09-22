package com.tompee.utilities.knowyourmeds.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.MedDetailActivity;
import com.tompee.utilities.knowyourmeds.view.adapter.StringListAdapter;

public class PropertiesFragment extends Fragment {
    private static PropertiesFragment mSingleton;

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
        if (med.isIngredient()) {
            inIcon.setBackgroundResource(R.drawable.ic_ingredient);
            inText.setText(R.string.property_ingredient);
            View inView = view.findViewById(R.id.ingredients);
            inView.setVisibility(View.GONE);
        } else {
            inIcon.setBackgroundResource(R.drawable.ic_brand);
            inText.setText(R.string.property_brands);
            ListView listView = (ListView) view.findViewById(R.id.list_ingredients);
            listView.setAdapter(new StringListAdapter(getContext(), med.getIngredients(),
                    R.drawable.ic_ingredient));
        }
        return view;
    }
}
