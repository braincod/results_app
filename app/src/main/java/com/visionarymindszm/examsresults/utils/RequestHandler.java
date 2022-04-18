package com.visionarymindszm.examsresults.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

// TODO: 2/21/21 Clean the static memory leaks, it might be a problem in Android 7 and below
public class RequestHandler {

    private static RequestHandler instance;
    private RequestQueue requestQueue;
    private static Context mContext;

    /**
     *
     * @param context: Activity/Fragment
     */

    private RequestHandler(Context context){
        mContext = context;
        requestQueue = getRequestQueue();
    }

    /**
     *
     * @param context: Activity/Fragment
     * @return instance (RequestHandler-Class)
     */

    public static synchronized RequestHandler getInstance(Context context){
        if (instance == null){
            instance = new RequestHandler(context);
        }
        return instance;
    }
    public RequestQueue getRequestQueue(){
        if (requestQueue == null){
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }

        return requestQueue;
    }

    /**
     *
     * @param request: request being passed
     * @param <T> : generic type
     */
    public <T> void addRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }
}
