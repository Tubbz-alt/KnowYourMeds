package com.tompee.utilities.knowyourmeds.model;

import java.util.List;

public class Medicine {
    private String mRxnormId;
    private String mName;
    private String mUrl;
    private boolean mIsPrescribable;
    private boolean mIsIngredient;
    private List<String> mSources;
    private List<String> mBrands;
    private List<String> mIngredients;
    private List<String> mScdc;
    private List<String> mSbdc;
    private List<String> mSbdg;

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

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
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

    public List<String> getBrands() {
        return mBrands;
    }

    public void setBrands(List<String> brands) {
        mBrands = brands;
    }

    public List<String> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(List<String> ingredients) {
        mIngredients = ingredients;
    }

    public List<String> getScdc() {
        return mScdc;
    }

    public void setScdc(List<String> scdc) {
        mScdc = scdc;
    }

    public List<String> getSbdc() {
        return mSbdc;
    }

    public void setSbdc(List<String> sbdc) {
        mSbdc = sbdc;
    }

    public List<String> getSbdg() {
        return mSbdg;
    }

    public void setSbdg(List<String> sbdg) {
        mSbdg = sbdg;
    }
}

