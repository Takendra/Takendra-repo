package com.viewlift.models.network.rest;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.viewlift.models.data.appcms.api.RedeemApiResponse;
import com.viewlift.models.data.appcms.api.RedeemRequest;
import com.viewlift.models.data.appcms.ui.authentication.ErrorResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;

/**
 * This class is responsible for API calls to check redemption codes
 *
 * @author Vinay
 * @since 2019-01-21
 */

public class AppCMSRedeemCall {
    private static final String TAG = "AppCMSRedeem";

    private final AppCMSRedeemRest appCMSRedeemRest;
    private final Gson gson;
    private Map<String, String> headersMap;

    @Inject
    public AppCMSRedeemCall(AppCMSRedeemRest appCMSRedeemRest, Gson gson) {
        this.appCMSRedeemRest = appCMSRedeemRest;
        this.gson = gson;
        this.headersMap = new HashMap<>();
    }

    /**
     * API call to redeem the validated coupon
     *
     * @param url          - API URL
     * @param couponCode   - coupon code
     * @param site         - site name
     * @param platform     - platform - android/firetv
     * @param transaction  - transaction
     * @param userId       - userId
     * @param purchaseType - purchase Type
     * @param currencyCode - currency Code
     * @param apiKey       - xapi key
     * @param authToken    - authorization token
     * @return object of  RedeemApiResponse
     * @author Vinay
     */
    public RedeemApiResponse redeemCoupon(String url, String couponCode, String site, String platform, String siteId,
                                          String transaction, String userId, String purchaseType, String currencyCode, String apiKey, String authToken) {
        RedeemApiResponse redeemApiResponse = null;

        RedeemRequest redeemRequest = new RedeemRequest();

        redeemRequest.setUrl(url);
        redeemRequest.setCouponCode(couponCode);
        redeemRequest.setSite(site);
        redeemRequest.setPlatform(platform);
        redeemRequest.setSiteId(siteId);
        redeemRequest.setTransaction(transaction);
        redeemRequest.setUserId(userId);
        redeemRequest.setPurchaseType(purchaseType);
        redeemRequest.setCurrencyCode(currencyCode);

        headersMap.clear();
        if (!TextUtils.isEmpty(apiKey)) {
            headersMap.put("x-api-key", apiKey);
        }

        if (!TextUtils.isEmpty(authToken)) {
            headersMap.put("Authorization", authToken);
        }

        try {
            Call<JsonElement> call = appCMSRedeemRest.redeem(url, redeemRequest, headersMap);

            Response<JsonElement> response = call.execute();
            if (response.body() != null) {
                JsonElement redeemResponse = response.body();
                //Log.d(TAG, "Raw response: " + redeemResponse.toString());
                redeemApiResponse = gson.fromJson(redeemResponse, RedeemApiResponse.class);
            } else if (response.errorBody() != null) {
                String errorResponse = response.errorBody().string();
                //Log.d(TAG, "Raw response: " + errorResponse);
                redeemApiResponse = new RedeemApiResponse();
                if (errorResponse != null) {
                    redeemApiResponse.setErrorResponse(gson.fromJson(errorResponse, ErrorResponse.class));
                    redeemApiResponse.setErrorResponseSet(true);
                }
            }
        } catch (JsonSyntaxException | IOException e) {
            //Log.e(TAG, "SignIn error: " + e.toString());
        }

        return redeemApiResponse;
    }


    /**
     * API call to validate coupon
     *
     * @param url        - API URL
     * @param couponCode - coupon code
     * @param apiKey     - xapi key
     * @param authToken  - authorization token
     * @return object of  RedeemApiResponse
     * @author Vinay
     * @deprecated use {@link #validateOffer(String, String, String, String)}
     */
    @Deprecated
    public RedeemApiResponse validateCoupon(String url, String couponCode, String apiKey, String authToken) {
        RedeemApiResponse redeemApiResponse = null;

        RedeemRequest redeemRequest = new RedeemRequest();
        redeemRequest.setCouponCode(couponCode);

        headersMap.clear();
        if (!TextUtils.isEmpty(apiKey)) {
            headersMap.put("x-api-key", apiKey);
        }

        if (!TextUtils.isEmpty(authToken)) {
            headersMap.put("Authorization", authToken);
        }

        try {
            Call<JsonElement> call = appCMSRedeemRest.redeem(url, redeemRequest, headersMap);

            Response<JsonElement> response = call.execute();
            if (response.body() != null) {
                JsonElement redeemResponse = response.body();
                //Log.d(TAG, "Raw response: " + redeemResponse.toString());
                redeemApiResponse = gson.fromJson(redeemResponse, RedeemApiResponse.class);
            } else if (response.errorBody() != null) {
                String errorResponse = response.errorBody().string();
                //Log.d(TAG, "Raw response: " + errorResponse);
                redeemApiResponse = new RedeemApiResponse();
                if (errorResponse != null) {
                    redeemApiResponse.setErrorResponse(gson.fromJson(errorResponse, ErrorResponse.class));
                }
            }
        } catch (JsonSyntaxException | IOException e) {
            //Log.e(TAG, "SignIn error: " + e.toString());
        }

        return redeemApiResponse;
    }


    /**
     * API call to validate coupon
     *
     * @param url       - API URL
     * @param offerCode - offer code
     * @param apiKey    - xapi key
     * @param authToken - authorization token
     * @return object of  RedeemApiResponse
     * @author Wishy
     */
    public RedeemApiResponse validateOffer(String url, String offerCode, String apiKey, String authToken) {
        RedeemApiResponse redeemApiResponse = null;

        RedeemRequest redeemRequest = new RedeemRequest();
        redeemRequest.setOfferCode(offerCode);

        headersMap.clear();
        if (!TextUtils.isEmpty(apiKey)) {
            headersMap.put("x-api-key", apiKey);
        }

        if (!TextUtils.isEmpty(authToken)) {
            headersMap.put("Authorization", authToken);
        }

        try {
            Call<JsonElement> call = appCMSRedeemRest.redeem(url, redeemRequest, headersMap);

            Response<JsonElement> response = call.execute();
            if (response.body() != null) {
                JsonElement redeemResponse = response.body();
                //Log.d(TAG, "Raw response: " + redeemResponse.toString());
                redeemApiResponse = gson.fromJson(redeemResponse, RedeemApiResponse.class);
            } else if (response.errorBody() != null) {
                String errorResponse = response.errorBody().string();
                //Log.d(TAG, "Raw response: " + errorResponse);
                redeemApiResponse = new RedeemApiResponse();
                if (errorResponse != null) {
                    redeemApiResponse.setErrorResponse(gson.fromJson(errorResponse, ErrorResponse.class));
                }
            }
        } catch (JsonSyntaxException | IOException e) {
            //Log.e(TAG, "SignIn error: " + e.toString());
        }

        return redeemApiResponse;
    }
}
