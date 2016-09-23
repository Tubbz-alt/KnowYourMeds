package com.tompee.utilities.knowyourmeds.model;

import java.util.ArrayList;

public class Medicine {
    private String mRxnormId;
    private String mName;
    private String mUrl;
    private boolean mIsPrescribable;
    private boolean mIsIngredient;
    private ArrayList<String> mSources;
    private ArrayList<String> mBrands;
    private ArrayList<String> mIngredients;
    private ArrayList<String> mScdc;
    private ArrayList<String> mSbdc;
    private ArrayList<String> mSbdg;
    private ArrayList<String> mScd;

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

    public ArrayList<String> getSources() {
        return mSources;
    }

    public void setSources(ArrayList<String> sources) {
        mSources = sources;
    }

    public ArrayList<String> getBrands() {
        return mBrands;
    }

    public void setBrands(ArrayList<String> brands) {
        mBrands = brands;
    }

    public ArrayList<String> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        mIngredients = ingredients;
    }

    public ArrayList<String> getScdc() {
        return mScdc;
    }

    public void setScdc(ArrayList<String> scdc) {
        mScdc = scdc;
    }

    public ArrayList<String> getSbdc() {
        return mSbdc;
    }

    public void setSbdc(ArrayList<String> sbdc) {
        mSbdc = sbdc;
    }

    public ArrayList<String> getSbdg() {
        return mSbdg;
    }

    public void setSbdg(ArrayList<String> sbdg) {
        mSbdg = sbdg;
    }

    public ArrayList<String> getScd() {
        return mScd;
    }

    public void setScd(ArrayList<String> scd) {
        mScd = scd;
    }
}

