package com.viewlift.models.network.rest;

import com.viewlift.models.data.appcms.api.AppCMSCardInfoResponse;
import com.viewlift.models.data.appcms.api.AppCMSDeleteCardResponse;
import com.viewlift.models.data.appcms.api.AppCMSSavedCardsResponse;
import com.viewlift.models.data.appcms.api.JuspayOrderRequest;
import com.viewlift.models.data.appcms.api.OfferCodeResponse;
import com.viewlift.models.data.appcms.api.OfferDiscount;
import com.viewlift.models.data.appcms.subscriptions.AppCMSUserSubscriptionPlanResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface AppCMSJuspayRest{

    @POST
    Call<AppCMSUserSubscriptionPlanResult> createJuspayOrder(@Url String url, @Body JuspayOrderRequest appCMSJuspayOrderRequest, @HeaderMap Map<String, String> authHeaders);

    @GET
    Call<AppCMSSavedCardsResponse> getUserSavedCards(@Url String url, @HeaderMap Map<String, String> authHeaders);

    @POST
    Call<AppCMSDeleteCardResponse> deleteJuspaySavedCard(@Url String url, @Body Map<String, String> params, @HeaderMap Map<String, String> authHeaders);

    @GET
    Call<AppCMSCardInfoResponse> getCardInfo(@Url String url);

    @POST
    Call<OfferCodeResponse> validateOfferCode(@Url String url, @Body Map<String, String> params, @HeaderMap Map<String, String> authHeaders);

    @GET
    Call<OfferDiscount> calculateDiscount(@Url String url, @HeaderMap Map<String, String> authHeaders);
}