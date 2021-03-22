package com.viewlift.models.network.rest;

/*
 * Created by Viewlift on 7/5/17.
 */

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.google.gson.Gson;
import com.viewlift.models.data.appcms.history.AppCMSHistoryResult;
import com.viewlift.models.data.appcms.history.AppCMSRecommendationGenreResult;
import com.viewlift.models.data.appcms.history.SeriesHistory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

public class AppCMSHistoryCall {

    private static final String TAG = "AppCMSHistoryCallTAG_";
    private final AppCMSHistoryRest appCMSHistoryRest;

    @SuppressWarnings({"unused, FieldCanBeLocal"})
    private final Gson gson;
    boolean isRefreshingHistoryData = false;
    List<Action1<AppCMSHistoryResult>> results = new ArrayList<Action1<AppCMSHistoryResult>>();

    @Inject
    public AppCMSHistoryCall(AppCMSHistoryRest appCMSHistoryRest, Gson gson) {
        this.appCMSHistoryRest = appCMSHistoryRest;
        this.gson = gson;
    }


    @WorkerThread
    public void call(String url, String authToken,String xApi,
                     final Action1<AppCMSHistoryResult> historyResultAction1) throws IOException {
        if (historyResultAction1 != null) {
            results.add(historyResultAction1);
        }
        if (!isRefreshingHistoryData) {
            isRefreshingHistoryData = true;
            try {
                Map<String, String> authTokenMap = new HashMap<>();
                authTokenMap.put("Authorization", authToken);
                authTokenMap.put("x-api-key", xApi);
                //    Log.i("AppCMSHistory URL:- ",url);
//            Log.i("AppCMSHistory URL:- ",url);
                appCMSHistoryRest.get(url, authTokenMap).enqueue(new Callback<AppCMSHistoryResult>() {
                    @Override
                    public void onResponse(@NonNull Call<AppCMSHistoryResult> call,
                                           @NonNull Response<AppCMSHistoryResult> response) {
                        sendUpdate(response);
                    }

                    @Override
                    public void onFailure(@NonNull Call<AppCMSHistoryResult> call,
                                          @NonNull Throwable t) {
                        sendUpdate(null);
                        //Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });
            } catch (Exception e) {
                sendUpdate(null);
                //Log.e(TAG, "Failed to execute history " + url + ": " + e.toString());
            }
        }
    }

    private void sendUpdate(Response<AppCMSHistoryResult> response){
            if (results != null){
                Log.i("/history","result");
                for (Action1<AppCMSHistoryResult> historyResultAction1: results) {
                    Observable.just(response!= null ? response.body() : null)
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(historyResultAction1);
                }
                isRefreshingHistoryData = false;
                results.clear();
            }
    }

    @WorkerThread
    public void callRecommendation(String url, String authToken, String xApi,
                                   final Action1<AppCMSHistoryResult> historyResultAction1) throws IOException {
            try {
                Map<String, String> authTokenMap = new HashMap<>();
                authTokenMap.put("Authorization", authToken);
                authTokenMap.put("x-api-key", xApi);
                //    Log.i("AppCMSHistory URL:- ",url);
            Log.i("RecommandationURL:- ",url);
                appCMSHistoryRest.get(url, authTokenMap).enqueue(new Callback<AppCMSHistoryResult>() {
                    @Override
                    public void onResponse(@NonNull Call<AppCMSHistoryResult> call,
                                           @NonNull Response<AppCMSHistoryResult> response) {
                    Observable.just(response.body())
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(historyResultAction1);
                    }

                    @Override
                    public void onFailure(@NonNull Call<AppCMSHistoryResult> call,
                                          @NonNull Throwable t) {
                        //Log.e(TAG, "onFailure: " + t.getMessage());
                    Observable.just((AppCMSHistoryResult) null)
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(historyResultAction1);
                    }
                });
            } catch (Exception e) {
                //Log.e(TAG, "Failed to execute history " + url + ": " + e.toString());
            Observable.just((AppCMSHistoryResult) null)
                    .onErrorResumeNext(throwable -> Observable.empty())
                    .subscribe(historyResultAction1);
            }
        }

    @WorkerThread
    public void callRecommendationGenre(String url,
                                        String authToken,
                                        String xApi,
                                        final Action1<AppCMSRecommendationGenreResult> action) {
        try {
            Map<String, String> authTokenMap = new HashMap<>();
            authTokenMap.put("x-api-key", xApi);

            appCMSHistoryRest.getRecommendationGenre(url, authTokenMap).enqueue(new Callback<AppCMSRecommendationGenreResult>() {
                @Override
                public void onResponse(@NonNull Call<AppCMSRecommendationGenreResult> call,
                                       @NonNull Response<AppCMSRecommendationGenreResult> response) {
                    Observable.just(response.body()).subscribe(action);
                }

                @Override
                public void onFailure(@NonNull Call<AppCMSRecommendationGenreResult> call,
                                      @NonNull Throwable t) {
                    //Log.e(TAG, "onFailure: " + t.getMessage());
                    action.call(null);
                }
            });
        } catch (Exception e) {
            //Log.e(TAG, "Failed to execute history " + url + ": " + e.toString());
            Observable.just((AppCMSRecommendationGenreResult) null)
                    .onErrorResumeNext(throwable -> Observable.empty())
                    .subscribe(action);
        }
    }


    @WorkerThread
    public void callSeries(String url, String authToken,String xApi,
                     final Action1<List<SeriesHistory>> historyResultAction1) throws IOException {
        try {
            Map<String, String> authTokenMap = new HashMap<>();
            authTokenMap.put("Authorization", authToken);
            authTokenMap.put("x-api-key", xApi);
            Log.i("AppCMSHistory URL:- ",url);
            appCMSHistoryRest.getSeries(url, authTokenMap).enqueue(new Callback<List<SeriesHistory>>() {
                @Override
                public void onResponse(@NonNull Call<List<SeriesHistory>> call,
                                       @NonNull Response<List<SeriesHistory>> response) {
                    Observable.just(response.body())
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(historyResultAction1);
                }

                @Override
                public void onFailure(@NonNull Call<List<SeriesHistory>> call,
                                      @NonNull Throwable t) {
                    //Log.e(TAG, "onFailure: " + t.getMessage());
                    Observable.just((List<SeriesHistory>) null)
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(historyResultAction1);
                }
            });
        } catch (Exception e) {
            //Log.e(TAG, "Failed to execute history " + url + ": " + e.toString());
            Observable.just((List<SeriesHistory>) null)
                    .onErrorResumeNext(throwable -> Observable.empty())
                    .subscribe(historyResultAction1);
        }
    }
}
