package com.tompee.utilities.knowyourmeds.controller.networkinterface;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.RequestFuture;
import com.tompee.utilities.knowyourmeds.model.Medicine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RxNavWrapper {
    private static final String TAG = "RxNavWrapper";
    private static final String RX_NORM_BASE_URL = "https://rxnav.nlm.nih.gov/REST";

    /* Constants */
    private static final String YES = "Y";
    private static final String INGREDIENT = "IN";

    /* RX Norm REST functions */
    private static final String SEARCH_BY_STRING = "%s/rxcui.json?name=%s";
    private static final String PROPERTY_NAME = "%s/rxcui/%s/property.json?propName=%s";
    private static final String SPELLING_SUGGESTIONS = "%s/spellingsuggestions.json?name=%s";
    private static final String TERM_TYPE = "%s/rxcui/%s/property.json?propName=TTY";
    private static final String SOURCES = "%s/rxcui/%s/property.json?propName=Source";
    private static final String BRANDS = "%s/rxcui/%s/related.json?tty=BN";

    /* RXNorm Properties */
    private static final String PROPERTIES_RX_NORM = "RxNorm%20Name";
    private static final String PROPERTIES_PRESCRIBABLE = "PRESCRIBABLE";

    /* RXNorm JSON Tags */
    private static final String TAG_ID_GROUP = "idGroup";
    private static final String TAG_RX_NORM_ID = "rxnormId";
    private static final String TAG_PROP_CONCEPT_GROUP = "propConceptGroup";
    private static final String TAG_PROP_CONCEPT = "propConcept";
    private static final String TAG_PROP_VALUE = "propValue";
    private static final String TAG_SUGGESTION_GROUP = "suggestionGroup";
    private static final String TAG_SUGGESTION_LIST = "suggestionList";
    private static final String TAG_RELATED_GROUP = "relatedGroup";
    private static final String TAG_CONCEPT_GROUP = "conceptGroup";
    private static final String TAG_CONCEPT_PROPERTIES = "conceptProperties";
    private static final String TAG_NAME = "name";

    private final Context mContext;

    public RxNavWrapper(Context context) {
        mContext = context;
    }

    public Medicine searchMed(String name) {
        String url = String.format(SEARCH_BY_STRING, RX_NORM_BASE_URL, name);
        Medicine med = new Medicine();

        /** Get ID */
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                future, future);
        VolleyRequestQueue.getInstance(mContext).addToRequestQueue(jsonRequest);
        try {
            JSONObject response = future.get();
            JSONObject group = response.getJSONObject(TAG_ID_GROUP);
            JSONArray arrayId = group.getJSONArray(TAG_RX_NORM_ID);
            med.setRxnormId(arrayId.getString(0));
        } catch (InterruptedException | ExecutionException | JSONException e) {
            Log.d(TAG, "Error in get ID request: " + e.getMessage());
            return null;
        }

        /** Get NAME */
        future = RequestFuture.newFuture();
        url = String.format(PROPERTY_NAME, RX_NORM_BASE_URL, med.getRxnormId(), PROPERTIES_RX_NORM);
        jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, future, future);
        VolleyRequestQueue.getInstance(mContext).addToRequestQueue(jsonRequest);
        try {
            JSONObject response = future.get();
            med.setName(response.getJSONObject(TAG_PROP_CONCEPT_GROUP).
                    getJSONArray(TAG_PROP_CONCEPT).getJSONObject(0).getString(TAG_PROP_VALUE));
        } catch (InterruptedException | ExecutionException | JSONException e) {
            Log.d(TAG, "Error in get name request: " + e.getMessage());
            return null;
        }

        /** Get Prescribable */
        future = RequestFuture.newFuture();
        url = String.format(PROPERTY_NAME, RX_NORM_BASE_URL, med.getRxnormId(), PROPERTIES_PRESCRIBABLE);
        jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, future, future);
        VolleyRequestQueue.getInstance(mContext).addToRequestQueue(jsonRequest);
        try {
            JSONObject response = future.get();
            med.setIsPrescribable(response.getJSONObject(TAG_PROP_CONCEPT_GROUP).
                    getJSONArray(TAG_PROP_CONCEPT).getJSONObject(0).
                    getString(TAG_PROP_VALUE).equals(YES));
        } catch (InterruptedException | ExecutionException | JSONException e) {
            Log.d(TAG, "Error in get prescribable request: " + e.getMessage());
            med.setIsPrescribable(false);
        }
        return med;
    }

    public List<Medicine> suggestedNames(String name) {
        String url = String.format(SPELLING_SUGGESTIONS, RX_NORM_BASE_URL, name);
        List<Medicine> suggestedNames = new ArrayList<>();
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, future, future);
        VolleyRequestQueue.getInstance(mContext).addToRequestQueue(jsonRequest);
        try {
            JSONObject response = future.get();
            JSONArray suggestionArray = response.getJSONObject(TAG_SUGGESTION_GROUP).
                    getJSONObject(TAG_SUGGESTION_LIST).getJSONArray("suggestion");
            for (int index = 0; index < suggestionArray.length(); index++) {
                Medicine med = new Medicine();
                med.setName(suggestionArray.getString(index));
                suggestedNames.add(med);
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            Log.d(TAG, "Error in get suggestedNames request: " + e.getMessage());
        }
        return suggestedNames;
    }

    public boolean isIngredient(String rxcui) {
        String url = String.format(TERM_TYPE, RX_NORM_BASE_URL, rxcui);
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, future, future);
        VolleyRequestQueue.getInstance(mContext).addToRequestQueue(jsonRequest);
        try {
            JSONObject response = future.get();
            if (response.getJSONObject(TAG_PROP_CONCEPT_GROUP).
                    getJSONArray(TAG_PROP_CONCEPT).getJSONObject(0).
                    getString(TAG_PROP_VALUE).equals(INGREDIENT)) {
                return true;
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            Log.d(TAG, "Error in get is ingredient request: " + e.getMessage());
        }
        return false;
    }

    public List<String> getSources(String rxcui) {
        String url = String.format(SOURCES, RX_NORM_BASE_URL, rxcui);
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, future, future);
        VolleyRequestQueue.getInstance(mContext).addToRequestQueue(jsonRequest);
        try {
            JSONObject response = future.get();
            List<String> sourceList = new ArrayList<>();
            JSONArray array = response.getJSONObject(TAG_PROP_CONCEPT_GROUP).
                    getJSONArray(TAG_PROP_CONCEPT);
            for (int index = 0; index < array.length(); index++) {
                sourceList.add(array.getJSONObject(index).getString(TAG_PROP_VALUE));
            }
            return sourceList;
        } catch (InterruptedException | ExecutionException | JSONException e) {
            Log.d(TAG, "Error in get sources request: " + e.getMessage());
        }
        return null;
    }

    public List<String> getBrandNames(String rxcui) {
        String url = String.format(BRANDS, RX_NORM_BASE_URL, rxcui);
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, future, future);
        VolleyRequestQueue.getInstance(mContext).addToRequestQueue(jsonRequest);
        try {
            JSONObject response = future.get();
            List<String> brandList = new ArrayList<>();
            JSONArray array = response.getJSONObject(TAG_RELATED_GROUP).
                    getJSONArray(TAG_CONCEPT_GROUP).getJSONObject(0).
                    getJSONArray(TAG_CONCEPT_PROPERTIES);
            for (int index = 0; index < array.length(); index++) {
                brandList.add(array.getJSONObject(index).getString(TAG_NAME));
            }
            return brandList;
        } catch (InterruptedException | ExecutionException | JSONException e) {
            Log.d(TAG, "Error in get brands request: " + e.getMessage());
        }
        return null;
    }
}
