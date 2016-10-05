package com.tompee.utilities.knowyourmeds.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Medicine {
    private String mRxnormId;
    private String mName;
    private boolean mIsPrescribable;
    private boolean mIsIngredient;
    private String mUrl;
    private ArrayList<String> mIngredients;
    private ArrayList<String> mSources;
    private ArrayList<String> mBrands;
    private ArrayList<String> mScdc;
    private ArrayList<String> mSbdc;
    private ArrayList<String> mSbdg;
    private ArrayList<String> mScd;
    private Date mDate;
    private ArrayList<String> mScdg;
    private ArrayList<String> mSbd;
    private Map<String, String> mSplSetId;
    private Map<String, Map<String, String>> mInteractions;

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

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public ArrayList<String> getScdg() {
        return mScdg;
    }

    public void setScdg(ArrayList<String> scdg) {
        mScdg = scdg;
    }

    public ArrayList<String> getSbd() {
        return mSbd;
    }

    public void setSbd(ArrayList<String> sbd) {
        mSbd = sbd;
    }

    public Map<String, String> getSplSetId() {
        return mSplSetId;
    }

    public void setSplSetId(Map<String, String> splSetId) {
        mSplSetId = splSetId;
    }

    public Map<String, Map<String, String>> getInteractions() {
        return mInteractions;
    }

    public void setInteractions(Map<String, Map<String, String>> interactions) {
        mInteractions = interactions;
    }
}

