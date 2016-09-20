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

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.database.DatabaseHelper;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.MedDetailActivity;
import com.tompee.utilities.knowyourmeds.view.adapter.MedListAdapter;

import java.util.List;

public class RecentFavoriteFragment extends Fragment implements View.OnClickListener {
    private DatabaseHelper mDbHelper;

    private ListView mFavoriteListView;
    private ListView mRecentListView;
    private List<Medicine> mFavoriteMedList;
    private List<Medicine> mRecentMedList;

    private View mFaveNoItemsView;
    private View mRecentNoItemsView;
    private View mRecentTrash;

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
        mFavoriteListView = (ListView) view.findViewById(R.id.list_favorite);
        mFavoriteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startMedListActivity(mFavoriteMedList.get(i));
            }
        });
        mRecentListView = (ListView) view.findViewById(R.id.recent_list);
        mRecentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startMedListActivity(mRecentMedList.get(i));
            }
        });
        mFaveNoItemsView = view.findViewById(R.id.fave_no_items);
        mRecentNoItemsView = view.findViewById(R.id.recent_no_items);
        mRecentTrash = view.findViewById(R.id.image_trash);
        mRecentTrash.setOnClickListener(this);
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
        }
    }

    private void updateLists() {
        if (mDbHelper != null) {
            mFavoriteMedList = mDbHelper.getAllEntries(DatabaseHelper.FAVORITE_TABLE);
            if (mFavoriteMedList.size() > 0) {
                MedListAdapter adapter = new MedListAdapter(getContext(), mFavoriteMedList, true);
                mFavoriteListView.setAdapter(adapter);
                mFavoriteListView.setVisibility(View.VISIBLE);
                mFaveNoItemsView.setVisibility(View.GONE);
            } else {
                mFavoriteListView.setVisibility(View.GONE);
                mFaveNoItemsView.setVisibility(View.VISIBLE);
            }

            mRecentMedList = mDbHelper.getAllEntries(DatabaseHelper.RECENT_TABLE);
            if (mRecentMedList.size() > 0) {
                MedListAdapter adapter = new MedListAdapter(getContext(), mRecentMedList, true);
                mRecentListView.setAdapter(adapter);
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
        builder.setMessage(R.string.delete_recent_message);
        builder.setNegativeButton(R.string.control_cancel, null);
        builder.setPositiveButton(R.string.control_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDbHelper.deleteAll(DatabaseHelper.RECENT_TABLE);
                updateLists();
            }
        });
        builder.create().show();
    }
}
