package com.tompee.utilities.knowyourmeds.controller.networkinterface;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.RequestFuture;
import com.tompee.utilities.knowyourmeds.model.Medicine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RxNavWrapper {
    private static final String TAG = "RxNavWrapper";
    private static final String RX_NORM_BASE_URL = "https://rxnav.nlm.nih.gov/REST";
    private static final String MEDLINE_BASE_URL = "https://apps.nlm.nih.gov/medlineplus/services/" +
            "mpconnect_service.cfm?knowledgeResponseType=application/json&mainSearchCriteria.v.cs=" +
            "2.16.840.1.113883.6.88&mainSearchCriteria.v.c=%s";
    private static final String MEDLINE_QUERY_URL = "https://vsearch.nlm.nih.gov/vivisimo/cgi-bin/" +
            "query-meta?v%3Aproject=medlineplus&v%3Asources=medlineplus-bundle&query=";
    private static final String DAILY_MED_BASE_URL = "https://dailymed.nlm.nih.gov/dailymed/services/v2";

    /* Constants */
    private static final String YES = "Y";
    private static final String INGREDIENT = "IN";
    private static final String TTY = "TTY";
    private static final String BRAND = "BN";
    private static final String SCDC = "SCDC";
    private static final String SBDC = "SBDC";
    private static final String SBDG = "SBDG";
    private static final String SCD = "SCD";
    private static final String SCDG = "SCDG";
    private static final String SBD = "SBD";
    private static final String SPL_SET_ID = "SPL_SET_ID";
    private static final int URL_REQUEST_TIMEOUT = 10000;

    /* RX Norm REST functions */
    private static final String URL_SEARCH_BY_STRING = "%s/rxcui.json?name=%s";
    private static final String URL_PROPERTY_NAME = "%s/rxcui/%s/property.json?propName=%s";
    private static final String URL_SPELLING_SUGGESTIONS = "%s/spellingsuggestions.json?name=%s";
    private static final String URL_ATTRIBUTES = "%s/rxcui/%s/allProperties.json?prop=ATTRIBUTES";
    private static final String URL_CODES = "%s/rxcui/%s/allProperties.json?prop=CODES";
    private static final String URL_SOURCES = "%s/rxcui/%s/property.json?propName=Source";
    private static final String URL_TTY_VALUES = "%s/rxcui/%s/related.json?tty=" + BRAND + "+" +
            INGREDIENT + "+" + SCDC + "+" + SBDC + "+" + SBDG + "+" + SCD + "+" + SCDG + "+" + SBD;

    /* Daily med REST functions */
    private static final String URL_SPL = "%s/spls.json?rxcui=%s&page=%d";

    /* RXNorm Properties */
    private static final String PROPERTIES_RX_NORM = "RxNorm%20Name";
    private static final String PROPERTIES_PRESCRIBABLE = "PRESCRIBABLE";

    /* RXNorm JSON Tags */
    private static final String TAG_ID_GROUP = "idGroup";
    private static final String TAG_RX_NORM_ID = "rxnormId";
    private static final String TAG_PROP_CONCEPT_GROUP = "propConceptGroup";
    private static final String TAG_PROP_CONCEPT = "propConcept";
    private static final String TAG_PROP_VALUE = "propValue";
    private static final String TAG_PROP_NAME = "propName";
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
    private static final String TAG_DATA = "data";
    private static final String TAG_TITLE = "title";
    private static final String TAG_SET_ID = "setid";
    private static final String TAG_METADATA = "metadata";
    private static final String TAG_TOTAL_PAGES = "total_pages";
    private static final String TAG_CURRENT_PAGE = "current_page";

    private final Context mContext;

    public RxNavWrapper(Context context) {
        mContext = context;
    }

    public Medicine searchMed(String name) throws NoConnectionError {
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
            Log.e(TAG, "Error in get ID request: " + e.getMessage());
            if (e.getCause() instanceof NoConnectionError) {
                throw (NoConnectionError) e.getCause();
            }
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
            Log.e(TAG, "Error in get name request: " + e.getMessage());
            if (e.getCause() instanceof NoConnectionError) {
                throw (NoConnectionError) e.getCause();
            }
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
            Log.e(TAG, "Error in get prescribable request: " + e.getMessage());
            if (e.getCause() instanceof NoConnectionError) {
                throw (NoConnectionError) e.getCause();
            }
            med.setIsPrescribable(false);
        }
        return med;
    }

    public List<Medicine> suggestedNames(String name) throws NoConnectionError {
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
            Log.e(TAG, "Error in get suggestedNames request: " + e.getMessage());
            if (e.getCause() instanceof NoConnectionError) {
                throw (NoConnectionError) e.getCause();
            }
        }
        return suggestedNames;
    }

    public void getAttributes(Medicine med) throws NoConnectionError {
        String url = String.format(URL_ATTRIBUTES, RX_NORM_BASE_URL, med.getRxnormId());
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, future, future);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(URL_REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyRequestQueue.getInstance(mContext).addToRequestQueue(jsonRequest);
        try {
            JSONObject response = future.get();
            JSONArray array = response.getJSONObject(TAG_PROP_CONCEPT_GROUP).
                    getJSONArray(TAG_PROP_CONCEPT);
            for (int index = 0; index < array.length(); index++) {
                JSONObject obj = array.getJSONObject(index);
                switch (obj.getString(TAG_PROP_NAME)) {
                    case TTY:
                        med.setIsIngredient(obj.getString(TAG_PROP_VALUE).equals(INGREDIENT));
                        break;
                }
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            Log.e(TAG, "Error in get is ingredient request: " + e.getMessage());
            if (e.getCause() instanceof NoConnectionError) {
                throw (NoConnectionError) e.getCause();
            }
        }
    }

    public void getCodes(Medicine med) throws NoConnectionError {
        Map<String, String> codeMap = new HashMap<>();
        int totalPages = 0;
        int currentPage = 0;
        do {
            String url = String.format(URL_SPL, DAILY_MED_BASE_URL, med.getRxnormId(), ++currentPage);
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, future, future);
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(URL_REQUEST_TIMEOUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleyRequestQueue.getInstance(mContext).addToRequestQueue(jsonRequest);
            try {
                JSONObject response = future.get();
                /** Get data */
                JSONArray array = response.getJSONArray(TAG_DATA);
                for (int index = 0; index < array.length(); index++) {
                    JSONObject obj = array.getJSONObject(index);
                    codeMap.put(obj.getString(TAG_SET_ID), obj.getString(TAG_TITLE));
                }
                /** Get Metadata */
                totalPages = response.getJSONObject(TAG_METADATA).getInt(TAG_TOTAL_PAGES);
                currentPage = response.getJSONObject(TAG_METADATA).getInt(TAG_CURRENT_PAGE);
            } catch (InterruptedException | ExecutionException | JSONException e) {
                Log.e(TAG, "Error in get codes request: " + e.getMessage());
                if (e.getCause() instanceof NoConnectionError) {
                    throw (NoConnectionError) e.getCause();
                }
            }
        } while (currentPage < totalPages);
        med.setSplSetId(codeMap);
    }

    public void getSources(Medicine med) throws NoConnectionError {
        String url = String.format(URL_SOURCES, RX_NORM_BASE_URL, med.getRxnormId());
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
            med.setSources(sourceList);
        } catch (InterruptedException | ExecutionException | JSONException e) {
            Log.e(TAG, "Error in get sources request: " + e.getMessage());
            if (e.getCause() instanceof NoConnectionError) {
                throw (NoConnectionError) e.getCause();
            }
        }
    }

    public void getMedLineUrl(Medicine med) throws NoConnectionError {
        String url = String.format(MEDLINE_BASE_URL, med.getRxnormId());
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, future, future);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(URL_REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyRequestQueue.getInstance(mContext).addToRequestQueue(jsonRequest);
        try {
            JSONObject response = future.get();
            med.setUrl(response.getJSONObject(TAG_FEED).getJSONArray(TAG_ENTRY).getJSONObject(0).
                    getJSONArray(TAG_LINK).getJSONObject(0).getString(TAG_HREF));
            return;
        } catch (InterruptedException | ExecutionException | JSONException e) {
            Log.e(TAG, "Error in get Medline URL request: " + e.getMessage());
            if (e.getCause() instanceof NoConnectionError) {
                throw (NoConnectionError) e.getCause();
            }
        }
        med.setUrl(MEDLINE_QUERY_URL + med.getName());
    }

    public void getTtyValues(Medicine med) throws NoConnectionError {
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
                    case SCDG:
                        JSONArray scdgArray = obj.getJSONArray(TAG_CONCEPT_PROPERTIES);
                        for (int i = 0; i < scdgArray.length(); i++) {
                            list.add(scdgArray.getJSONObject(i).getString(TAG_NAME));
                        }
                        med.setScdg(list);
                        break;
                    case SBD:
                        JSONArray sbdArray = obj.getJSONArray(TAG_CONCEPT_PROPERTIES);
                        for (int i = 0; i < sbdArray.length(); i++) {
                            list.add(sbdArray.getJSONObject(i).getString(TAG_NAME));
                        }
                        med.setSbd(list);
                        break;
                }
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            Log.e(TAG, "Error in get is ingredient request: " + e.getMessage());
            if (e.getCause() instanceof NoConnectionError) {
                throw (NoConnectionError) e.getCause();
            }
        }
    }
}
