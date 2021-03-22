package com.viewlift.models.network.rest;

import com.viewlift.models.data.appcms.user.ParentalControlResponse;
import com.viewlift.models.data.appcms.user.UserIdentity;
import com.viewlift.models.data.appcms.user.UserDescriptionResponse;
import com.viewlift.models.data.appcms.user.UserIdentityPassword;
import com.viewlift.models.data.appcms.user.UserMeResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

/**
 * Created by viewlift on 7/6/17.
 */

public interface AppCMSUserIdentityRest {

    @GET
    Call<UserIdentity> get(@Url String url, @HeaderMap Map<String, String> authHeaders);

    @GET
    Call<UserMeResponse> getUserMe(@Url String url, @HeaderMap Map<String, String> authHeaders);

    @GET
    Call<UserDescriptionResponse> getUserDescription(@Url String url, @HeaderMap Map<String, String> authHeaders);

    @POST
    Call<UserIdentity> post(@Url String url, @HeaderMap Map<String, String> authHeaders,
                            @Body UserIdentity userIdentity);

    @POST
    Call<UserIdentityPassword> post(@Url String url, @HeaderMap Map<String, String> authHeaders,
                                    @Body UserIdentityPassword userIdentityPassword);

    @POST
    Call<UserDescriptionResponse> submitUserProfile(@Url String url, @HeaderMap Map<String, String> authHeaders,
                                                    @Body UserDescriptionResponse userDescriptionResponse);

    @PUT
    Call<ParentalControlResponse> setUserParentalControlInfo(@Url String url, @HeaderMap Map<String, String> authHeaders,
                                                    @Body ParentalControlResponse parentalControlResponse);
}
