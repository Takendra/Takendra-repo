package com.viewlift.models.network.rest;

import androidx.annotation.WorkerThread;
import android.util.Log;

import com.google.gson.Gson;
import com.viewlift.models.data.appcms.beacon.AppCMSBeaconRequest;
import com.viewlift.models.data.appcms.beacon.BeaconResponse;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by sandeep.singh on 8/22/2017.
 */

public class AppCMSBeaconCall {

    private static final String TAG = "AppCMSBeaconCall";
    private final AppCMSBeaconRest appCMSBeaconRest;
    private final String userAgent;


    @Inject
    public AppCMSBeaconCall(AppCMSBeaconRest appCMSBeaconRest) {

        this.appCMSBeaconRest = appCMSBeaconRest;
        this.userAgent = System.getProperty("http.agent");
    }

    @WorkerThread
    public void call(String url, String xApiKey, final Action1<BeaconResponse> action1, AppCMSBeaconRequest request) {

        try {
            Map<String, String> authTokenMap = new HashMap<>();
            authTokenMap.put("Content-Type", "application/json");
            authTokenMap.put("user-agent", userAgent);
//            authTokenMap.put("x-api-key", xApiKey);

            Log.d(TAG,"Beacon request URL: " + url);
            Log.d(TAG, "Beacon request headers: " + authTokenMap);
            //Log.d(TAG, "Beacon request value: " + new Gson().toJson(request));

            Call<BeaconResponse> call;
            call = appCMSBeaconRest.sendBeaconMessage(url, authTokenMap, request.getBeaconRequest());
            call.enqueue(new Callback<BeaconResponse>() {

                @Override
                public void onResponse(Call<BeaconResponse> call, Response<BeaconResponse> response) {
                    Observable.just(response.body())
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(action1);
                }

                @Override
                public void onFailure(Call<BeaconResponse> call, Throwable t) {
                    Log.e(TAG, "Beacon  onFailure: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
