package com.viewlift.models.network.rest;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.viewlift.models.data.appcms.subscriptions.AppCMSRestorePurchaseRequest;
import com.viewlift.models.data.appcms.ui.authentication.RestoreAmazonPurchase;
import com.viewlift.models.data.appcms.ui.authentication.SignInResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

/**
 * Created by viewlift on 9/27/17.
 */

public class AppCMSRestorePurchaseCall {
    private static final String TAG = "RestorePurchaseCall";

    private Gson gson;
    private AppCMSRestorePurchaseRest appCMSRestorePurchaseRest;
    private Map<String, String> authHeaders;

    @Inject
    public AppCMSRestorePurchaseCall(Gson gson,
                                     AppCMSRestorePurchaseRest appCMSRestorePurchaseRest) {
        this.gson = gson;
        this.appCMSRestorePurchaseRest = appCMSRestorePurchaseRest;
        this.authHeaders = new HashMap<>();
    }

    public void call(String apiKey,
                     String url,
                     String purchaseToken,
                     String site,
                     Action1<SignInResponse> readyAction) {

        authHeaders.put("x-api-key", apiKey);

        AppCMSRestorePurchaseRequest appCMSRestorePurchaseRequest = new AppCMSRestorePurchaseRequest();
        appCMSRestorePurchaseRequest.setPaymentUniqueId(purchaseToken);
        appCMSRestorePurchaseRequest.setSite(site);
        Call<JsonElement> call = appCMSRestorePurchaseRest.restorePurchase(authHeaders,
                url,
                appCMSRestorePurchaseRequest);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                SignInResponse parsedSigninResponse = null;
                if (response.body() != null) {
                    try {
                        JsonElement signInResponse = response.body();
                        if (readyAction != null) {
                            //Log.d(TAG, "Received response: " + signInResponse);
                            parsedSigninResponse = gson.fromJson(signInResponse, SignInResponse.class);
                            Observable.just(parsedSigninResponse)
                                    .onErrorResumeNext(throwable -> Observable.empty())
                                    .subscribe(readyAction);
                        }
                    } catch (JsonSyntaxException e) {
                        //Log.e(TAG, "Failed to retrieve sign-in response: " +
//                                e.getMessage());
                        if (readyAction != null) {
                            Observable.just(parsedSigninResponse)
                                    .onErrorResumeNext(throwable -> Observable.empty())
                                    .subscribe(readyAction);
                        }
                    }
                } else if (response.errorBody() != null) {
                    try {
                        String errorResponse = response.errorBody().string();
                        //Log.d(TAG, "Received raw error response: " + errorResponse);
                        if (readyAction != null) {
                            parsedSigninResponse = gson.fromJson(errorResponse, SignInResponse.class);
                            Observable.just(parsedSigninResponse)
                                    .onErrorResumeNext(throwable -> Observable.empty())
                                    .subscribe(readyAction);
                        }
                    } catch (JsonSyntaxException | NullPointerException | IOException e) {
                        //Log.e(TAG, "Failed to retrieve error body: " +
//                                e.getMessage());
                        if (readyAction != null) {
                            Observable.just(parsedSigninResponse)
                                    .onErrorResumeNext(throwable -> Observable.empty())
                                    .subscribe(readyAction);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                //Log.e(TAG, "Failed to retrieve network response for sign-in request: " +
//                        t.getMessage());
                Observable.just((SignInResponse) null)
                        .onErrorResumeNext(throwable -> Observable.empty())
                        .subscribe(readyAction);
            }
        });
    }

    public void restoreAmazonPurchase(String apiKey,
                                      String authToken,
                                      String url,
                                      String amazonUserId,
                                      String platform,
                                      String site,
                                      Action1<RestoreAmazonPurchase> restoreAmazonPurchaseAction1
                                      ){
        AppCMSRestorePurchaseRequest appCMSRestorePurchaseRequest = new AppCMSRestorePurchaseRequest();
        appCMSRestorePurchaseRequest.setPaymentUniqueId(amazonUserId);
        appCMSRestorePurchaseRequest.setSite(site);
        appCMSRestorePurchaseRequest.setPlatform(platform);
        if (authToken != null) {
            authHeaders.put("Authorization", authToken);
        }
        authHeaders.put("x-api-key", apiKey);
        Call<JsonElement> call = appCMSRestorePurchaseRest.restorePurchase(authHeaders,
                url,
                appCMSRestorePurchaseRequest);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                RestoreAmazonPurchase restoreAmazonPurchase = null;
                if (response.body() != null) {
                    try {
                        JsonElement signInResponse = response.body();
                        if (restoreAmazonPurchaseAction1 != null) {
                            //Log.d(TAG, "Received response: " + signInResponse);
                            restoreAmazonPurchase = gson.fromJson(signInResponse, RestoreAmazonPurchase.class);
                            Observable.just(restoreAmazonPurchase)
                                    .onErrorResumeNext(throwable -> Observable.empty())
                                    .subscribe(restoreAmazonPurchaseAction1);
                        }
                    } catch (JsonSyntaxException e) {
                        //Log.e(TAG, "Failed to retrieve sign-in response: " +
//                                e.getMessage());
                        if (restoreAmazonPurchaseAction1 != null) {
                            Observable.just(restoreAmazonPurchase)
                                    .onErrorResumeNext(throwable -> Observable.empty())
                                    .subscribe(restoreAmazonPurchaseAction1);
                        }
                    }
                } else if (response.errorBody() != null) {
                    try {
                        String errorResponse = response.errorBody().string();
                        if (restoreAmazonPurchaseAction1 != null) {
                            Observable.just(restoreAmazonPurchase)
                                    .onErrorResumeNext(throwable -> Observable.empty())
                                    .subscribe(restoreAmazonPurchaseAction1);
                        }
                    } catch (JsonSyntaxException | NullPointerException | IOException e) {

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e(TAG, "Failed to retrieve network response for sign-in request: " +
                        t.getMessage());
                try {
                    if (restoreAmazonPurchaseAction1 != null) {
                        Observable.just(null)
                                .onErrorResumeNext(throwable -> Observable.empty())
                                .subscribe((Observer<? super Object>) restoreAmazonPurchaseAction1);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
