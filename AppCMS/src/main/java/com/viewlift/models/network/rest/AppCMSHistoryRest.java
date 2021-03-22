package com.viewlift.models.network.rest;

/*
 * Created by Viewlift on 7/5/17.
 */

import com.viewlift.models.data.appcms.history.AppCMSHistoryResult;
import com.viewlift.models.data.appcms.history.AppCMSRecommendationGenreResult;
import com.viewlift.models.data.appcms.history.AppCMSRecomendationResult;
import com.viewlift.models.data.appcms.history.SeriesHistory;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Url;

public interface AppCMSHistoryRest {
    @GET
    Call<AppCMSHistoryResult> get(@Url String url, @HeaderMap Map<String, String> headers);
    @GET
    Call<AppCMSRecommendationGenreResult> getRecommendationGenre(@Url String url, @HeaderMap Map<String, String> headers);

    @GET
    Call<AppCMSRecomendationResult> getRecomendation(@Url String url, @HeaderMap Map<String, String> headers);

    @GET
    Call<List<SeriesHistory>> getSeries(@Url String url, @HeaderMap Map<String, String> headers);
}
