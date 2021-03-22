package com.viewlift.models.network.rest;

import com.viewlift.models.data.appcms.api.AppCMSEmailConsentValue;

import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by viewlift on 8/1/17.
 */

public class AppCMSEmailConsentCall {
    private static final String TAG = "IPGeoLocator";

    private final AppCMSEmailConsentRest appCMSEmailConsentRest;

    @Inject
    public AppCMSEmailConsentCall(AppCMSEmailConsentRest appCMSEmailConsentRest) {
        this.appCMSEmailConsentRest = appCMSEmailConsentRest;
    }

    public void call(String url,
                     Action1<Map<String, AppCMSEmailConsentValue>> readyAction) {

        try {
            appCMSEmailConsentRest.get(url).enqueue(new Callback<Map<String, AppCMSEmailConsentValue>>() {
                @Override
                public void onResponse(Call<Map<String, AppCMSEmailConsentValue>> call,
                                       Response<Map<String, AppCMSEmailConsentValue>> response) {
                    Observable.just(response.body())
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(readyAction);
                }

                @Override
                public void onFailure(Call<Map<String, AppCMSEmailConsentValue>> call, Throwable t) {
                    readyAction.call(null);
                }
            });
        } catch (Exception e) {
            //Log.e(TAG, "Failed to retrieve IP based Geolocation: " + e.toString());
            readyAction.call(null);
        }
    }
}
