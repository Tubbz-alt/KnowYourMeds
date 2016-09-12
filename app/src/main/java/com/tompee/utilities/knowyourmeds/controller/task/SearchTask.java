package com.tompee.utilities.knowyourmeds.controller.task;

import android.content.Context;
import android.os.AsyncTask;

import com.tompee.utilities.knowyourmeds.controller.networkinterface.RxNavWrapper;
import com.tompee.utilities.knowyourmeds.model.Medicine;

import java.util.ArrayList;
import java.util.List;

public class SearchTask extends AsyncTask<String, Void, List<Medicine>> {
    private final RxNavWrapper mWrapper;
    private final SearchListener mListener;

    public SearchTask(Context context, SearchListener listener) {
        mWrapper = new RxNavWrapper(context);
        mListener = listener;
    }

    @Override
    protected List<Medicine> doInBackground(String... args) {
        List<Medicine> medList = new ArrayList<>();
        Medicine med = mWrapper.searchForId(args[0]);
        if (med == null) {
        } else {
            medList.add(med);
        }
        return medList;
    }

    @Override
    protected void onPostExecute(List<Medicine> medList) {
        mListener.onSearchSuccess(medList);
    }

    public interface SearchListener {
        void onSearchSuccess(List<Medicine> medList);
    }
}

