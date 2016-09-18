package com.tompee.utilities.knowyourmeds.model;

import java.util.List;

public class Medicine {
    private String mRxnormId;
    private String mName;
    private boolean mIsPrescribable;
    private boolean mIsIngredient;
    private List<String> mSources;

    public Medicine() {
    }

    public String getRxnormId() {
        return mRxnormId;
    }

    public void setRxnormId(String normId) {
        mRxnormId = normId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public boolean isPrescribable() {
        return mIsPrescribable;
    }

    public void setIsPrescribable(boolean isPrescribable) {
        mIsPrescribable = isPrescribable;
    }

    public boolean isIngredient() {
        return mIsIngredient;
    }

    public void setIsIngredient(boolean isIngredient) {
        mIsIngredient = isIngredient;
    }

    public List<String> getSources() {
        return mSources;
    }

    public void setSources(List<String> mSources) {
        this.mSources = mSources;
    }
}
