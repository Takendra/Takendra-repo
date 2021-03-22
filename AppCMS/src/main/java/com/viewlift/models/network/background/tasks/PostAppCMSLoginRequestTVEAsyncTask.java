package com.viewlift.models.network.background.tasks;

import com.viewlift.models.data.appcms.ui.authentication.SignInResponse;
import com.viewlift.models.network.rest.AppCMSSignInCall;

import java.util.Map;

import hu.akarnokd.rxjava3.interop.RxJavaInterop;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by viewlift on 7/5/17.
 */

public class PostAppCMSLoginRequestTVEAsyncTask {
    private final AppCMSSignInCall call;
    private final Action1<SignInResponse> readyAction;

    private static final String TAG = "LoginRequestTask";
    private final String apiKey;

    public static class Params {
        String url;
        String userId;
        String tveUserId;
        String provider;
        String idpName;
        String idpLogo;
        Map<String, Boolean> resourceIds;

        public static class Builder {
            private Params params;

            public Builder() {
                this.params = new Params();
            }

            public Builder url(String url) {
                params.url = url;
                return this;
            }

            public Builder userId(String userId) {
                params.userId = userId;
                return this;
            }

            public Builder tveUserId(String tveUserId) {
                params.tveUserId = tveUserId;
                return this;
            }

            public Builder provider(String provider) {
                params.provider = provider;
                return this;
            }

            public Builder idpName(String idpName) {
                params.idpName = idpName;
                return this;
            }

            public Builder idpLogo(String idpLogo) {
                params.idpLogo = idpLogo;
                return this;
            }

            public Builder resourceIds(Map<String, Boolean> resourceIds) {
                params.resourceIds = resourceIds;
                return this;
            }

            public Params build() {
                return params;
            }
        }
    }

    public PostAppCMSLoginRequestTVEAsyncTask(AppCMSSignInCall call,
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
                            return call.callTVESignin(params.url, params.userId, params.tveUserId, params.provider, params.idpName, params.idpLogo, params.resourceIds, apiKey);
                        } catch (Exception e) {
                            //Log.e(TAG, "DialogType retrieving page API data: " + e.getMessage());
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
