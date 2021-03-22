package com.viewlift.models.network.rest;

/*
 * Created by Viewlift on 7/10/17.
 */

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import android.util.Log;

import com.google.gson.Gson;
import com.viewlift.models.data.appcms.api.AddToWatchlistRequest;
import com.viewlift.models.data.appcms.likes.LikeRequest;
import com.viewlift.models.data.appcms.likes.LikeResult;
import com.viewlift.models.data.appcms.likes.Likes;
import com.viewlift.models.data.appcms.watchlist.AppCMSAddToWatchlistResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

public class AppCMSAddToWatchlistCall {

    private static final String TAG = "AppCMSAddToWatchlistTAG";
    private final AppCMSAddToWatchlistRest appCMSAddToWatchlistRest;

    @SuppressWarnings({"unused, FieldCanBeLocal"})
    private final Gson gson;

    @Inject
    public AppCMSAddToWatchlistCall(AppCMSAddToWatchlistRest appCMSAddToWatchlistRest, Gson gson) {
        this.appCMSAddToWatchlistRest = appCMSAddToWatchlistRest;
        this.gson = gson;
    }

    @WorkerThread
    public void call(String url, String authToken, String xApi,
                     final Action1<AppCMSAddToWatchlistResult> addToWatchlistResultAction1,
                     AddToWatchlistRequest request, boolean add) throws Exception {
        try {
            Map<String, String> authTokenMap = new HashMap<>();
            authTokenMap.put("Authorization", authToken);
            authTokenMap.put("x-api-key", xApi);
            Call<AppCMSAddToWatchlistResult> call;
            if (add) {
                call = appCMSAddToWatchlistRest.add(url, authTokenMap, request);
            } else {
                call = appCMSAddToWatchlistRest.removeSingle(url, authTokenMap, request);
            }

            call.enqueue(new Callback<AppCMSAddToWatchlistResult>() {
                @Override
                public void onResponse(@NonNull Call<AppCMSAddToWatchlistResult> call,
                                       @NonNull Response<AppCMSAddToWatchlistResult> response) {
                    try {
                        Log.d(TAG, "API Response Code: " + response.code());
                        if (response.code() == 400
                                && response.errorBody() != null
                                && response.errorBody().string().toLowerCase().contains("queue already exists")) {
                            AppCMSAddToWatchlistResult result = new AppCMSAddToWatchlistResult();
                            result.setMessage("queue already exists");
                            Observable.just(result)
                                    .onErrorResumeNext(throwable -> Observable.empty())
                                    .subscribe(addToWatchlistResultAction1);
                        } else if (response.body() == null) {
                            addToWatchlistResultAction1.call(null);
                        } else {
                            Observable.just(response.body())
                                    .onErrorResumeNext(throwable -> Observable.empty())
                                    .subscribe(addToWatchlistResultAction1);
                        }
                    } catch (IOException e) {
                        addToWatchlistResultAction1.call(null);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AppCMSAddToWatchlistResult> call,
                                      @NonNull Throwable t) {
//                    Log.e(TAG, "onFailure: " + t.getMessage());
                    addToWatchlistResultAction1.call(null);
                }
            });
        } catch (Exception e) {
            //Log.e(TAG, "Failed to execute add to watchlist " + url + ": " + e.toString());
        }
    }

    public  void callAddLike(String url, String authToken, String xApi, LikeRequest request,
                             Action1<LikeResult> likeResultAction){

        try{
            Map<String, String> authTokenMap = new HashMap<>();
            authTokenMap.put("Authorization", authToken);
            authTokenMap.put("x-api-key", xApi);
            Call<LikeResult> call;

            appCMSAddToWatchlistRest.addLike(url,authTokenMap,request).enqueue(new Callback<LikeResult>() {
                @Override
                public void onResponse(Call<LikeResult> call, Response<LikeResult> response) {
                    if (response.body() == null) {
                        likeResultAction.call(null);
                    } else {
                        Observable.just(response.body())
                                .onErrorResumeNext(throwable -> Observable.empty())
                                .subscribe(likeResultAction);
                    }
                }

                @Override
                public void onFailure(Call<LikeResult> call, Throwable t) {
                    likeResultAction.call(null);
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public  void callLikeStatus(String url, String authToken, String xApi,
                             Action1<LikeResult> likeResultAction){

        try{
            Map<String, String> authTokenMap = new HashMap<>();
            authTokenMap.put("Authorization", authToken);
            authTokenMap.put("x-api-key", xApi);
            Call<LikeResult> call;

            appCMSAddToWatchlistRest.getLike(url,authTokenMap).enqueue(new Callback<LikeResult>() {
                @Override
                public void onResponse(Call<LikeResult> call, Response<LikeResult> response) {
                    if (response.body() == null) {
                        likeResultAction.call(null);
                    } else {
                        Observable.just(response.body())
                                .onErrorResumeNext(throwable -> Observable.empty())
                                .subscribe(likeResultAction);
                    }
                }

                @Override
                public void onFailure(Call<LikeResult> call, Throwable t) {
                    likeResultAction.call(null);
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public  void deleteLike(String url, String authToken, String xApi,
                                Action1<LikeResult> likeResultAction){

        try{
            Map<String, String> authTokenMap = new HashMap<>();
            authTokenMap.put("Authorization", authToken);
            authTokenMap.put("x-api-key", xApi);
            Call<LikeResult> call;

            appCMSAddToWatchlistRest.removeLike(url,authTokenMap).enqueue(new Callback<LikeResult>() {
                @Override
                public void onResponse(Call<LikeResult> call, Response<LikeResult> response) {
                    if (response.body() == null && response.code()==200) {
                        LikeResult likeResult =new LikeResult();
                        likeResult.setContentId(url);
                        Observable.just(likeResult)
                                .onErrorResumeNext(throwable -> Observable.empty())
                                .subscribe(likeResultAction);
                    }
                    if (response.body() == null) {
                        likeResultAction.call(null);
                    } else {
                        Observable.just(response.body())
                                .onErrorResumeNext(throwable -> Observable.empty())
                                .subscribe(likeResultAction);
                    }
                }

                @Override
                public void onFailure(Call<LikeResult> call, Throwable t) {
                    likeResultAction.call(null);
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public  void countLike(String url, String authToken, String xApi,
                            Action1<Likes> likeResultAction){

        try{
            Map<String, String> authTokenMap = new HashMap<>();
            authTokenMap.put("Authorization", authToken);
            authTokenMap.put("x-api-key", xApi);
            Call<LikeResult> call;

            appCMSAddToWatchlistRest.countLike(url,authTokenMap).enqueue(new Callback<Likes>() {
                @Override
                public void onResponse(Call<Likes> call, Response<Likes> response) {
                    if (response.body() == null) {
                        likeResultAction.call(null);
                    } else {
                        Observable.just(response.body())
                                .onErrorResumeNext(throwable -> Observable.empty())
                                .subscribe(likeResultAction);
                    }
                }

                @Override
                public void onFailure(Call<Likes> call, Throwable t) {
                    likeResultAction.call(null);
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
