package com.tompee.utilities.knowyourmeds.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.database.DatabaseHelper;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.adapter.MedListAdapter;

import java.util.List;

public class RecentFavoriteFragment extends Fragment implements AdapterView.OnItemClickListener {
    private DatabaseHelper mDbHelper;
    private ListView mFavoriteListView;
    private ListView mRecentListView;
    private View mFaveNoItemsView;
    private View mRecentNoItemsView;

    public static RecentFavoriteFragment newInstance() {
        return new RecentFavoriteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new DatabaseHelper(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        updateLists();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            updateLists();
        }
    }

    private void updateLists() {
        if (mDbHelper != null) {
            List<Medicine> favoriteMedList = mDbHelper.getAllEntries(DatabaseHelper.FAVORITE_TABLE);
            if (favoriteMedList.size() > 0) {
                MedListAdapter adapter = new MedListAdapter(getContext(), favoriteMedList, true);
                mFavoriteListView.setAdapter(adapter);
                mFavoriteListView.setVisibility(View.VISIBLE);
                mFaveNoItemsView.setVisibility(View.GONE);
            } else {
                mFavoriteListView.setVisibility(View.GONE);
                mFaveNoItemsView.setVisibility(View.VISIBLE);
            }

            List<Medicine> recentMedList = mDbHelper.getAllEntries(DatabaseHelper.RECENT_TABLE);
            if (recentMedList.size() > 0) {
                MedListAdapter adapter = new MedListAdapter(getContext(), recentMedList, true);
                mRecentListView.setAdapter(adapter);
                mRecentListView.setVisibility(View.VISIBLE);
                mRecentNoItemsView.setVisibility(View.GONE);
            } else {
                mRecentListView.setVisibility(View.GONE);
                mRecentNoItemsView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_favorite, container, false);
        mFavoriteListView = (ListView) view.findViewById(R.id.list_favorite);
        mFavoriteListView.setOnItemClickListener(this);
        mRecentListView = (ListView) view.findViewById(R.id.recent_list);
        mRecentListView.setOnItemClickListener(this);
        mFaveNoItemsView = view.findViewById(R.id.fave_no_items);
        mRecentNoItemsView = view.findViewById(R.id.recent_no_items);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
