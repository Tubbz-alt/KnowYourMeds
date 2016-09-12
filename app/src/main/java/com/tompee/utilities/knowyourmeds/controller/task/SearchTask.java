package com.tompee.utilities.knowyourmeds.controller.task;

import android.content.Context;
import android.os.AsyncTask;

import com.tompee.utilities.knowyourmeds.controller.networkinterface.RxNormWrapper;
import com.tompee.utilities.knowyourmeds.model.Medicine;

public class SearchTask extends AsyncTask<String, Void, Medicine> {
    private final RxNormWrapper mWrapper;
    private final SearchListener mListener;

    public SearchTask(Context context, SearchListener listener) {
        mWrapper = new RxNormWrapper(context);
        mListener = listener;
    }

    @Override
    protected Medicine doInBackground(String... args) {
        Medicine med = mWrapper.searchForId(args[0]);
        if (med == null) {

        }
        return med;
    }

    @Override
    protected void onPostExecute(Medicine med) {
        mListener.onSearchSuccess(med);
    }

    public interface SearchListener {
        void onSearchSuccess(Medicine medicine);
    }
}

