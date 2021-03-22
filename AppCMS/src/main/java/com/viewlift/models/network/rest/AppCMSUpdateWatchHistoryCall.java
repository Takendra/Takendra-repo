package com.viewlift.models.network.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.viewlift.models.data.appcms.history.UpdateHistoryRequest;
import com.viewlift.models.data.appcms.history.UpdateHistoryResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by viewlift on 7/7/17.
 */

public class AppCMSUpdateWatchHistoryCall {
    private final AppCMSUpdateWatchHistoryRest appCMSUpdateWatchHistoryRest;
    private Map<String, String> authHeaders;

    @Inject
    public AppCMSUpdateWatchHistoryCall(AppCMSUpdateWatchHistoryRest appCMSUpdateWatchHistoryRest) {
        this.appCMSUpdateWatchHistoryRest = appCMSUpdateWatchHistoryRest;
        this.authHeaders = new HashMap<>();
    }

    public void call(String url,
                     String authToken,
                     String xApiKey,
                     UpdateHistoryRequest updateHistoryRequest,
                     final Action1<UpdateHistoryResponse> readyAction) {
        authHeaders.put("Authorization", authToken);
        authHeaders.put("x-api-key", xApiKey);


        appCMSUpdateWatchHistoryRest.post(url,
                authHeaders,
                updateHistoryRequest)
                .enqueue(new Callback<Void>() {
                    UpdateHistoryResponse updateHistoryResponse = new UpdateHistoryResponse();
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        updateHistoryResponse.setResponseCode(response.code());
                         if(response.code() == 401){
                            Gson gson = new GsonBuilder()
                                    .serializeNulls()
                                    .create();
                            try {
                                updateHistoryResponse = gson.fromJson(response.errorBody().string(), UpdateHistoryResponse.class);
                                updateHistoryResponse.setResponseCode(response.code());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Observable.just(updateHistoryResponse)
                                .onErrorResumeNext(throwable -> Observable.empty())
                                .subscribe(readyAction);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Observable.just(updateHistoryResponse)
                                .onErrorResumeNext(throwable -> Observable.empty())
                                .subscribe(readyAction);
                    }
                });
    }
}
