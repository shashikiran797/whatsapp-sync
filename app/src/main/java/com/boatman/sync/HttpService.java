package com.boatman.sync;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

class HttpService {
    public static void postCall(Context context, String url, final JSONObject requestObj,
        Response.Listener<JSONObject> successCallBack, Response.ErrorListener failureCallback) {

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, requestObj,
                successCallBack, failureCallback
        ) {
        };
        queue.add(postRequest);
    }
}