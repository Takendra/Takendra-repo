package com.viewlift.models.network.rest;

import com.viewlift.models.data.appcms.api.AppCMSLocationResponse;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

public class AppCMSLocationCall {

    private AppCMSLocationRest appCMSLocationRest;

    @Inject
    public AppCMSLocationCall(AppCMSLocationRest appCMSLocationRest) {
        this.appCMSLocationRest = appCMSLocationRest;

    }

    public void getCurrentLocation(String url, Action1<AppCMSLocationResponse> locationResponseAction) {
        try {
            appCMSLocationRest.getCurrentLocation(url).enqueue(new Callback<AppCMSLocationResponse>() {
                @Override
                public void onResponse(Call<AppCMSLocationResponse> createOrder, Response<AppCMSLocationResponse> response) {
                    Observable.just(response.body())
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(locationResponseAction);
                }

                @Override
                public void onFailure(Call<AppCMSLocationResponse> call, Throwable t) {
                    Observable.just((AppCMSLocationResponse) null)
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(locationResponseAction);
                }
            });

        } catch (Exception e) {
            //Log.e(TAG, "Failed to retrieve IP based Geolocation: " + e.toString());
            Observable.just((AppCMSLocationResponse) null)
                    .onErrorResumeNext(throwable -> Observable.empty())
                    .subscribe(locationResponseAction);
        }
    }
}
