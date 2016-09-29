package com.tompee.utilities.knowyourmeds.controller.networkinterface;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
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
    private static final String MEDLINE_BASE_URL = "https://apps.nlm.nih.gov/medlineplus/services/" +
            "mpconnect_service.cfm?knowledgeResponseType=application/json&mainSearchCriteria.v.cs=" +
            "2.16.840.1.113883.6.88&mainSearchCriteria.v.c=%s";
    private static final String MEDLINE_QUERY_URL = "https://vsearch.nlm.nih.gov/vivisimo/cgi-bin/" +
            "query-meta?v%3Aproject=medlineplus&v%3Asources=medlineplus-bundle&query=";

    /* Constants */
    private static final String YES = "Y";
    private static final String INGREDIENT = "IN";
    private static final String BRAND = "BN";
    private static final String SCDC = "SCDC";
    private static final String SBDC = "SBDC";
    private static final String SBDG = "SBDG";
    private static final String SCD = "SCD";
    private static final int URL_REQUEST_TIMEOUT = 10000;

    /* RX Norm REST functions */
    private static final String URL_SEARCH_BY_STRING = "%s/rxcui.json?name=%s";
    private static final String URL_PROPERTY_NAME = "%s/rxcui/%s/property.json?propName=%s";
    private static final String URL_SPELLING_SUGGESTIONS = "%s/spellingsuggestions.json?name=%s";
    private static final String URL_TERM_TYPE = "%s/rxcui/%s/property.json?propName=TTY";
    private static final String URL_SOURCES = "%s/rxcui/%s/property.json?propName=Source";
    private static final String URL_TTY_VALUES = "%s/rxcui/%s/related.json?tty=" + BRAND + "+" +
            INGREDIENT + "+" + SCDC + "+" + SBDC + "+" + SBDG + "+" + SCD;

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
    private static final String TAG_TTY = "tty";
    private static final String TAG_CONCEPT_PROPERTIES = "conceptProperties";
    private static final String TAG_NAME = "name";
    private static final String TAG_FEED = "feed";
    private static final String TAG_ENTRY = "entry";
    private static final String TAG_LINK = "link";
    private static final String TAG_HREF = "href";

    private final Context mContext;

    public RxNavWrapper(Context context) {
        mContext = context;
    }

    public Medicine searchMed(String name) {
        String url = String.format(URL_SEARCH_BY_STRING, RX_NORM_BASE_URL, name).replace(" ", "%20");
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
        url = String.format(URL_PROPERTY_NAME, RX_NORM_BASE_URL, med.getRxnormId(), PROPERTIES_RX_NORM);
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
        url = String.format(URL_PROPERTY_NAME, RX_NORM_BASE_URL, med.getRxnormId(), PROPERTIES_PRESCRIBABLE);
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
        String url = String.format(URL_SPELLING_SUGGESTIONS, RX_NORM_BASE_URL, name);
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
        String url = String.format(URL_TERM_TYPE, RX_NORM_BASE_URL, rxcui);
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

    public ArrayList<String> getSources(String rxcui) {
        String url = String.format(URL_SOURCES, RX_NORM_BASE_URL, rxcui);
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, future, future);
        VolleyRequestQueue.getInstance(mContext).addToRequestQueue(jsonRequest);
        try {
            JSONObject response = future.get();
            ArrayList<String> sourceList = new ArrayList<>();
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

    public String getMedlineUrl(String name, String rxcui) {
        String url = String.format(MEDLINE_BASE_URL, rxcui);
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, future, future);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(URL_REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyRequestQueue.getInstance(mContext).addToRequestQueue(jsonRequest);
        try {
            JSONObject response = future.get();
            return response.getJSONObject(TAG_FEED).getJSONArray(TAG_ENTRY).getJSONObject(0).
                    getJSONArray(TAG_LINK).getJSONObject(0).getString(TAG_HREF);
        } catch (InterruptedException | ExecutionException | JSONException e) {
            Log.d(TAG, "Error in get Medline URL request: " + e.getMessage());
        }
        return MEDLINE_QUERY_URL + name;
    }

    public void getTtyValues(Medicine med) {
        String url = String.format(URL_TTY_VALUES, RX_NORM_BASE_URL, med.getRxnormId());
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, future, future);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(URL_REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyRequestQueue.getInstance(mContext).addToRequestQueue(jsonRequest);
        try {
            JSONObject response = future.get();
            JSONArray array = response.getJSONObject(TAG_RELATED_GROUP).
                    getJSONArray(TAG_CONCEPT_GROUP);
            for (int index = 0; index < array.length(); index++) {
                ArrayList<String> list = new ArrayList<>();
                JSONObject obj = array.getJSONObject(index);
                switch (obj.getString(TAG_TTY)) {
                    case BRAND:
                        JSONArray brandArray = obj.getJSONArray(TAG_CONCEPT_PROPERTIES);
                        for (int i = 0; i < brandArray.length(); i++) {
                            list.add(brandArray.getJSONObject(i).getString(TAG_NAME));
                        }
                        med.setBrands(list);
                        break;
                    case INGREDIENT:
                        JSONArray inArray = obj.getJSONArray(TAG_CONCEPT_PROPERTIES);
                        for (int i = 0; i < inArray.length(); i++) {
                            list.add(inArray.getJSONObject(i).getString(TAG_NAME));
                        }
                        med.setIngredients(list);
                        break;
                    case SCDC:
                        JSONArray scdcArray = obj.getJSONArray(TAG_CONCEPT_PROPERTIES);
                        for (int i = 0; i < scdcArray.length(); i++) {
                            list.add(scdcArray.getJSONObject(i).getString(TAG_NAME));
                        }
                        med.setScdc(list);
                        break;
                    case SBDC:
                        JSONArray sbdcArray = obj.getJSONArray(TAG_CONCEPT_PROPERTIES);
                        for (int i = 0; i < sbdcArray.length(); i++) {
                            list.add(sbdcArray.getJSONObject(i).getString(TAG_NAME));
                        }
                        med.setSbdc(list);
                        break;
                    case SBDG:
                        JSONArray sbdgArray = obj.getJSONArray(TAG_CONCEPT_PROPERTIES);
                        for (int i = 0; i < sbdgArray.length(); i++) {
                            list.add(sbdgArray.getJSONObject(i).getString(TAG_NAME));
                        }
                        med.setSbdg(list);
                        break;
                    case SCD:
                        JSONArray gpckArray = obj.getJSONArray(TAG_CONCEPT_PROPERTIES);
                        for (int i = 0; i < gpckArray.length(); i++) {
                            list.add(gpckArray.getJSONObject(i).getString(TAG_NAME));
                        }
                        med.setScd(list);
                        break;
                }
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            Log.d(TAG, "Error in get is ingredient request: " + e.getMessage());
        }
    }
}
