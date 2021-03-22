package com.viewlift.models.network.rest;

/*
 * Created by Viewlift on 6/28/2017.
 */

import androidx.annotation.WorkerThread;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.viewlift.models.data.appcms.sslcommerz.SSLInitiateResponse;
import com.viewlift.models.billing.appcms.purchase.InAppPurchase;
import com.viewlift.models.data.appcms.sslcommerz.SSLInitiateBody;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

public class AppCMSSSLCommerzInitiateCall {

    private static final String TAG = AppCMSSSLCommerzInitiateCall.class.getSimpleName() + "TAG";
    private final AppCMSSSLCommerzInitiateRest appCMSSSLCommerzInitiateRest;

    @SuppressWarnings({"unused, FieldCanBeLocal"})
    private final Gson gson;
    private Map<String, String> headersMap;

    @Inject
    public AppCMSSSLCommerzInitiateCall(AppCMSSSLCommerzInitiateRest appCMSSSLCommerzInitiateRest, Gson gson) {
        this.appCMSSSLCommerzInitiateRest = appCMSSSLCommerzInitiateRest;
        this.gson = gson;
        this.headersMap = new HashMap<>();
    }

    @WorkerThread
    public void call(String url,
                     final Action1<SSLInitiateResponse> sslInitiateAction1,
                     String apiKey, String authToken,
                     String planId, String transId, InAppPurchase inAppPurchase, String mobile) {
        try {
            headersMap.clear();
            headersMap.put("x-api-key", apiKey);
            headersMap.put("Authorization", authToken);

            SSLInitiateBody sslInitiateBody = new SSLInitiateBody();
            sslInitiateBody.setPlanId(planId);
            sslInitiateBody.setTran_id(transId);
            sslInitiateBody.setPhone(mobile);
            sslInitiateBody.setTransactionRequest(inAppPurchase);


            appCMSSSLCommerzInitiateRest.initiateSSL(url, sslInitiateBody, headersMap).enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    SSLInitiateResponse sslInitiateResponse = null;
                    if (response.body() != null) {
                        JsonElement initiateResponse = response.body();
                        sslInitiateResponse = gson.fromJson(initiateResponse, SSLInitiateResponse.class);
                    } else if (response.errorBody() != null) {
                        Reader errorResponse = response.errorBody().charStream();
                        sslInitiateResponse = gson.fromJson(errorResponse, SSLInitiateResponse.class);
                        //Log.d(TAG, "Raw response: " + errorResponse);
                        //sslInitiateResponse = new RSAKeyResponse();
                    }
                    Observable.just(sslInitiateResponse).subscribe(sslInitiateAction1);
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {

                }
            });
        } catch (Exception e) {
        }
    }
}
