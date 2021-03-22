package com.viewlift.models.network.rest;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.viewlift.models.data.appcms.ui.authentication.ForgotPasswordRequest;
import com.viewlift.models.data.appcms.ui.authentication.ForgotPasswordResponse;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

/*
 * Created by viewlift on 7/6/17.
 */

public class AppCMSResetPasswordCall {
    private AppCMSResetPasswordRest appCMSResetPasswordRest;
    private Gson gson;

    @Inject
    public AppCMSResetPasswordCall(AppCMSResetPasswordRest appCMSResetPasswordRest,
                                   Gson gson) {
        this.appCMSResetPasswordRest = appCMSResetPasswordRest;
        this.gson = gson;
    }

    public void call(String url, String email, String xApiKey,String authToken, final Action1<ForgotPasswordResponse> readyAction) {
        Map<String, String> authTokenMap = new HashMap<>();
        authTokenMap.put("x-api-key", xApiKey);
        if (!TextUtils.isEmpty(authToken)) {
            authTokenMap.put("Authorization", authToken);
        }

        appCMSResetPasswordRest.resetPassword(url, new ForgotPasswordRequest(email), authTokenMap).enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            @SuppressWarnings("ConstantConditions")
            public void onResponse(@NonNull Call<ForgotPasswordResponse> call,
                                   @NonNull Response<ForgotPasswordResponse> response) {
                if (readyAction != null) {
                    if (response.body() != null) {
                        Observable.just(response.body())
                                .onErrorResumeNext(throwable -> Observable.empty())
                                .subscribe(readyAction);
                    } else if (response.errorBody() != null) {
                        try {
                            ForgotPasswordResponse forgotPasswordResponse =
                                    gson.fromJson(response.errorBody().string(),
                                            ForgotPasswordResponse.class);
                            Observable.just(forgotPasswordResponse)
                                    .onErrorResumeNext(throwable -> Observable.empty())
                                    .subscribe(readyAction);
                        } catch (Exception e) {
                            Observable.just((ForgotPasswordResponse) null)
                                    .onErrorResumeNext(throwable -> Observable.empty())
                                    .subscribe(readyAction);
                        }
                    } else {
                        Observable.just((ForgotPasswordResponse) null)
                                .onErrorResumeNext(throwable -> Observable.empty())
                                .subscribe(readyAction);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ForgotPasswordResponse> call, @NonNull Throwable t) {
                if (readyAction != null) {
                    Observable.just((ForgotPasswordResponse) null)
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(readyAction);
                }
            }
        });
    }
}
