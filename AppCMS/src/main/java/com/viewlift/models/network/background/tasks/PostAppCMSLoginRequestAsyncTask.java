package com.viewlift.models.network.background.tasks;

import android.util.Log;

import com.viewlift.models.data.appcms.ui.authentication.SignInResponse;
import com.viewlift.models.network.rest.AppCMSSignInCall;
import com.viewlift.presenters.AppCMSPresenter;

import hu.akarnokd.rxjava3.interop.RxJavaInterop;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by viewlift on 7/5/17.
 */

public class PostAppCMSLoginRequestAsyncTask {
    private final AppCMSSignInCall call;
    private final Action1<SignInResponse> readyAction;

    private static final String TAG = "LoginRequestTask";
    private final String apiKey;

    public static class Params {
        String url;
        String email;
        String name;
        String password;
        Boolean emailConsent;
        boolean whatsAppConsent;
        String genreValue;
        String userIdValue;
        String requestType;
        String phone;
        AppCMSPresenter appCMSPresenter;
        String screenName;
        String otpValue;
        String tveId;
        String isEmailConsentPresent;
        String authToken;
        String amazonUserId;

        public static class Builder {
            private Params params;

            public Builder() {
                this.params = new Params();
            }

            public Builder url(String url) {
                params.url = url;
                return this;
            }

            public Builder authToken(String authToken) {
                params.authToken = authToken;
                return this;
            }

            public Builder email(String email) {
                params.email = email;
                return this;
            }
            public Builder name(String name) {
                params.name = name;
                return this;
            }

            public Builder password(String password) {
                params.password = password;
                return this;
            }

            public Builder emailConsent(Boolean emailConsent) {
                params.emailConsent = emailConsent;
                return this;
            }

            public Builder whatsAppConsent(boolean whatsAppConsent) {
                params.whatsAppConsent = whatsAppConsent;
                return this;
            }

            public Builder genreValue(String genreValue) {
                params.genreValue = genreValue;
                return this;
            }

            public Builder userIdValue(String userIdValue) {
                params.userIdValue = userIdValue;
                return this;
            }

            public Builder tveId(String tveId) {
                params.tveId = tveId;
                return this;
            }


            public Builder phoneValue(String phoneValue) {
                params.phone = phoneValue;
                return this;
            }

            public Builder requestType(String requestType) {
                params.requestType = requestType;
                return this;
            }

            public Builder appCMSPresenter(AppCMSPresenter appCMSPresenter) {
                params.appCMSPresenter = appCMSPresenter;
                return this;
            }

            public Builder screenName(String screenName) {
                params.screenName = screenName;
                return this;
            }

            public Builder otpValue(String screenName) {
                params.otpValue = screenName;
                return this;
            }

            public Builder isEmailConsentPresent(String isEmailConsentPresent) {
                params.isEmailConsentPresent = isEmailConsentPresent;
                return this;
            }

            public Builder amazonUserId(String amazonUserId) {
                params.amazonUserId = amazonUserId;
                return this;
            }

            public Params build() {
                return params;
            }
        }
    }

    public PostAppCMSLoginRequestAsyncTask(AppCMSSignInCall call,
                                           Action1<SignInResponse> readyAction, String apiKey) {
        this.call = call;
        this.readyAction = readyAction;
        this.apiKey = apiKey;
    }


    public void execute(Params params) {
        Observable
                .fromCallable(() -> {
                    if (params != null) {
                        try {
                            return call.call(params.url, params.name, params.email, params.password, params.emailConsent, params.genreValue,
                                    params.userIdValue, params.requestType, params.phone, params.appCMSPresenter, params.screenName,
                                    params.otpValue,apiKey, params.isEmailConsentPresent, params.tveId, params.whatsAppConsent, params.amazonUserId);
                        } catch (Exception e) {
                            Log.e(TAG, "DialogType retrieving page API data: " + e.getMessage());
                        }
                    }
                    return null;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(RxJavaInterop.toV1Scheduler(AndroidSchedulers.mainThread()))
                .onErrorResumeNext(throwable -> Observable.empty())
                .subscribe((result) -> Observable.just(result).subscribe(readyAction));
    }
}
