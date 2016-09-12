package com.tompee.utilities.knowyourmeds.controller.networkinterface;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.tompee.utilities.knowyourmeds.model.Medicine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class RxNormWrapper {
    private static final String TAG = "RxNormWrapper";
    private static final String BASE_URL = "https://rxnav.nlm.nih.gov/REST";

    /* REST functions */
    private static final String SEARCH_BY_STRING = "%s/rxcui.json?name=%s";

    /* Search JSON Tags */
    private static final String SEARCH_TAG_ID_GROUP = "idGroup";
    private static final String SEARCH_TAG_RX_NORM_ID = "rxnormId";
    private static final String SEARCH_TAG_NAME = "name";

    private final Context mContext;

    public RxNormWrapper(Context context) {
        mContext = context;
    }

    public Medicine searchForId(String name) {
        String url = String.format(SEARCH_BY_STRING, BASE_URL, name);
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                future, future);
        VolleyRequestQueue.getInstance(mContext).addToRequestQueue(jsonRequest);
        try {
            JSONObject response = future.get();
            JSONObject group = response.getJSONObject(SEARCH_TAG_ID_GROUP);
            JSONArray arrayId = group.getJSONArray(SEARCH_TAG_RX_NORM_ID);
            String id = arrayId.getString(0);
            String medName = group.getString(SEARCH_TAG_NAME);
            return new Medicine(id, medName);
        } catch (InterruptedException | ExecutionException e) {
            Log.d(TAG, "Error in search request: " + e.getMessage());
        } catch (JSONException e) {
            Log.d(TAG, "Error in parsing: " + e.getMessage());
        }
        return null;
    }
}
