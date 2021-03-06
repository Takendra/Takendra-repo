package com.viewlift.models.network.rest;

/*
 * Created by Viewlift on 7/19/17.
 */

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.google.gson.Gson;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.SubscriptionRequest;
import com.viewlift.models.data.appcms.subscriptions.AppCMSSubscriptionPlanResult;
import com.viewlift.models.data.appcms.subscriptions.AppCMSUserSubscriptionPlanResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

public class AppCMSSubscriptionPlanCall {

    private static final String TAG = "SubscriptionPlanCall";
    private final AppCMSSubscriptionPlanRest appCMSSubscriptionPlanRest;
    @SuppressWarnings({"unused, FieldCanBeLocal"})
    private final Gson gson;
    private Map<String, String> authHeaders;
    int NO_OF_RETRY;
    static final int MAX_RETRY = 10;
    static final int WAIT_TIME_IN_MILLS = 5000; // 5 seconds

    @Inject
    public AppCMSSubscriptionPlanCall(AppCMSSubscriptionPlanRest appCMSSubscriptionPlanRest,
                                      Gson gson) {
        this.appCMSSubscriptionPlanRest = appCMSSubscriptionPlanRest;
        this.gson = gson;
        this.authHeaders = new HashMap<>();
        NO_OF_RETRY = 0;
    }

    @WorkerThread
    public void call(String url, int subscriptionCallType, SubscriptionRequest request,
                     boolean retryRequired, String apiKey, String authToken,
                     final Action1<List<AppCMSSubscriptionPlanResult>> planResultAction1,
                     final Action1<AppCMSSubscriptionPlanResult> resultAction1,
                     final Action1<AppCMSUserSubscriptionPlanResult> userSubscriptionPlanResult,
                     final Action1<String> errorResponse)
            throws IOException {


        authHeaders.clear();
        if (!TextUtils.isEmpty(apiKey) && !TextUtils.isEmpty(authToken)) {
            authHeaders.put("Authorization", authToken);
            authHeaders.put("x-api-key", apiKey);
        }

        System.out.println("Google InApp Subscription url-"+ url);
        Log.d(TAG, "URL: " + url);
        Log.d(TAG, "Google InApp Subscription Auth: " + authHeaders);
        if(request != null)
        Log.d(TAG, "Google InApp Subscription SubscriptionRequest: " + request.toString());

        switch (subscriptionCallType) {

            case R.string.app_cms_subscription_plan_list_key:
                appCMSSubscriptionPlanRest.getPlanList(url, authHeaders)
                        .enqueue(new Callback<List<AppCMSSubscriptionPlanResult>>() {
                            @Override
                            public void onResponse(@NonNull Call<List<AppCMSSubscriptionPlanResult>> call,
                                                   @NonNull Response<List<AppCMSSubscriptionPlanResult>> response) {
                                try {
//                                    String request = gson.toJson(response.raw().request().body(), SubscriptionRequest.class);
                                    //Log.d(TAG, "URL: " + response.raw().request().url().toString());
                                    //Log.d(TAG, "Request: " + request);
                                    //Log.d(TAG, "Response code: " + response.code());
//                                    String responseValue = gson.toJson(response.body(), AppCMSSubscriptionPlanResult.class);

                                    Observable.just(response.body())
                                            .onErrorResumeNext(throwable -> Observable.empty())
                                            .subscribe(planResultAction1);
                                    String responseValue = gson.toJson(response.body(), AppCMSSubscriptionPlanResult.class);
                                    Log.e(TAG, "Response: " + responseValue);

                                } catch (Exception e) {
                                    //Log.e(TAG, "Exception occurred when sending update subscription: " +
//                                            e.getMessage());
                                    Observable.just((List<AppCMSSubscriptionPlanResult>) null)
                                            .onErrorResumeNext(throwable -> Observable.empty())
                                            .subscribe(planResultAction1);
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<List<AppCMSSubscriptionPlanResult>> call,
                                                  @NonNull Throwable t) {
                                //Log.e(TAG, "onFailure: " + t.getMessage());
                                Observable.just((List<AppCMSSubscriptionPlanResult>) null)
                                        .onErrorResumeNext(throwable -> Observable.empty())
                                        .subscribe(planResultAction1);
                            }
                        });
                break;

            case R.string.app_cms_subscription_subscribed_plan_key:
                appCMSSubscriptionPlanRest.getSubscribedPlan(url, authHeaders).enqueue(new Callback<AppCMSUserSubscriptionPlanResult>() {
                    @Override
                    public void onResponse(Call<AppCMSUserSubscriptionPlanResult> call, Response<AppCMSUserSubscriptionPlanResult> response) {
                        try {
                            if (response.isSuccessful()) {
                                Observable.just(response.body())
                                        .onErrorResumeNext(throwable -> Observable.empty())
                                        .subscribe(userSubscriptionPlanResult);
                            } else if (retryRequired && (NO_OF_RETRY < MAX_RETRY)) {
                                //Thread.sleep(WAIT_TIME_IN_MILLS);
                                Completable.timer(WAIT_TIME_IN_MILLS, TimeUnit.MILLISECONDS).subscribe( () -> {
                                    call(url,
                                            subscriptionCallType,
                                            request,
                                            retryRequired,
                                            apiKey,
                                            authToken,
                                            planResultAction1,
                                            resultAction1,
                                            userSubscriptionPlanResult,
                                            errorResponse);
                                    NO_OF_RETRY++;
                                });
                            } else {
                                Observable.just(response.body())
                                        .onErrorResumeNext(throwable -> Observable.empty())
                                        .subscribe(userSubscriptionPlanResult);
                            }
                        } catch (Exception e) {
                            //Log.e(TAG, "Exception occurred when sending update subscription: " +
//                                    e.getMessage());
                            Observable.just((AppCMSUserSubscriptionPlanResult) null)
                                    .onErrorResumeNext(throwable -> Observable.empty())
                                    .subscribe(userSubscriptionPlanResult);
                        }
                    }

                    @Override
                    public void onFailure(Call<AppCMSUserSubscriptionPlanResult> call, Throwable t) {
                        //Log.e(TAG, "onFailure: " + t.getMessage());
                        Observable.just((AppCMSUserSubscriptionPlanResult) null)
                                .onErrorResumeNext(throwable -> Observable.empty())
                                .subscribe(userSubscriptionPlanResult);
                    }
                });
                break;

            case R.string.app_cms_subscription_plan_create_key:
                //Log.d(TAG, "Headers: " + authHeaders.toString());
                appCMSSubscriptionPlanRest.createPlan(url, authHeaders, request)
                        .enqueue(new Callback<AppCMSSubscriptionPlanResult>() {
                            @Override
                            public void onResponse(@NonNull Call<AppCMSSubscriptionPlanResult> call,
                                                   @NonNull Response<AppCMSSubscriptionPlanResult> response) {
                                try {
//                                    String request = gson.toJson(response.raw().request().body(), SubscriptionRequest.class);
                                    //Log.d(TAG, "URL: " + response.raw().request().url().toString());
                                    //Log.d(TAG, "Request: " + request);
                                    //Log.d(TAG, "Response code: " + response.code());
//                                    String responseValue = gson.toJson(response.body(), AppCMSSubscriptionPlanResult.class);
                                    //Log.d(TAG, "Response: " + responseValue);
                                    if(response.isSuccessful() && response.body() != null){
                                        Observable.just(response.body())
                                                .onErrorResumeNext(throwable -> Observable.empty())
                                                .subscribe(resultAction1);
                                    }else if(NO_OF_RETRY < MAX_RETRY){
                                        Thread.sleep(WAIT_TIME_IN_MILLS);
                                        call(url,
                                                subscriptionCallType,
                                                request,
                                                retryRequired,
                                                apiKey,
                                                authToken,
                                                planResultAction1,
                                                resultAction1,
                                                userSubscriptionPlanResult,
                                                errorResponse);
                                        NO_OF_RETRY ++;
                                    } else{
                                        Observable.just(response.errorBody().string()).onErrorResumeNext(throwable -> Observable.empty()).subscribe(errorResponse);
                                    }
                                } catch (Exception e) {
                                    //Log.e(TAG, "Exception occurred when sending update subscription: " +
//                                            e.getMessage());
                                    if (NO_OF_RETRY < MAX_RETRY) {
                                        try {
                                            Thread.sleep(WAIT_TIME_IN_MILLS);
                                            call(url,
                                                    subscriptionCallType,
                                                    request,
                                                    retryRequired,
                                                    apiKey,
                                                    authToken,
                                                    planResultAction1,
                                                    resultAction1,
                                                    userSubscriptionPlanResult,
                                                    errorResponse);
                                        } catch (InterruptedException | IOException e1) {
                                            e1.printStackTrace();
                                        }
                                    } else {
                                        Observable.just((AppCMSSubscriptionPlanResult) null)
                                                .onErrorResumeNext(throwable -> Observable.empty())
                                                .subscribe(resultAction1);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<AppCMSSubscriptionPlanResult> call,
                                                  @NonNull Throwable t) {
                                //Log.e(TAG, "onFailure: " + t.getMessage());
                                if(NO_OF_RETRY < MAX_RETRY){
                                    try {
                                        Thread.sleep(WAIT_TIME_IN_MILLS);
                                        call(url,
                                                subscriptionCallType,
                                                request,
                                                retryRequired,
                                                apiKey,
                                                authToken,
                                                planResultAction1,
                                                resultAction1,
                                                userSubscriptionPlanResult,
                                                errorResponse);
                                    } catch (InterruptedException | IOException e) {
                                        e.printStackTrace();
                                    }
                                    NO_OF_RETRY ++;
                                }else {
                                    Observable.just((AppCMSSubscriptionPlanResult) null)
                                            .onErrorResumeNext(throwable -> Observable.empty())
                                            .subscribe(resultAction1);
                                }
                            }
                        });
                break;

            case R.string.app_cms_subscription_plan_update_key:
                authHeaders.put("Content-Type", "application/json");
                //Log.d(TAG, "Headers: " + authHeaders.toString());
                appCMSSubscriptionPlanRest.updatePlan(url, authHeaders, request)
                        .enqueue(new Callback<AppCMSSubscriptionPlanResult>() {
                            @Override
                            public void onResponse(@NonNull Call<AppCMSSubscriptionPlanResult> call,
                                                   @NonNull Response<AppCMSSubscriptionPlanResult> response) {
                                try {
//                                    String request = gson.toJson(response.raw().request().body(), SubscriptionRequest.class);
                                    //Log.d(TAG, "URL: " + response.raw().request().url().toString());
                                    //Log.d(TAG, "Request: " + request);
                                    //Log.d(TAG, "Response code: " + response.code());
//                                    String responseValue = gson.toJson(response.body(), AppCMSSubscriptionPlanResult.class);
                                    //Log.d(TAG, "Response: " + responseValue);
                                    if (response.isSuccessful() && response.body() != null) {
                                        Observable.just(response.body())
                                                .onErrorResumeNext(throwable -> Observable.empty())
                                                .subscribe(resultAction1);
                                    }else if(NO_OF_RETRY < MAX_RETRY){
                                        try {
                                            Thread.sleep(WAIT_TIME_IN_MILLS);
                                            call(url,
                                                    subscriptionCallType,
                                                    request,
                                                    retryRequired,
                                                    apiKey,
                                                    authToken,
                                                    planResultAction1,
                                                    resultAction1,
                                                    userSubscriptionPlanResult,
                                                    errorResponse);
                                        } catch (InterruptedException | IOException e) {
                                            e.printStackTrace();
                                        }
                                        NO_OF_RETRY ++;
                                    } else if (response.errorBody() != null) {
                                        AppCMSSubscriptionPlanResult errorResponse =
                                                gson.fromJson(response.errorBody().string(), AppCMSSubscriptionPlanResult.class);
                                        Observable.just(errorResponse)
                                                .onErrorResumeNext(throwable -> Observable.empty())
                                                .subscribe(resultAction1);
                                    } else {
                                        Observable.just((AppCMSSubscriptionPlanResult) null)
                                                .onErrorResumeNext(throwable -> Observable.empty())
                                                .subscribe(resultAction1);
                                    }
                                } catch (Exception e) {
                                    //Log.e(TAG, "Exception occurred when sending update subscription: " +
//                                        e.getMessage());
                                    try {
                                        if(NO_OF_RETRY < MAX_RETRY){
                                            try {
                                                Thread.sleep(WAIT_TIME_IN_MILLS);
                                                call(url,
                                                        subscriptionCallType,
                                                        request,
                                                        retryRequired,
                                                        apiKey,
                                                        authToken,
                                                        planResultAction1,
                                                        resultAction1,
                                                        userSubscriptionPlanResult,
                                                        errorResponse);
                                            } catch (InterruptedException | IOException e1) {
                                                e1.printStackTrace();
                                            }
                                            NO_OF_RETRY ++;
                                        }else if (response.errorBody() != null) {
                                            AppCMSSubscriptionPlanResult errorResponse =
                                                    gson.fromJson(response.errorBody().string(), AppCMSSubscriptionPlanResult.class);
                                            Observable.just(errorResponse)
                                                    .onErrorResumeNext(throwable -> Observable.empty())
                                                    .subscribe(resultAction1);
                                        } else {
                                            Observable.just((AppCMSSubscriptionPlanResult) null)
                                                    .onErrorResumeNext(throwable -> Observable.empty())
                                                    .subscribe(resultAction1);
                                        }
                                    } catch (Exception e1) {
                                        Observable.just((AppCMSSubscriptionPlanResult) null)
                                                .onErrorResumeNext(throwable -> Observable.empty())
                                                .subscribe(resultAction1);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<AppCMSSubscriptionPlanResult> call,
                                                  @NonNull Throwable t) {
                                //Log.e(TAG, "onFailure: " + t.getMessage());
                                if(NO_OF_RETRY < MAX_RETRY){
                                    try {
                                        Thread.sleep(WAIT_TIME_IN_MILLS);
                                        call(url,
                                                subscriptionCallType,
                                                request,
                                                retryRequired,
                                                apiKey,
                                                authToken,
                                                planResultAction1,
                                                resultAction1,
                                                userSubscriptionPlanResult,
                                                errorResponse);
                                    } catch (InterruptedException | IOException e) {
                                        e.printStackTrace();
                                    }
                                    NO_OF_RETRY ++;
                                }else {
                                Observable.just((AppCMSSubscriptionPlanResult) null)
                                        .onErrorResumeNext(throwable -> Observable.empty())
                                        .subscribe(resultAction1);
                                }
                            }
                        });
                break;

            case R.string.app_cms_subscription_plan_cancel_key:
                appCMSSubscriptionPlanRest.cancelPlan(url, authHeaders, request)
                        .enqueue(new Callback<AppCMSSubscriptionPlanResult>() {
                            @Override
                            public void onResponse(@NonNull Call<AppCMSSubscriptionPlanResult> call,
                                                   @NonNull Response<AppCMSSubscriptionPlanResult> response) {
                                try {
//                                    String request = gson.toJson(response.raw().request().body(), SubscriptionRequest.class);
                                    //Log.d(TAG, "Request: " + request);
                                    //Log.d(TAG, "Response code: " + response.code());
//                                    String responseValue = gson.toJson(response.body(), AppCMSSubscriptionPlanResult.class);
                                    //Log.d(TAG, "Response: " + responseValue);
                                    if(response.isSuccessful() && response.body() !=null){
                                        Observable.just(response.body())
                                                .onErrorResumeNext(throwable -> Observable.empty())
                                                .subscribe(resultAction1);
                                    }else if(NO_OF_RETRY < MAX_RETRY){
                                        try {
                                            Thread.sleep(WAIT_TIME_IN_MILLS);
                                            call(url,
                                                    subscriptionCallType,
                                                    request,
                                                    retryRequired,
                                                    apiKey,
                                                    authToken,
                                                    planResultAction1,
                                                    resultAction1,
                                                    userSubscriptionPlanResult,
                                                    errorResponse);
                                        } catch (InterruptedException | IOException e) {
                                            e.printStackTrace();
                                        }
                                        NO_OF_RETRY ++;
                                    } else {
                                        Observable.just(response.body())
                                                .onErrorResumeNext(throwable -> Observable.empty())
                                                .subscribe(resultAction1);
                                    }
                                } catch (Exception e) {
                                    if(NO_OF_RETRY < MAX_RETRY){
                                        try {
                                            Thread.sleep(WAIT_TIME_IN_MILLS);
                                            call(url,
                                                    subscriptionCallType,
                                                    request,
                                                    retryRequired,
                                                    apiKey,
                                                    authToken,
                                                    planResultAction1,
                                                    resultAction1,
                                                    userSubscriptionPlanResult,
                                                    errorResponse);
                                        } catch (InterruptedException | IOException e1) {
                                            e1.printStackTrace();
                                        }
                                        NO_OF_RETRY ++;
                                    }else {
                                    Observable.just((AppCMSSubscriptionPlanResult) null)
                                            .onErrorResumeNext(throwable -> Observable.empty())
                                            .subscribe(resultAction1);}

                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<AppCMSSubscriptionPlanResult> call,
                                                  @NonNull Throwable t) {
                                //Log.e(TAG, "onFailure: " + t.getMessage());
                                if(NO_OF_RETRY < MAX_RETRY){
                                    try {
                                        Thread.sleep(WAIT_TIME_IN_MILLS);
                                        call(url,
                                                subscriptionCallType,
                                                request,
                                                retryRequired,
                                                apiKey,
                                                authToken,
                                                planResultAction1,
                                                resultAction1,
                                                userSubscriptionPlanResult,
                                                errorResponse);
                                    } catch (InterruptedException | IOException e) {
                                        e.printStackTrace();
                                    }
                                    NO_OF_RETRY ++;
                                }else {
                                Observable.just((AppCMSSubscriptionPlanResult) null)
                                        .onErrorResumeNext(throwable -> Observable.empty())
                                        .subscribe(resultAction1);
                                }
                            }
                        });
                break;

            case R.string.app_cms_check_ccavenue_plan_status_key:
                appCMSSubscriptionPlanRest.checkCCAvenueUpgradeStatus(url, authHeaders, request)
                        .enqueue(new Callback<AppCMSSubscriptionPlanResult>() {
                            @Override
                            public void onResponse(@NonNull Call<AppCMSSubscriptionPlanResult> call,
                                                   @NonNull Response<AppCMSSubscriptionPlanResult> response) {
                                try {
//                                    String request = gson.toJson(response.raw().request().body(), SubscriptionRequest.class);
                                    //Log.d(TAG, "URL: " + response.raw().request().url().toString());
                                    //Log.d(TAG, "Request: " + request);
                                    //Log.d(TAG, "Response code: " + response.code());
//                                    String responseValue = gson.toJson(response.body(), AppCMSSubscriptionPlanResult.class);
                                    //Log.d(TAG, "Response: " + responseValue);
                                    if(response.isSuccessful() && response.body() != null){
                                    Observable.just(response.body())
                                            .onErrorResumeNext(throwable -> Observable.empty())
                                            .subscribe(resultAction1);
                                    }else if(NO_OF_RETRY < MAX_RETRY){
                                        try {
                                            Thread.sleep(WAIT_TIME_IN_MILLS);
                                            call(url,
                                                    subscriptionCallType,
                                                    request,
                                                    retryRequired,
                                                    apiKey,
                                                    authToken,
                                                    planResultAction1,
                                                    resultAction1,
                                                    userSubscriptionPlanResult,
                                                    errorResponse);
                                        } catch (InterruptedException | IOException e) {
                                            e.printStackTrace();
                                        }
                                        NO_OF_RETRY ++;
                                    } else {
                                        Observable.just(response.body())
                                                .onErrorResumeNext(throwable -> Observable.empty())
                                                .subscribe(resultAction1);
                                    }
                                } catch (Exception e) {
                                    if(NO_OF_RETRY < MAX_RETRY){
                                        try {
                                            Thread.sleep(WAIT_TIME_IN_MILLS);
                                            call(url,
                                                    subscriptionCallType,
                                                    request,
                                                    retryRequired,
                                                    apiKey,
                                                    authToken,
                                                    planResultAction1,
                                                    resultAction1,
                                                    userSubscriptionPlanResult,
                                                    errorResponse);
                                        } catch (InterruptedException | IOException e1) {
                                            e1.printStackTrace();
                                        }
                                        NO_OF_RETRY ++;
                                    }else {
                                    Observable.just((AppCMSSubscriptionPlanResult) null)
                                            .onErrorResumeNext(throwable -> Observable.empty())
                                            .subscribe(resultAction1);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<AppCMSSubscriptionPlanResult> call,
                                                  @NonNull Throwable t) {
                                //Log.e(TAG, "onFailure: " + t.getMessage());
                                if(NO_OF_RETRY < MAX_RETRY){
                                    try {
                                        Thread.sleep(WAIT_TIME_IN_MILLS);
                                        call(url,
                                                subscriptionCallType,
                                                request,
                                                retryRequired,
                                                apiKey,
                                                authToken,
                                                planResultAction1,
                                                resultAction1,
                                                userSubscriptionPlanResult,
                                                errorResponse);
                                    } catch (InterruptedException | IOException e) {
                                        e.printStackTrace();
                                    }
                                    NO_OF_RETRY ++;
                                } else {
                                Observable.just((AppCMSSubscriptionPlanResult) null)
                                        .onErrorResumeNext(throwable -> Observable.empty())
                                        .subscribe(resultAction1);
                                }
                            }
                        });
                break;
            case R.string.app_cms_subscription_receipt_validate_key:
                appCMSSubscriptionPlanRest.validate(url, authHeaders, request)
                        .enqueue(new Callback<AppCMSSubscriptionPlanResult>() {
                            @Override
                            public void onResponse(Call<AppCMSSubscriptionPlanResult> call, Response<AppCMSSubscriptionPlanResult> response) {
                                try {
                                    Observable.just(response.body())
                                            .onErrorResumeNext(throwable -> Observable.empty())
                                            .subscribe(resultAction1);
                                } catch (Exception e) {
                                    Observable.just((AppCMSSubscriptionPlanResult) null)
                                            .onErrorResumeNext(throwable -> Observable.empty())
                                            .subscribe(resultAction1);
                                }
                            }

                            @Override
                            public void onFailure(Call<AppCMSSubscriptionPlanResult> call, Throwable t) {
                                Observable.just((AppCMSSubscriptionPlanResult) null)
                                        .onErrorResumeNext(throwable -> Observable.empty())
                                        .subscribe(resultAction1);
                            }
                        });
                break;
            default:
                throw new RuntimeException("Invalid SubscriptionCallType: " + subscriptionCallType);
        }
    }

    @WorkerThread
    public void fetchSubscriptionPlans(String url, String apiKey, String authToken,
                                       final Consumer<List<ContentDatum>> subscriptionPlans) {
        authHeaders.clear();
        if (!TextUtils.isEmpty(apiKey) && !TextUtils.isEmpty(authToken)) {
            authHeaders.put("Authorization", authToken);
            authHeaders.put("x-api-key", apiKey);
        }
        appCMSSubscriptionPlanRest.getPlansById(url, authHeaders).enqueue(new Callback<List<ContentDatum>>() {
            @Override
            public void onResponse(Call<List<ContentDatum>> call, Response<List<ContentDatum>> response) {
                try {
                    io.reactivex.rxjava3.core.Observable.just(response.body() != null ? response.body() : new ArrayList<ContentDatum>())
                            .onErrorResumeNext(throwable -> io.reactivex.rxjava3.core.Observable.empty())
                            .subscribe(subscriptionPlans);
                } catch (Exception e) {
                    io.reactivex.rxjava3.core.Observable.just(new ArrayList<ContentDatum>())
                            .onErrorResumeNext(throwable -> io.reactivex.rxjava3.core.Observable.empty())
                            .subscribe(subscriptionPlans);
                }
            }
            @Override
            public void onFailure(Call<List<ContentDatum>> call, Throwable t) {
                io.reactivex.rxjava3.core.Observable.just(new ArrayList<ContentDatum>())
                        .onErrorResumeNext(throwable -> io.reactivex.rxjava3.core.Observable.empty())
                        .subscribe(subscriptionPlans);
            }
        });
    }
}
