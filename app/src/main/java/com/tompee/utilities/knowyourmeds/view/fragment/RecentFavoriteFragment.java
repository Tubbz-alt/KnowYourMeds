package com.tompee.utilities.knowyourmeds.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.database.FavoriteDbHelper;

public class RecentFavoriteFragment extends Fragment {
    private FavoriteDbHelper mFavoriteDbHelper;

    public static RecentFavoriteFragment newInstance() {
        return new RecentFavoriteFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFavoriteDbHelper = new FavoriteDbHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_favorite, container, false);
        return view;
    }
}
