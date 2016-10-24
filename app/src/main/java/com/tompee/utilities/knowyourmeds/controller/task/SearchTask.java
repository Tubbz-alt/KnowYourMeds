package com.tompee.utilities.knowyourmeds.controller.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.android.volley.NoConnectionError;
import com.tompee.utilities.knowyourmeds.controller.database.DatabaseHelper;
import com.tompee.utilities.knowyourmeds.controller.networkinterface.RxNavWrapper;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.MainActivity;
import com.tompee.utilities.knowyourmeds.view.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchTask extends AsyncTask<String, Void, Boolean> {
    private final RxNavWrapper mWrapper;
    private final SearchListener mListener;
    private final SharedPreferences mSharedPreferences;
    private final DatabaseHelper mDatabaseHelper;
    private List<Medicine> mMedList;
    private boolean mIsOffline;

    public SearchTask(Context context, SearchListener listener) {
        mWrapper = new RxNavWrapper(context);
        mListener = listener;
        mMedList = new ArrayList<>();
        mSharedPreferences = context.getSharedPreferences(MainActivity.SHARED_PREF,
                Context.MODE_PRIVATE);
        mDatabaseHelper = DatabaseHelper.getInstance(context);
    }

    @Override
    protected Boolean doInBackground(String... args) {
        if (!mSharedPreferences.getBoolean(SettingsActivity.TAG_OFFLINE_MODE_CB, false)) {
            try {
                Medicine med = mWrapper.searchMed(args[0]);
                if (med == null) {
                    mMedList = mWrapper.suggestedNames(args[0]);
                    return false;
                }
                mMedList.add(med);
                return true;
            } catch (NoConnectionError noConnectionError) {
                mIsOffline = true;
            }
        } else {
            mMedList = mDatabaseHelper.getAllShortEntriesByName(args[0]);
            return true;
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean isMed) {
        if (mIsOffline) {
            mListener.onConnectionError();
            return;
        }
        if (isMed) {
            mListener.onSearchSuccess(mMedList);
        } else {
            mListener.onSearchFailed(mMedList);
        }
    }

    public interface SearchListener {
        void onSearchSuccess(List<Medicine> medList);

        void onSearchFailed(List<Medicine> suggestedMed);

        void onConnectionError();
    }
}

