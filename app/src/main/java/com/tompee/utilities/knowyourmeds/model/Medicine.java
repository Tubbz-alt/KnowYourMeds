package com.tompee.utilities.knowyourmeds.model;

public class Medicine {
    private final String mRxnormId;
    private final String mName;

    public Medicine(String id, String name) {
        mRxnormId = id;
        mName = name;
    }

    public String getRxnormId() {
        return mRxnormId;
    }

    public String getName() {
        return mName;
    }
}
