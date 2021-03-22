package com.viewlift.models.network.rest;

import com.google.gson.JsonElement;
import com.viewlift.models.data.appcms.AppCMSCarrierBillingRequest;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface AppCMSCarrierBillingRest {
    @POST
    Call<JsonElement> getRobiAndGPDataBundleSubscription(@Url String url, @HeaderMap Map<String, String> headers, @Body AppCMSCarrierBillingRequest body);

}
