package com.tompee.utilities.knowyourmeds.controller.task;

import android.content.Context;
import android.os.AsyncTask;

import com.tompee.utilities.knowyourmeds.controller.networkinterface.RxNavWrapper;
import com.tompee.utilities.knowyourmeds.model.Medicine;

public class GetMedDetailTask extends AsyncTask<String, Void, Medicine> {
    private final RxNavWrapper mWrapper;
    private final GetMedTaskListener mListener;

    public GetMedDetailTask(Context context, GetMedTaskListener listener) {
        mWrapper = new RxNavWrapper(context);
        mListener = listener;
    }

    @Override
    protected Medicine doInBackground(String... args) {
        Medicine med = mWrapper.searchMed(args[0]);
        med.setUrl(mWrapper.getMedlineUrl(med.getName(), med.getRxnormId()));
        med.setIsIngredient(mWrapper.isIngredient(med.getRxnormId()));
        med.setSources(mWrapper.getSources(med.getRxnormId()));
        med.setBrands(mWrapper.getBrandNames(med.getRxnormId()));
        med.setIngredients(mWrapper.getIngredients(med.getRxnormId()));
        med.setScdc(mWrapper.getScdc(med.getRxnormId()));
        med.setSbdc(mWrapper.getSbdc(med.getRxnormId()));
        return med;
    }

    @Override
    protected void onPostExecute(Medicine med) {
        mListener.onCompleted(med);
    }

    public interface GetMedTaskListener {
        void onCompleted(Medicine med);
    }
}
