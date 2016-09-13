package com.tompee.utilities.knowyourmeds.controller.task;

import android.content.Context;
import android.os.AsyncTask;

import com.tompee.utilities.knowyourmeds.controller.networkinterface.RxNavWrapper;
import com.tompee.utilities.knowyourmeds.model.Medicine;

import java.util.ArrayList;
import java.util.List;

public class SearchTask extends AsyncTask<String, Void, Boolean> {
    private final RxNavWrapper mWrapper;
    private final SearchListener mListener;
    private List<Medicine> mMedList;

    public SearchTask(Context context, SearchListener listener) {
        mWrapper = new RxNavWrapper(context);
        mListener = listener;
        mMedList = new ArrayList<>();
    }

    @Override
    protected Boolean doInBackground(String... args) {
        Medicine med = mWrapper.searchMed(args[0]);
        if (med == null) {
            mMedList = mWrapper.suggestedNames(args[0]);
            return false;
        }
        mMedList.add(med);
        return true;
    }

    @Override
    protected void onPostExecute(Boolean isMed) {
        if (isMed) {
            mListener.onSearchSuccess(mMedList);
        } else {
            mListener.onSearchFailed(mMedList);
        }
    }

    public interface SearchListener {
        void onSearchSuccess(List<Medicine> medList);
        void onSearchFailed(List<Medicine> suggestedMed);
    }
}

