package com.viewlift.models.network.rest;

import com.viewlift.models.data.appcms.api.AppCMSEntitlementResponse;
import com.viewlift.models.data.appcms.api.AppCMSRentalResponse;
import com.viewlift.models.data.appcms.api.AppCMSTransactionDataResponse;
import com.viewlift.models.data.appcms.api.AppCMSTransactionDataValue;
import com.viewlift.models.data.appcms.api.AppCMSVideoDetail;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.PUT;
import retrofit2.http.Url;

/**
 * Created by anas.azeem on 7/13/2017.
 * Owned by ViewLift, NYC
 */

public interface AppCMSVideoDetailRest {
    @GET
    Call<AppCMSVideoDetail> get(@Url String url, @HeaderMap Map<String, String> authHeaders);

    @GET
    Call<AppCMSEntitlementResponse> getEntitlementVideo(@Url String url, @HeaderMap Map<String, String> authHeaders);

    @PUT
    Call<AppCMSRentalResponse> getRentalVideoRespose(@Url String url, @HeaderMap Map<String, String> authHeaders);

    @GET
    Call<List<Map<String, AppCMSTransactionDataValue>>> getTransactionDataResponse(@Url String url, @HeaderMap Map<String, String> authHeaders);

    @GET
    Call<AppCMSTransactionDataResponse> getAppCMSTransactionDataResponse(@Url String url, @HeaderMap Map<String, String> authHeaders);
}
