package com.viewlift.models.network.rest;

import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.util.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.viewlift.models.data.appcms.ui.authentication.ErrorResponse;
import com.viewlift.models.data.appcms.ui.authentication.OTPRequest;
import com.viewlift.models.data.appcms.ui.authentication.SignInRequest;
import com.viewlift.models.data.appcms.ui.authentication.SignInResponse;
import com.viewlift.models.data.appcms.ui.authentication.SignUpEmailConsentRequest;
import com.viewlift.models.data.appcms.ui.authentication.SignUpRequest;
import com.viewlift.presenters.AppCMSPresenter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by viewlift on 7/5/17.
 */

public class AppCMSSignInCall {
    private static final String TAG = "AppCMSSignin";

    private final AppCMSSignInRest appCMSSignInRest;
    private final Gson gson;
    private Map<String, String> headersMap;

    @Inject
    public AppCMSSignInCall(AppCMSSignInRest appCMSSignInRest, Gson gson) {
        this.appCMSSignInRest = appCMSSignInRest;
        this.gson = gson;
        this.headersMap = new HashMap<>();
    }

    public SignInResponse call(String url,
                               String name, String email,
                               String password,
                               Boolean emailConsent,
                               String genreValue,
                               String userIdValue,
                               String requestType,
                               String phone,
                               AppCMSPresenter appCMSPresenter,
                               String screenName,
                               String otpValue,
                               String apiKey,
                               String isEmailConsentPresent,
                               String tveUserId,
                               boolean isWhatsAppConsentPresent,
                               String amazonUserId) {
        SignInResponse loggedInResponseResponse = null;

        SignInRequest signInRequest = new SignInRequest();
        SignUpRequest signUpRequest = new SignUpRequest();
        OTPRequest otpRequest = new OTPRequest();

        if (appCMSPresenter != null && appCMSPresenter.getAppCMSMain() != null &&
                appCMSPresenter.getAppCMSMain().getFeatures() != null &&
                appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue() != null &&
                appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isOtpEnabled()
                && appCMSPresenter.phoneObjectRequest != null && appCMSPresenter.phoneObjectRequest.isFromVerify()) {
            otpRequest.setPhoneNumber(phone);
            if (!Strings.isEmptyOrWhitespace(name)) {
                otpRequest.setName(name);
            }
            if (!Strings.isEmptyOrWhitespace(email)) {
                otpRequest.setEmail(email);
            }
            otpRequest.setRequestType(requestType);
            otpRequest.setEmail(email != null ? email : "");
            otpRequest.setEmailConsent(emailConsent != null ? emailConsent : false);
            otpRequest.setWhatsAppConsent(isWhatsAppConsentPresent);
            otpRequest.setOtpValue(otpValue);
        }
        SignUpEmailConsentRequest signUpEmailConsentRequest = new SignUpEmailConsentRequest();

        if (emailConsent == null) {
            signInRequest.setEmail(email);
            signInRequest.setPassword(password);
        } else {
            if (isEmailConsentPresent != null && isEmailConsentPresent.equalsIgnoreCase("Y")) {
                signUpEmailConsentRequest.setEmail(email);
                signUpEmailConsentRequest.setPassword(password);
                signUpEmailConsentRequest.setEmailConsent(emailConsent);
                signUpEmailConsentRequest.setGenreValue(genreValue);
                signUpEmailConsentRequest.setUserIdValue(userIdValue);
                if (!TextUtils.isEmpty(amazonUserId)) {
                    signUpEmailConsentRequest.setFiretvUserId(amazonUserId);
                }
            } else {
                signUpRequest.setEmail(email);
                signUpRequest.setPassword(password);
                signUpRequest.setGenreValue(genreValue);
                signUpRequest.setUserIdValue(userIdValue);
                signUpRequest.setTveUserId(tveUserId);
                if (!TextUtils.isEmpty(amazonUserId)) {
                    signUpRequest.setFiretvUserId(amazonUserId);
                }
            }
        }
        headersMap.clear();
        if (!TextUtils.isEmpty(apiKey)) {
            headersMap.put("x-api-key", apiKey);
        }

        try {
            Call<JsonElement> call = null;
            if (appCMSPresenter != null && appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getFeatures() != null &&
                    appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue() != null &&
                    appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isOtpEnabled() && appCMSPresenter.phoneObjectRequest != null && appCMSPresenter.phoneObjectRequest.isFromVerify()) {
                call = appCMSSignInRest.signin(url, otpRequest, headersMap);
            } else if (screenName != null && screenName.equalsIgnoreCase("Update")) {
                otpRequest.setPhoneNumber(phone);
                if (!Strings.isEmptyOrWhitespace(name)) {
                    otpRequest.setName(name);
                }

                if (!Strings.isEmptyOrWhitespace(email)) {
                    otpRequest.setEmail(email);
                }
                otpRequest.setRequestType(requestType);
                otpRequest.setEmail(email != null ? email : "");
                otpRequest.setEmailConsent(emailConsent != null ? emailConsent : false);
                otpRequest.setWhatsAppConsent(isWhatsAppConsentPresent);
                otpRequest.setOtpValue(otpValue);
                call = appCMSSignInRest.signin(url, otpRequest, headersMap);
            } else {
                if (emailConsent == null)
                    call = appCMSSignInRest.signin(url, signInRequest, headersMap);
                else {
                    if (isEmailConsentPresent != null && isEmailConsentPresent.equalsIgnoreCase("Y"))
                        call = appCMSSignInRest.signin(url, signUpEmailConsentRequest, headersMap);
                    else
                        call = appCMSSignInRest.signin(url, signUpRequest, headersMap);
                }
            }

            Response<JsonElement> response = call.execute();

            if (response.body() != null) {
                JsonElement signInResponse = response.body();
                //Log.d(TAG, "Raw response: " + signInResponse.toString());
                loggedInResponseResponse = gson.fromJson(signInResponse, SignInResponse.class);
            } else if (response.errorBody() != null) {
                String errorResponse = response.errorBody().string();
                //Log.d(TAG, "Raw response: " + errorResponse);
                loggedInResponseResponse = new SignInResponse();
                loggedInResponseResponse.setErrorResponse(gson.fromJson(errorResponse, ErrorResponse.class));
                loggedInResponseResponse.setErrorResponseSet(true);
            }
        } catch (JsonSyntaxException | IOException e) {
            //Log.e(TAG, "SignIn error: " + e.toString());
        }

        return loggedInResponseResponse;
    }

    public SignInResponse callTVESignin(String url, String userId, String tveUserId, String provider, String idpName,
                                        String idpLogo, Map<String, Boolean> resourceIds, String apiKey) {
        SignInResponse loggedInResponseResponse = null;

        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setUserId(userId);
        signInRequest.setTveUserId(tveUserId);
        signInRequest.setProvider("verimatrix");
        signInRequest.setMvpdProvider(provider);
        signInRequest.setIdpLogo(idpLogo);
        signInRequest.setIdpName(idpName);
        if (resourceIds != null) signInRequest.setResourceIds(resourceIds);

        headersMap.clear();
        if (!TextUtils.isEmpty(apiKey)) {
            headersMap.put("x-api-key", apiKey);
        }

        try {
            Call<JsonElement> call = appCMSSignInRest.signinTVE(url, signInRequest, headersMap);

            Response<JsonElement> response = call.execute();
            if (response.body() != null) {
                JsonElement signInResponse = response.body();
                //Log.d(TAG, "Raw response: " + signInResponse.toString());
                loggedInResponseResponse = gson.fromJson(signInResponse, SignInResponse.class);
            } else if (response.errorBody() != null) {
                String errorResponse = response.errorBody().string();
                //Log.d(TAG, "Raw response: " + errorResponse);
                loggedInResponseResponse = new SignInResponse();
                if (errorResponse != null) {
                    loggedInResponseResponse.setErrorResponse(gson.fromJson(errorResponse, ErrorResponse.class));
                }
            }
        } catch (JsonSyntaxException | IOException e) {
            //Log.e(TAG, "SignIn error: " + e.toString());
        }

        return loggedInResponseResponse;
    }

    public void signoutTVE(String url, String apiKey, String authToken, Action1<SignInResponse> readyAction) {
        headersMap.clear();
        if (!TextUtils.isEmpty(apiKey)) {
            headersMap.put("x-api-key", apiKey);
        }
        if (!TextUtils.isEmpty(authToken)) {
            headersMap.put("Authorization", authToken);
        }
        try {
            appCMSSignInRest.updateTVE(url, new SignInRequest(), headersMap).enqueue(new Callback<SignInResponse>() {
                @Override
                public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                    SignInResponse signInResponse = response.body();
                    Observable.just(signInResponse)
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(readyAction);

                }

                @Override
                public void onFailure(Call<SignInResponse> call, Throwable t) {
                    readyAction.call(null);
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "SignIn error: " + ex.toString());
        }
    }

    public void updateEmail(String url, String email, String apiKey, String authToken, Action1<SignInResponse> readyAction) {
        headersMap.clear();
        if (!TextUtils.isEmpty(apiKey)) {
            headersMap.put("x-api-key", apiKey);
        }
        if (!TextUtils.isEmpty(authToken)) {
            headersMap.put("Authorization", authToken);
        }

        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setEmail(email);
        signInRequest.setIgnorePassword(false);

        try {
            appCMSSignInRest.updateEmail(url, signInRequest, headersMap).enqueue(new Callback<SignInResponse>() {
                @Override
                public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                    SignInResponse signInResponse = response.body();
                    if (signInResponse != null) {
                        Observable.just(signInResponse)
                                .onErrorResumeNext(throwable -> Observable.empty())
                                .subscribe(readyAction);
                    }

                }

                @Override
                public void onFailure(Call<SignInResponse> call, Throwable t) {
                    readyAction.call(null);
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "SignIn error: " + ex.toString());
        }
    }


    public void signout(String url, String authToken, String apiKey, Action0 action0) {
        headersMap.clear();
        if (!TextUtils.isEmpty(apiKey)) {
            headersMap.put("x-api-key", apiKey);
        }
        if (!TextUtils.isEmpty(authToken)) {
            headersMap.put("Authorization", authToken);
        }

        try {
            appCMSSignInRest.signout(url, headersMap).enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    if (action0 != null)
                        action0.call();
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    if (action0 != null) action0.call();
                }
            });


        } catch (JsonSyntaxException e) {
            //Log.e(TAG, "SignIn error: " + e.toString());
        }
    }
}
