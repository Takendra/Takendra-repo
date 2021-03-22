package com.viewlift.models.network.rest;

import com.viewlift.models.data.appcms.api.IPGeoLocatorResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by viewlift on 8/1/17.
 */

public interface AppCMSIPGeoLocatorRest {
    @POST
    Call<IPGeoLocatorResponse> get(@Url String url, @HeaderMap Map<String, String> authHeaders);
}
