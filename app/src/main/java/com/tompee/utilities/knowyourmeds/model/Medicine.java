package com.tompee.utilities.knowyourmeds.model;

public class Medicine {
    private String mRxnormId;
    private String mName;
    private boolean mIsPrescribable;
    private boolean mIsIngredient;

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

    public boolean isIsIngredient() {
        return mIsIngredient;
    }

    public void setIsIngredient(boolean isIngredient) {
        mIsIngredient = isIngredient;
    }
}
