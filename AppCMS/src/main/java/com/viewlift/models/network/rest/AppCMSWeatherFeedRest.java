package com.viewlift.models.network.rest;

import com.viewlift.models.data.appcms.api.IPGeoLocatorResponse;
import com.viewlift.models.data.appcms.weather.Cities;
import com.viewlift.models.data.appcms.weather.TickerFeed;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by viewlift on 8/1/17.
 */

public interface AppCMSWeatherFeedRest {
    @GET
    Call<Cities> get(@Url String url);

    @GET
    Call<TickerFeed> getTickerFeed(@Url String url);
}
