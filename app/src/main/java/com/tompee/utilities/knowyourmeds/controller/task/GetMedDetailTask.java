package com.tompee.utilities.knowyourmeds.controller.task;

import android.content.Context;
import android.os.AsyncTask;

import com.tompee.utilities.knowyourmeds.controller.networkinterface.RxNavWrapper;
import com.tompee.utilities.knowyourmeds.model.Medicine;

public class GetMedDetailTask extends AsyncTask<String, Void, Void>{
    private final RxNavWrapper mWrapper;

    public GetMedDetailTask(Context context) {
        mWrapper = new RxNavWrapper(context);
    }
    @Override
    protected Void doInBackground(String... args) {
        Medicine med = mWrapper.searchMed(args[0]);
        med.setIsIngredient(mWrapper.isIngredient(med.getRxnormId()));
        return null;
    }
}
