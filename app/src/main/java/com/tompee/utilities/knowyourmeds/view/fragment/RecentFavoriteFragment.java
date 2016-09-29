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

import com.google.android.gms.ads.InterstitialAd;
import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.database.DatabaseHelper;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.MainActivity;
import com.tompee.utilities.knowyourmeds.view.MedDetailActivity;
import com.tompee.utilities.knowyourmeds.view.adapter.MedListAdapter;
import com.tompee.utilities.knowyourmeds.view.custom.SwipeListView;

import java.util.Collections;
import java.util.List;

public class RecentFavoriteFragment extends Fragment implements View.OnClickListener {
    private DatabaseHelper mDbHelper;

    private SwipeListView mFavoriteListView;
    private SwipeListView mRecentListView;
    private List<Medicine> mFavoriteMedList;
    private List<Medicine> mRecentMedList;

    private View mFaveNoItemsView;
    private View mRecentNoItemsView;
    private View mRecentTrash;
    private View mFavoriteTrash;

    public static RecentFavoriteFragment newInstance() {
        return new RecentFavoriteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new DatabaseHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_favorite, container, false);
        mFavoriteListView = (SwipeListView) view.findViewById(R.id.list_favorite);
        mFavoriteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                InterstitialAd ad = ((MainActivity) getActivity()).getInterstitialAd();
                startMedListActivity(mFavoriteMedList.get(i));
                if (ad.isLoaded()) {
                    ad.show();
                }
            }
        });
        mRecentListView = (SwipeListView) view.findViewById(R.id.recent_list);
        mRecentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                InterstitialAd ad = ((MainActivity) getActivity()).getInterstitialAd();
                startMedListActivity(mRecentMedList.get(i));
                if (ad.isLoaded()) {
                    ad.show();
                }
            }
        });
        mFaveNoItemsView = view.findViewById(R.id.fave_no_items);
        mRecentNoItemsView = view.findViewById(R.id.recent_no_items);
        mRecentTrash = view.findViewById(R.id.image_recent_trash);
        mRecentTrash.setOnClickListener(this);
        mFavoriteTrash = view.findViewById(R.id.image_favorite_trash);
        mFavoriteTrash.setOnClickListener(this);
        return view;
    }

    private void startMedListActivity(Medicine med) {
        Intent intent = new Intent(getContext(), MedDetailActivity.class);
        intent.putExtra(MedDetailActivity.TAG_NAME, med.getName());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
        } else {
            closeAllOpenListItems();
        }
    }

    private void updateLists() {
        if (mDbHelper != null) {
            mFavoriteMedList = mDbHelper.getAllEntries(DatabaseHelper.FAVORITE_TABLE);
            if (mFavoriteMedList.size() > 0) {
                MedListAdapter adapter = new MedListAdapter(getContext(), mFavoriteMedList, true, true);
                View view = mFavoriteListView.getChildAt(0);
                int position = mFavoriteListView.getFirstVisiblePosition();
                mFavoriteListView.setAdapter(adapter);
                if (view == null) {
                    mFavoriteListView.setSelectionFromTop(position, 0);
                } else {
                    mFavoriteListView.setSelectionFromTop(position, view.getTop());
                }

                mFavoriteListView.setVisibility(View.VISIBLE);
                mFaveNoItemsView.setVisibility(View.GONE);
                mFavoriteTrash.setVisibility(View.VISIBLE);
            } else {
                mFavoriteListView.setVisibility(View.GONE);
                mFaveNoItemsView.setVisibility(View.VISIBLE);
                mFavoriteTrash.setVisibility(View.GONE);
            }

            mRecentMedList = mDbHelper.getAllEntries(DatabaseHelper.RECENT_TABLE);
            if (mRecentMedList.size() > 0) {
                Collections.reverse(mRecentMedList);
                MedListAdapter adapter = new MedListAdapter(getContext(), mRecentMedList, true, true);
                View view = mRecentListView.getChildAt(0);
                int position = mRecentListView.getFirstVisiblePosition();
                mRecentListView.setAdapter(adapter);
                if (view == null) {
                    mRecentListView.setSelectionFromTop(position, 0);
                } else {
                    mRecentListView.setSelectionFromTop(position, view.getTop());
                }

                mRecentListView.setVisibility(View.VISIBLE);
                mRecentNoItemsView.setVisibility(View.GONE);
                mRecentTrash.setVisibility(View.VISIBLE);
            } else {
                mRecentListView.setVisibility(View.GONE);
                mRecentNoItemsView.setVisibility(View.VISIBLE);
                mRecentTrash.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.control_delete);
        if (view.equals(mRecentTrash)) {
            builder.setMessage(R.string.delete_recent_message);
            builder.setPositiveButton(R.string.control_delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mDbHelper.deleteAll(DatabaseHelper.RECENT_TABLE);
                    updateLists();
                }
            });
        } else {
            builder.setMessage(R.string.delete_favorite_message);
            builder.setPositiveButton(R.string.control_delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mDbHelper.deleteAll(DatabaseHelper.FAVORITE_TABLE);
                    updateLists();
                }
            });
        }
        builder.setNegativeButton(R.string.control_cancel, null);
        builder.create().show();
    }

    private void closeAllOpenListItems() {
        if (mFavoriteListView != null) {
            mFavoriteListView.resetVisibleView();
        }
        if (mRecentListView != null) {
            mRecentListView.resetVisibleView();
        }
    }
}
