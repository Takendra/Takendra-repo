package com.viewlift.models.network.rest;

import com.viewlift.models.data.appcms.api.AppCMSEmailConsentValue;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by viewlift on 8/1/17.
 */

public interface AppCMSEmailConsentRest {
    @GET
    Call<Map<String, AppCMSEmailConsentValue>> get(@Url String url);
}
