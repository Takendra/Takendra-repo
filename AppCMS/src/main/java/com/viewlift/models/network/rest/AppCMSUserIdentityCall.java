package com.viewlift.models.network.rest;

import androidx.annotation.NonNull;

import com.viewlift.models.data.appcms.user.ParentalControlResponse;
import com.viewlift.models.data.appcms.user.UserIdentity;
import com.viewlift.models.data.appcms.user.UserDescriptionResponse;
import com.viewlift.models.data.appcms.user.UserIdentityPassword;
import com.viewlift.models.data.appcms.user.UserMeResponse;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by viewlift on 7/6/17.
 */

public class AppCMSUserIdentityCall {
    private final AppCMSUserIdentityRest appCMSUserIdentityRest;
    private final Map<String, String> authHeaders;

    @Inject
    public AppCMSUserIdentityCall(AppCMSUserIdentityRest appCMSUserIdentityRest) {
        this.appCMSUserIdentityRest = appCMSUserIdentityRest;
        this.authHeaders = new HashMap<>();
    }

    public void callGet(String url, String authToken, String apiKey, final Action1<UserIdentity> userIdentityAction) {
        authHeaders.put("Authorization", authToken);
        authHeaders.put("x-api-key", apiKey);
        appCMSUserIdentityRest.get(url, authHeaders).enqueue(new Callback<UserIdentity>() {
            @Override
            public void onResponse(@NonNull Call<UserIdentity> call,
                                   @NonNull Response<UserIdentity> response) {
                Observable.just(response.body())
                        .onErrorResumeNext(throwable -> Observable.empty())
                        .subscribe(userIdentityAction);
            }

            @Override
            public void onFailure(@NonNull Call<UserIdentity> call, @NonNull Throwable t) {
                Observable.just((UserIdentity) null)
                        .onErrorResumeNext(throwable -> Observable.empty())
                        .subscribe(userIdentityAction);
            }
        });
    }

    public void getUserDescription(String url, String authToken, String apiKey, final Action1<UserDescriptionResponse> userIdentityAction) {
        authHeaders.put("Authorization", authToken);
        authHeaders.put("x-api-key", apiKey);
        appCMSUserIdentityRest.getUserDescription(url, authHeaders).enqueue(new Callback<UserDescriptionResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserDescriptionResponse> call,
                                   @NonNull Response<UserDescriptionResponse> response) {
                Observable.just(response.body())
                        .onErrorResumeNext(throwable -> Observable.empty())
                        .subscribe(userIdentityAction);
            }

            @Override
            public void onFailure(@NonNull Call<UserDescriptionResponse> call, @NonNull Throwable t) {
                Observable.just((UserDescriptionResponse) null)
                        .onErrorResumeNext(throwable -> Observable.empty())
                        .subscribe(userIdentityAction);
            }
        });
    }

    public void getUserMe(String url, String authToken, String apiKey, final Action1<UserMeResponse> userIdentityAction) {
        authHeaders.put("Authorization", authToken);
        authHeaders.put("x-api-key", apiKey);
        appCMSUserIdentityRest.getUserMe(url, authHeaders).enqueue(new Callback<UserMeResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserMeResponse> call,
                                   @NonNull Response<UserMeResponse> response) {
                Observable.just(response.body())
                        .onErrorResumeNext(throwable -> Observable.empty())
                        .subscribe(userIdentityAction);
            }

            @Override
            public void onFailure(@NonNull Call<UserMeResponse> call, @NonNull Throwable t) {
                Observable.just((UserMeResponse) null)
                        .onErrorResumeNext(throwable -> Observable.empty())
                        .subscribe(userIdentityAction);
            }
        });
    }

    public void submitUserProfile(String url, String authToken, String apiKey, UserDescriptionResponse userDescription, final Action1<UserDescriptionResponse> userIdentityAction) {
        authHeaders.put("Authorization", authToken);
        authHeaders.put("x-api-key", apiKey);
        appCMSUserIdentityRest.submitUserProfile(url, authHeaders, userDescription).enqueue(new Callback<UserDescriptionResponse>() {
            @Override
            public void onResponse(Call<UserDescriptionResponse> call, Response<UserDescriptionResponse> response) {
                Observable.just(response.body())
                        .onErrorResumeNext(throwable -> Observable.empty())
                        .subscribe(userIdentityAction);
            }

            @Override
            public void onFailure(Call<UserDescriptionResponse> call, Throwable t) {
                Observable.just((UserDescriptionResponse) null)
                        .onErrorResumeNext(throwable -> Observable.empty())
                        .subscribe(userIdentityAction);
            }
        });

    }

    public void callPost(String url,
                         String authToken, String apiKey,
                         UserIdentity userIdentity,
                         final Action1<UserIdentity> userIdentityAction,
                         final Action1<ResponseBody> userErrorAction) {
        authHeaders.put("Authorization", authToken);
        authHeaders.put("x-api-key", apiKey);
        appCMSUserIdentityRest.post(url, authHeaders, userIdentity).enqueue(new Callback<UserIdentity>() {
            @Override
            public void onResponse(@NonNull Call<UserIdentity> call,
                                   @NonNull Response<UserIdentity> response) {
                if (response.body() != null) {
                    Observable.just(response.body())
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(userIdentityAction);
                } else {
                    Observable.just(response.errorBody()).subscribe(userErrorAction);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserIdentity> call, @NonNull Throwable t) {
                Observable.just((UserIdentity) null)
                        .onErrorResumeNext(throwable -> Observable.empty())
                        .subscribe(userIdentityAction);
            }
        });
    }

    public void passwordPost(String url,
                             String authToken, String apiKey,
                             UserIdentityPassword userIdentityPassword,
                             final Action1<UserIdentityPassword> userIdentityPasswordAction1,
                             final Action1<ResponseBody> userPasswordErrorAction) {
        authHeaders.put("resetToken", authToken);
        authHeaders.put("x-api-key", apiKey);
        appCMSUserIdentityRest.post(url, authHeaders,
                userIdentityPassword).enqueue(new Callback<UserIdentityPassword>() {
            @Override
            public void onResponse(@NonNull Call<UserIdentityPassword> call,
                                   @NonNull Response<UserIdentityPassword> response) {
                if (response.body() != null) {
                    Observable.just(response.body())
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(userIdentityPasswordAction1);
                } else {
                    Observable.just(response.errorBody())
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(userPasswordErrorAction);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserIdentityPassword> call, @NonNull Throwable t) {
                //
            }
        });
    }

    public void addPassword(String url, String authToken, String apiKey,
                            UserIdentityPassword userIdentityPassword,
                            final Action1<UserIdentityPassword> userIdentityPasswordAction1,
                            final Action1<ResponseBody> userPasswordErrorAction) {
        authHeaders.put("resetToken", authToken);
        authHeaders.put("x-api-key", apiKey);
        appCMSUserIdentityRest.post(url, authHeaders,
                userIdentityPassword).enqueue(new Callback<UserIdentityPassword>() {
            @Override
            public void onResponse(@NonNull Call<UserIdentityPassword> call,
                                   @NonNull Response<UserIdentityPassword> response) {
                if (response.body() != null) {
                    Observable.just(response.body())
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(userIdentityPasswordAction1);
                } else {
                    Observable.just(response.errorBody())
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(userPasswordErrorAction);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserIdentityPassword> call, @NonNull Throwable t) {
                //
            }
        });
    }

    public void setUserParentalControlInfo(String url, String authToken, String apiKey,
                                           ParentalControlResponse parentalControlResponse,
                                           Action1<ParentalControlResponse> parentalControlResponseAction1) {
        authHeaders.clear();
        authHeaders.put("Authorization", authToken);
        authHeaders.put("Content-Type", "application/json");
        authHeaders.put("x-api-key", apiKey);
        try {
            appCMSUserIdentityRest.setUserParentalControlInfo(url, authHeaders, parentalControlResponse).enqueue(new Callback<ParentalControlResponse>() {
                @Override
                public void onResponse(Call<ParentalControlResponse> call, Response<ParentalControlResponse> response) {
                    Observable.just(response.body())
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(parentalControlResponseAction1);
                }

                @Override
                public void onFailure(Call<ParentalControlResponse> call, Throwable t) {
                    Observable.just((ParentalControlResponse) null)
                            .onErrorResumeNext(throwable -> Observable.empty())
                            .subscribe(parentalControlResponseAction1);
                }
            });

        } catch (Exception e) {
            Observable.just((ParentalControlResponse) null)
                    .onErrorResumeNext(throwable -> Observable.empty())
                    .subscribe(parentalControlResponseAction1);
        }
    }
}
