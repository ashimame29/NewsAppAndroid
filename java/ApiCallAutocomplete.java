package com.example.newsapp_1;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiCallAutocomplete {
    private static ApiCallAutocomplete mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    public ApiCallAutocomplete(Context ctx) {
        mCtx = ctx;
        mRequestQueue = getRequestQueue();
    }
    public static synchronized ApiCallAutocomplete getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ApiCallAutocomplete(context);
        }
        return mInstance;
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
    public static void make(Context ctx, String query, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = "https://ashima-mehra1996.cognitiveservices.azure.com/bing/v7.0/suggestions?q=" + query;
        //JSONObject params = new JSONObject();
        final Map<String, String> params = new HashMap<>();
        params.put("Ocp-Apim-Subscription-Key", "d5ccea5de3534ae396d0715dbd81748b");
        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                listener,
                errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return params;
            }
        };
        ApiCallAutocomplete.getInstance(ctx).addToRequestQueue(jsonRequest);
    }
}
