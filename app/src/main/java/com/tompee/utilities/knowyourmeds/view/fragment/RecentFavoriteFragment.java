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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.database.DatabaseHelper;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.MedDetailActivity;
import com.tompee.utilities.knowyourmeds.view.adapter.MedListAdapter;

import java.util.List;

public class RecentFavoriteFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemClickListener {
    private static final String IS_RECENT = "is_recent";
    private DatabaseHelper mDbHelper;
    private View mRootView;

    private List<Medicine> mMedicineList;

    public static RecentFavoriteFragment newInstance(boolean isRecent) {
        RecentFavoriteFragment fragment = new RecentFavoriteFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_RECENT, isRecent);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = DatabaseHelper.getInstance(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        updateLists(mRootView, getArguments().getBoolean(IS_RECENT));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_recent_favorite, container, false);
        boolean isRecent = getArguments().getBoolean(IS_RECENT);
        TextView textView = (TextView) mRootView.findViewById(R.id.header);
        textView.setText(isRecent ? R.string.recent : R.string.favorites);
        updateLists(mRootView, isRecent);
        return mRootView;
    }

    private void startMedListActivity(Medicine med, int origin) {
        Intent intent = new Intent(getContext(), MedDetailActivity.class);
        intent.putExtra(MedDetailActivity.TAG_NAME, med.getName());
        intent.putExtra(MedDetailActivity.TAG_ID, med.getRxnormId());
        intent.putExtra(MedDetailActivity.TAG_ORIGIN, origin);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void updateLists(View rootView, boolean isRecent) {
        if (isRecent) {
            mMedicineList = mDbHelper.getAllShortEntries(DatabaseHelper.RECENT_TABLE);
        } else {
            mMedicineList = mDbHelper.getAllShortEntries(DatabaseHelper.FAVORITE_TABLE);
        }
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setOnItemClickListener(this);
        ImageView trash = (ImageView) rootView.findViewById(R.id.trash);
        trash.setOnClickListener(this);
        if (mMedicineList.size() > 0) {
            MedListAdapter adapter = new MedListAdapter(getContext(), mMedicineList, false);
            View view = listView.getChildAt(0);
            int position = listView.getFirstVisiblePosition();
            listView.setAdapter(adapter);
            if (view == null) {
                listView.setSelectionFromTop(position, 0);
            } else {
                listView.setSelectionFromTop(position, view.getTop());
            }
            trash.setVisibility(View.VISIBLE);
        } else {
            listView.setAdapter(null);
            trash.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.control_delete);
        if (getArguments().getBoolean(IS_RECENT)) {
            builder.setMessage(R.string.delete_recent_message);
            builder.setPositiveButton(R.string.control_delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mDbHelper.deleteAll(DatabaseHelper.RECENT_TABLE);
                    updateLists(mRootView, true);
                }
            });
        } else {
            builder.setMessage(R.string.delete_favorite_message);
            builder.setPositiveButton(R.string.control_delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mDbHelper.deleteAll(DatabaseHelper.FAVORITE_TABLE);
                    updateLists(mRootView, false);
                }
            });
        }
        builder.setNegativeButton(R.string.control_cancel, null);
        builder.create().show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        startMedListActivity(mMedicineList.get(i), getArguments().getBoolean(IS_RECENT) ?
                MedDetailActivity.FROM_RECENTS : MedDetailActivity.FROM_FAVORITES);
    }
}
