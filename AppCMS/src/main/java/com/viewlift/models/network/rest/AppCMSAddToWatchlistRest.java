package com.viewlift.models.network.rest;

/*
 * Created by Viewlift on 7/10/17.
 */

import com.viewlift.models.data.appcms.api.AddToWatchlistRequest;
import com.viewlift.models.data.appcms.likes.LikeRequest;
import com.viewlift.models.data.appcms.likes.LikeResult;
import com.viewlift.models.data.appcms.likes.Likes;
import com.viewlift.models.data.appcms.watchlist.AppCMSAddToWatchlistResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface AppCMSAddToWatchlistRest {
    @POST
    Call<AppCMSAddToWatchlistResult> add(@Url String url, @HeaderMap Map<String, String> headers,
                                         @Body AddToWatchlistRequest request);

    @HTTP(method = "DELETE", hasBody = true)
    Call<AppCMSAddToWatchlistResult> removeSingle(@Url String url, @HeaderMap Map<String,
            String> headers, @Body AddToWatchlistRequest request);

    @POST
    Call<LikeResult> addLike(@Url String url, @HeaderMap Map<String, String> headers,
                             @Body LikeRequest request);
    @GET
    Call<LikeResult> getLike(@Url String url, @HeaderMap Map<String, String> headers);

    @DELETE
    Call<LikeResult> removeLike(@Url String url, @HeaderMap Map<String, String> headers);

    @GET
    Call<Likes> countLike(@Url String url, @HeaderMap Map<String, String> headers);





//    @HTTP(method = "DELETE", hasBody = true)
//    Call<AppCMSAddToWatchlistResult> removeAll(@Url String url, @HeaderMap Map<String,
//            String> headers, @Body AddToWatchlistRequest request);
}