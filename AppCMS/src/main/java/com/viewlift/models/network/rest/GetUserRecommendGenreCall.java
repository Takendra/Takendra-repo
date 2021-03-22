package com.viewlift.models.network.rest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.viewlift.models.data.appcms.ui.main.GetRecommendationGenres;

import java.util.HashMap;
import java.util.Map;

public class GetUserRecommendGenreCall {
    private static final String TAG = "GetUserRecommendGenreCall";
    private final GetUserRecommendGenreRest getUserRecommendGenreRest;
    private final Gson gson;

    public GetUserRecommendGenreCall(GetUserRecommendGenreRest getUserRecommendGenreRest, Gson gson){
        this.getUserRecommendGenreRest = getUserRecommendGenreRest;
        this.gson=gson;
    }

    @SuppressLint("LongLogTag")
    public GetRecommendationGenres call(Context context, String siteName, String userId, String url, String xapiKey){

        StringBuilder appCMSMainUrlSb = new StringBuilder(url);
        appCMSMainUrlSb.append("?site="+ siteName);
        appCMSMainUrlSb.append("&userId="+userId);
        final String appCMSMainUrl = appCMSMainUrlSb.toString();

        GetRecommendationGenres getRecommendationGenres = null;

        try {
//                Log.d(TAG, "Retrieving main.json from URL: " + appCMSMainUrl);
            long start = System.currentTimeMillis();
            Log.d(TAG, appCMSMainUrl);
            Map<String, String> authTokenMap = new HashMap<>();
            authTokenMap.put("x-api-key", xapiKey);
            getRecommendationGenres = getUserRecommendGenreRest.get(appCMSMainUrlSb.toString(),authTokenMap).execute().body();
            long end = System.currentTimeMillis();
            //Log.d(TAG, getRecommendationGenres.toString());
            //Log.d(TAG, "main.json URL: " + appCMSMainUrlSb.toString());
        } catch (Exception e) {
            Log.w(TAG, "Failed to read recommend genres: " + e.getMessage());
        }

        return getRecommendationGenres;
    }
}
