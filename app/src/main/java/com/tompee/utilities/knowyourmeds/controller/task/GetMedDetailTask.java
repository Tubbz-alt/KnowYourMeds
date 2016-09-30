package com.tompee.utilities.knowyourmeds.controller.task;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.NoConnectionError;
import com.tompee.utilities.knowyourmeds.controller.networkinterface.RxNavWrapper;
import com.tompee.utilities.knowyourmeds.model.Medicine;

import java.util.Calendar;

public class GetMedDetailTask extends AsyncTask<String, Void, Medicine> {
    private final RxNavWrapper mWrapper;
    private final GetMedTaskListener mListener;
    private boolean mIsOffline;

    public GetMedDetailTask(Context context, GetMedTaskListener listener) {
        mWrapper = new RxNavWrapper(context);
        mListener = listener;
    }

    @Override
    protected Medicine doInBackground(String... args) {
        try {
            Medicine med = mWrapper.searchMed(args[0]);
            med.setUrl(mWrapper.getMedlineUrl(med.getName(), med.getRxnormId()));
            med.setSources(mWrapper.getSources(med.getRxnormId()));
            med.setIsIngredient(mWrapper.isIngredient(med.getRxnormId()));
            mWrapper.getTtyValues(med);
            med.setDate(Calendar.getInstance().getTime());
            return med;
        } catch (NoConnectionError noConnectionError) {
            mIsOffline = true;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Medicine med) {
        if (mIsOffline) {
            mListener.onConnectionError();
        } else {
            mListener.onCompleted(med);
        }
    }

    public interface GetMedTaskListener {
        void onCompleted(Medicine med);
        void onConnectionError();
    }
}
