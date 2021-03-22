package com.viewlift.models.network.background.tasks;

import android.content.Context;

import com.viewlift.models.data.appcms.api.AppCMSParentalRatingMapResponse;
import com.viewlift.models.network.rest.AppCMSParentalRatingMapCall;

import java.util.List;

import hu.akarnokd.rxjava3.interop.RxJavaInterop;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class GetAppCMSParentalRatingMapAsyncTask {
    private final AppCMSParentalRatingMapCall call;
    private final Action1<List<AppCMSParentalRatingMapResponse>> readyAction;

    public GetAppCMSParentalRatingMapAsyncTask(AppCMSParentalRatingMapCall call, Action1<List<AppCMSParentalRatingMapResponse>> readyAction) {
        this.call = call;
        this.readyAction = readyAction;
    }

    public void execute(GetAppCMSParentalRatingMapAsyncTask.Params params) {
        Observable.fromCallable(() -> {
            if (params != null) {
                try {
                    return call.call(params.context,0, params.networkDisconnected);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }).subscribeOn(Schedulers.io())
                .observeOn(RxJavaInterop.toV1Scheduler(AndroidSchedulers.mainThread()))
                .onErrorResumeNext(throwable -> Observable.empty())
                .subscribe((result) -> Observable.just(result).subscribe(readyAction));
    }

    public static class Params {
        Context context;
        boolean networkDisconnected;

        public static class Builder {
            Params params;

            public Builder() {
                params = new Params();
            }

            public Builder context(Context context) {
                params.context = context;
                return this;
            }

            public Builder networkDisconnected(boolean networkDisconnected) {
                params.networkDisconnected = networkDisconnected;
                return this;
            }

            public Params build() {
                return params;
            }
        }
    }
}
