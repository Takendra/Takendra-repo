package com.viewlift.models.network.rest;

import com.viewlift.models.data.appcms.api.AppCMSLocationResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface AppCMSLocationRest {

    @POST
    Call<AppCMSLocationResponse> getCurrentLocation(@Url String url);
}
