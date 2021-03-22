package com.viewlift.models.network.rest;

import android.text.TextUtils;

import com.viewlift.models.data.appcms.api.IPGeoLocatorResponse;
import com.viewlift.models.data.appcms.weather.Cities;
import com.viewlift.models.data.appcms.weather.TickerFeed;

import java.util.HashMap;
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

public class AppCMSWeatherFeedCall {
    private static final String TAG = "IPGeoLocator";

    private final AppCMSWeatherFeedRest appCMSWeatherFeedRest;

    private Map<String, String> authHeaders;

    @Inject
    public AppCMSWeatherFeedCall(AppCMSWeatherFeedRest appCMSWeatherFeedRest) {
        this.appCMSWeatherFeedRest = appCMSWeatherFeedRest;
        this.authHeaders = new HashMap<>();
    }

    public void call(String url,
                     Action1<Cities> readyAction) {
        /*authHeaders.clear();
        if (!TextUtils.isEmpty(authToken) && !TextUtils.isEmpty(apiKey)) {
            authHeaders.put("Authorization", authToken);
            authHeaders.put("x-api_key", apiKey);
        }
*/
        try {
            appCMSWeatherFeedRest.get(url).enqueue(new Callback<Cities>() {
                @Override
                public void onResponse(Call<Cities> call, Response<Cities> response) {
                    Observable.just(response.body())
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(readyAction);
                }

                @Override
                public void onFailure(Call<Cities> call, Throwable t) {
                    //Log.e(TAG, "Failed to retrieve IP based Geolocation: " + t.getMessage());
                    Observable.just((Cities) null)
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(readyAction);
                }
            });
        } catch (Exception e) {
            //Log.e(TAG, "Failed to retrieve IP based Geolocation: " + e.toString());
            Observable.just((Cities) null)
                    .onErrorResumeNext(throwable -> Observable.empty())
                    .subscribe(readyAction);
        }
    }

    public void callTickerFeed(String url , Action1<TickerFeed> tickerFeedAction1){

        try {
            appCMSWeatherFeedRest.getTickerFeed(url).enqueue(new Callback<TickerFeed>() {
                @Override
                public void onResponse(Call<TickerFeed> call, Response<TickerFeed> response) {
                    Observable.just(response.body())
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(tickerFeedAction1);
                }

                @Override
                public void onFailure(Call<TickerFeed> call, Throwable t) {
                    //Log.e(TAG, "Failed to retrieve IP based Geolocation: " + t.getMessage());
                    Observable.just((TickerFeed) null)
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(tickerFeedAction1);
                }
            });
        } catch (Exception e) {
            //Log.e(TAG, "Failed to retrieve IP based Geolocation: " + e.toString());
            Observable.just((TickerFeed) null)
                    .onErrorResumeNext(throwable -> Observable.empty())
                    .subscribe(tickerFeedAction1);
        }
    }
}
