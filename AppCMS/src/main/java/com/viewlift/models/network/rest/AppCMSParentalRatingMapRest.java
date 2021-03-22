package com.viewlift.models.network.rest;

import com.viewlift.models.data.appcms.api.AppCMSParentalRatingMapResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface AppCMSParentalRatingMapRest {
    @GET
    Call<List<AppCMSParentalRatingMapResponse>> getRatingMap(@Url String url);
}
