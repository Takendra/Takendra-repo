package com.viewlift.models.network.rest;

import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viewlift.models.data.appcms.api.AppCMSCardInfoResponse;
import com.viewlift.models.data.appcms.api.AppCMSDeleteCardResponse;
import com.viewlift.models.data.appcms.api.AppCMSSavedCardsResponse;
import com.viewlift.models.data.appcms.api.JuspayOrderRequest;
import com.viewlift.models.data.appcms.api.OfferCodeResponse;
import com.viewlift.models.data.appcms.api.OfferDiscount;
import com.viewlift.models.data.appcms.subscriptions.AppCMSUserSubscriptionPlanResult;
import com.viewlift.models.data.appcms.ui.authentication.ErrorResponse;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

public  class AppCMSJuspayCall {
    private static final String TAG = "JuspayCall";

    private final AppCMSJuspayRest appCMSJuspayRest;

    private Map<String, String> authHeaders;

    @SuppressWarnings({"unused, FieldCanBeLocal"})
    private final Gson gson;

    @Inject
    public AppCMSJuspayCall(AppCMSJuspayRest appCMSJuspayRest, Gson gson) {
        this.appCMSJuspayRest = appCMSJuspayRest;
        this.authHeaders = new HashMap<>();
        this.gson = gson;
    }


    public void createOrder(String url, String authToken,
                            String apiKey, Action1<AppCMSUserSubscriptionPlanResult> juspayOrder, JuspayOrderRequest appCMSJuspayOrderRequest){
        authHeaders.clear();
        if (!TextUtils.isEmpty(authToken) && !TextUtils.isEmpty(apiKey)) {
            authHeaders.put("Authorization", authToken);
            authHeaders.put("Content-Type","application/json");
            authHeaders.put("x-api-key", apiKey);
        }

        try {
            appCMSJuspayRest.createJuspayOrder(url, appCMSJuspayOrderRequest,authHeaders).enqueue(new Callback<AppCMSUserSubscriptionPlanResult>() {
                @Override
                public void onResponse(Call<AppCMSUserSubscriptionPlanResult> createOrder, Response<AppCMSUserSubscriptionPlanResult> response) {
                    Observable.just(response.body())
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(juspayOrder);
                }

                @Override
                public void onFailure(Call<AppCMSUserSubscriptionPlanResult> call, Throwable t) {
                    Observable.just((AppCMSUserSubscriptionPlanResult) null)
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(juspayOrder);
                }
            });

        } catch (Exception e) {
            //Log.e(TAG, "Failed to retrieve IP based Geolocation: " + e.toString());
            Observable.just((AppCMSUserSubscriptionPlanResult) null)
                    .onErrorResumeNext(throwable -> Observable.empty())
                    .subscribe(juspayOrder);
        }
    }

    // get saved Card
    public void getSavedCards(String url, String authToken, String apiKey, Action1<AppCMSSavedCardsResponse> savedCardResponseAction) {
        authHeaders.clear();
        if (!TextUtils.isEmpty(authToken) && !TextUtils.isEmpty(apiKey)) {
            authHeaders.put("Authorization", authToken);
            authHeaders.put("Content-Type","application/json");
            authHeaders.put("x-api-key", apiKey);
        }


        try {
            appCMSJuspayRest.getUserSavedCards(url, authHeaders).enqueue(new Callback<AppCMSSavedCardsResponse>() {
                @Override
                public void onResponse(Call<AppCMSSavedCardsResponse> createOrder, Response<AppCMSSavedCardsResponse> response) {
                    Observable.just(response.body())
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(savedCardResponseAction);
                }

                @Override
                public void onFailure(Call<AppCMSSavedCardsResponse> call, Throwable t) {
                    //Log.e(TAG, "Failed to retrieve IP based Geolocation: " + t.getMessage());
                    Observable.just((AppCMSSavedCardsResponse) null)
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(savedCardResponseAction);
                }
            });

        } catch (Exception e) {
            //Log.e(TAG, "Failed to retrieve IP based Geolocation: " + e.toString());
            Observable.just((AppCMSSavedCardsResponse) null)
                    .onErrorResumeNext(throwable -> Observable.empty())
                    .subscribe(savedCardResponseAction);
        }
    }

    // delete Saved Card
    public void deleteSavedCard(String url, String authToken, String apiKey, Map<String, String> params, Action1<AppCMSDeleteCardResponse> deleteResponseAction) {
        authHeaders.clear();
        if (!TextUtils.isEmpty(authToken) && !TextUtils.isEmpty(apiKey)) {
            authHeaders.put("Authorization", authToken);
            authHeaders.put("Content-Type","application/json");
            authHeaders.put("x-api-key", apiKey);
        }

        try {
            appCMSJuspayRest.deleteJuspaySavedCard(url, params, authHeaders).enqueue(new Callback<AppCMSDeleteCardResponse>() {
                @Override
                public void onResponse(Call<AppCMSDeleteCardResponse> createOrder, Response<AppCMSDeleteCardResponse> response) {
                    Observable.just(response.body())
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(deleteResponseAction);
                }

                @Override
                public void onFailure(Call<AppCMSDeleteCardResponse> call, Throwable t) {
                    //Log.e(TAG, "Failed to retrieve IP based Geolocation: " + t.getMessage());
                    Observable.just((AppCMSDeleteCardResponse) null)
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(deleteResponseAction);
                }
            });

        } catch (Exception e) {
            //Log.e(TAG, "Failed to retrieve IP based Geolocation: " + e.toString());
            Observable.just((AppCMSDeleteCardResponse) null)
                    .onErrorResumeNext(throwable -> Observable.empty())
                    .subscribe(deleteResponseAction);
        }
    }

    // get card info
    public void getCardInfo(String url, Action1<AppCMSCardInfoResponse> cardInfoAction){
        try {
            appCMSJuspayRest.getCardInfo(url).enqueue(new Callback<AppCMSCardInfoResponse>() {
                @Override
                public void onResponse(Call<AppCMSCardInfoResponse> createOrder, Response<AppCMSCardInfoResponse> response) {
                    Observable.just(response.body())
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(cardInfoAction);
                }

                @Override
                public void onFailure(Call<AppCMSCardInfoResponse> call, Throwable t) {
                    //Log.e(TAG, "Failed to retrieve IP based Geolocation: " + t.getMessage());
                    Observable.just((AppCMSCardInfoResponse) null)
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(cardInfoAction);
                }
            });

        } catch (Exception e) {
            //Log.e(TAG, "Failed to retrieve IP based Geolocation: " + e.toString());
            Observable.just((AppCMSCardInfoResponse) null)
                    .onErrorResumeNext(throwable -> Observable.empty())
                    .subscribe(cardInfoAction);
        }
    }

    public void validateOfferCode(String url, String authToken, String apiKey, Map<String, String> params, Action1<Pair<OfferCodeResponse, ErrorResponse>> validateOfferCodeAction){
        try {
            authHeaders.clear();
            if (!TextUtils.isEmpty(authToken) && !TextUtils.isEmpty(apiKey)) {
                authHeaders.put("Authorization", authToken);
                authHeaders.put("Content-Type","application/json");
                authHeaders.put("x-api-key", apiKey);
            }

            appCMSJuspayRest.validateOfferCode(url, params, authHeaders).enqueue(new Callback<OfferCodeResponse>() {
                @Override
                public void onResponse(Call<OfferCodeResponse> call, Response<OfferCodeResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        validateOfferCodeAction.call(new Pair<>(response.body(), null));
                    } else {
                        try {
                            Type type = new TypeToken<ErrorResponse>() {}.getType();
                            ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().charStream(),type);
                            validateOfferCodeAction.call(new Pair<>(null, errorResponse));
                        } catch (Exception e) {
                            e.printStackTrace();
                            validateOfferCodeAction.call(new Pair<>(null, null));
                        }
                    }

                }

                @Override
                public void onFailure(Call<OfferCodeResponse> call, Throwable t) {
                    validateOfferCodeAction.call(new Pair<>(null, null));
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Failed to retrieve: " + e);
            validateOfferCodeAction.call(new Pair<>(null, null));
        }
    }

    public void calculateDiscount(String url, String authToken, String apiKey, Action1<Pair<OfferDiscount, ErrorResponse>>  offerDiscountAction1){
        authHeaders.clear();
        if (!TextUtils.isEmpty(authToken) && !TextUtils.isEmpty(apiKey)) {
            authHeaders.put("Authorization", authToken);
            authHeaders.put("Content-Type","application/json");
            authHeaders.put("x-api-key", apiKey);
        }

        try {
            appCMSJuspayRest.calculateDiscount(url,authHeaders).enqueue(new Callback<OfferDiscount>() {
                @Override
                public void onResponse(Call<OfferDiscount> call, Response<OfferDiscount> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        offerDiscountAction1.call(new Pair<>(response.body(), null));
                    } else {
                        try {
                            Type type = new TypeToken<ErrorResponse>() {}.getType();
                            ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().charStream(),type);
                            offerDiscountAction1.call(new Pair<>(null, errorResponse));
                        } catch (Exception e) {
                            e.printStackTrace();
                            offerDiscountAction1.call(new Pair<>(null, null));
                        }
                    }
                }

                @Override
                public void onFailure(Call<OfferDiscount> call, Throwable t) {
                    offerDiscountAction1.call(new Pair<>(null, null));
                }
            });

        } catch (Exception e) {
            //Log.e(TAG, "Failed to retrieve IP based Geolocation: " + e.toString());
            offerDiscountAction1.call(new Pair<>(null, null));
        }
    }


}