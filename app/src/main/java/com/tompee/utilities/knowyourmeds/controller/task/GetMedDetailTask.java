package com.tompee.utilities.knowyourmeds.controller.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.android.volley.NoConnectionError;
import com.tompee.utilities.knowyourmeds.controller.networkinterface.RxNavWrapper;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.MainActivity;
import com.tompee.utilities.knowyourmeds.view.SettingsActivity;

import java.util.Calendar;

public class GetMedDetailTask extends AsyncTask<String, Void, Medicine> {
    private final RxNavWrapper mWrapper;
    private final GetMedTaskListener mListener;
    private final boolean mIsSplEnabled;
    private boolean mIsOffline;

    public GetMedDetailTask(Context context, GetMedTaskListener listener) {
        mWrapper = new RxNavWrapper(context);
        mListener = listener;
        SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.SHARED_PREF,
                Context.MODE_PRIVATE);
        mIsSplEnabled = sharedPreferences.getBoolean(SettingsActivity.TAG_SPL_CB, true);
    }

    @Override
    protected Medicine doInBackground(String... args) {
        try {
            Medicine med = mWrapper.searchMed(args[0]);
            med.setDate(Calendar.getInstance().getTime());
            mWrapper.getMedLineUrl(med);
            mWrapper.getSources(med);
            mWrapper.getAttributes(med);
            if (mIsSplEnabled) {
                mWrapper.getCodes(med);
            }
            mWrapper.getTtyValues(med);
            mWrapper.getDrugInteractions(med);
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
