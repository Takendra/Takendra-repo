package com.viewlift.models.network.rest;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.google.gson.Gson;
import com.viewlift.models.data.appcms.api.DeleteHistoryRequest;
import com.viewlift.models.data.appcms.history.AppCMSDeleteHistoryResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

public class AppCMSDeleteHistoryCall {

    private static final String TAG = "AppCMSDeleteHistoryTAG_";
    private final AppCMSDeleteHistoryRest appCMSDeleteHistoryRest;

    @SuppressWarnings({"unused, FieldCanBeLocal"})
    private final Gson gson;

    @Inject
    public AppCMSDeleteHistoryCall(AppCMSDeleteHistoryRest appCMSDeleteHistoryRest, Gson gson) {
        this.appCMSDeleteHistoryRest = appCMSDeleteHistoryRest;
        this.gson = gson;
    }

    @WorkerThread
    public void call(String url, String authToken, String xApiKey,
                     final Action1<List<AppCMSDeleteHistoryResult>> appCMSDeleteHistoryResultAction1,
                     DeleteHistoryRequest request, boolean post) throws Exception {
        try {
            Map<String, String> authTokenMap = new HashMap<>();
            authTokenMap.put("Authorization", authToken);
            authTokenMap.put("x-api-key", xApiKey);
            Call<List<AppCMSDeleteHistoryResult>> call;
            if (post) {
                call = appCMSDeleteHistoryRest.post(url, authTokenMap, request);
            } else {
                call = appCMSDeleteHistoryRest.removeSingle(url, authTokenMap, request);
            }

            call.enqueue(new Callback<List<AppCMSDeleteHistoryResult>>() {
                @Override
                public void onResponse(@NonNull Call<List<AppCMSDeleteHistoryResult>> call,
                                       @NonNull Response<List<AppCMSDeleteHistoryResult>> response) {
                    Observable.just(response.body())
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(appCMSDeleteHistoryResultAction1);
                }

                @Override
                public void onFailure(@NonNull Call<List<AppCMSDeleteHistoryResult>> call,
                                      @NonNull Throwable t) {
                    //Log.e(TAG, "onFailure: " + t.getMessage());
                    appCMSDeleteHistoryResultAction1.call(null);
                }
            });
        } catch (Exception e) {
            //Log.e(TAG, "Failed to execute delete History " + url + ": " + e.toString());
        }
    }

    @WorkerThread
    public void delete(String url, String authToken, String xApiKey,
                       final Action1<AppCMSDeleteHistoryResult> appCMSDeleteHistoryResultAction1) throws Exception {
        try {
            Map<String, String> authTokenMap = new HashMap<>();
            authTokenMap.put("Authorization", authToken);
            authTokenMap.put("x-api-key", xApiKey);
            Call<AppCMSDeleteHistoryResult> call = appCMSDeleteHistoryRest.delete(url, authTokenMap);
            call.enqueue(new Callback<AppCMSDeleteHistoryResult>() {
                @Override
                public void onResponse(@NonNull Call<AppCMSDeleteHistoryResult> call,
                                       @NonNull Response<AppCMSDeleteHistoryResult> response) {
                    Observable.just(response.body())
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(appCMSDeleteHistoryResultAction1);
                }

                @Override
                public void onFailure(@NonNull Call<AppCMSDeleteHistoryResult> call,
                                      @NonNull Throwable t) {
                    //Log.e(TAG, "onFailure: " + t.getMessage());
                    appCMSDeleteHistoryResultAction1.call(null);
                }
            });
        } catch (Exception e) {
            //Log.e(TAG, "Failed to execute delete History " + url + ": " + e.toString());
        }
    }
}
