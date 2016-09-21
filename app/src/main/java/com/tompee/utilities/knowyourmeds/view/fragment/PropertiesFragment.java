package com.tompee.utilities.knowyourmeds.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.KnowYourMedsApp;
import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.MedDetailActivity;
import com.tompee.utilities.knowyourmeds.view.adapter.StringListAdapter;

import java.util.List;

public class PropertiesFragment extends Fragment {
    private static final String TAG_SOURCES_VISIBILITY = "sources_visibility";

    private View mSourcesView;

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
            View inView = view.findViewById(R.id.ingredients);
            inView.setVisibility(View.GONE);
        } else {
            inIcon.setBackgroundResource(R.drawable.ic_brand);
            inText.setText(R.string.property_brands);
            ListView listView = (ListView) view.findViewById(R.id.list_ingredients);
            listView.setAdapter(new StringListAdapter(getContext(), med.getIngredients(),
                    R.drawable.ic_ingredient));
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
        mSourcesView = view.findViewById(R.id.sources);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_property, menu);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(KnowYourMedsApp.
                SHARED_PREF_NAME, Context.MODE_PRIVATE);

        MenuItem show = menu.findItem(R.id.menu_sources_on);
        MenuItem hide = menu.findItem(R.id.menu_sources_off);
        if (sharedPreferences.getBoolean(TAG_SOURCES_VISIBILITY, true)) {
            show.setVisible(false);
            hide.setVisible(true);
        } else {
            show.setVisible(true);
            hide.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(KnowYourMedsApp.
                SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (item.getItemId()) {
            case R.id.menu_sources_on:
                mSourcesView.setVisibility(View.VISIBLE);
                editor.putBoolean(TAG_SOURCES_VISIBILITY, true);
                break;
            case R.id.menu_sources_off:
                mSourcesView.setVisibility(View.GONE);
                editor.putBoolean(TAG_SOURCES_VISIBILITY, false);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        editor.apply();
        getActivity().invalidateOptionsMenu();
        return true;
    }
}
