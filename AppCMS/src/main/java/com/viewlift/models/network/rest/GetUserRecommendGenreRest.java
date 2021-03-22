package com.viewlift.models.network.rest;

import com.viewlift.models.data.appcms.ui.main.GetRecommendationGenres;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface GetUserRecommendGenreRest {
    @GET
    @Headers("Cache-Control: max-age=0, no-cache, no-store")
    Call<GetRecommendationGenres> get(@Url String url, @HeaderMap Map<String, String> headers);
}
