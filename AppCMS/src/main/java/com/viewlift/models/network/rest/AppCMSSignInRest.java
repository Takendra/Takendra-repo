package com.viewlift.models.network.rest;

import com.google.gson.JsonElement;
import com.viewlift.models.data.appcms.ui.authentication.SignInResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by viewlift on 7/5/17.
 */

public interface AppCMSSignInRest {
    @POST
    Call<JsonElement> signin(@Url String url, @Body Object body, @HeaderMap Map<String, String> headers);

    @POST
    Call<JsonElement> signinTVE(@Url String url, @Body Object body, @HeaderMap Map<String, String> headers);
    @POST
    Call<SignInResponse> updateTVE(@Url String url, @Body Object body, @HeaderMap Map<String, String> headers);

    @POST
    Call<JsonElement> signout(@Url String url, @HeaderMap Map<String, String> headers);

    @POST
    Call<SignInResponse> updateEmail(@Url String url, @Body Object body, @HeaderMap Map<String, String> headers);
}
