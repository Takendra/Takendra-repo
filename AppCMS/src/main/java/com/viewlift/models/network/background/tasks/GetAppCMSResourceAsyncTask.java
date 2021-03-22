package com.viewlift.models.network.background.tasks;

import android.text.TextUtils;

import com.viewlift.models.data.appcms.ui.Resources;
import com.viewlift.models.data.appcms.ui.android.MetaPage;
import com.viewlift.models.network.rest.AppCMSResourceCall;

import java.io.IOException;

import hu.akarnokd.rxjava3.interop.RxJavaInterop;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by viewlift on 5/9/17.
 */

public class GetAppCMSResourceAsyncTask {
    private static final String TAG = "";

    private final AppCMSResourceCall call;
    private final Action1<Resources> readyAction;

    public static class Params {
        String url;
        String xApiKey;
        boolean loadFromFile;
        boolean bustCache;
        String authToken;
        MetaPage metaPage;

        public static class Builder {
            private Params params;

            public Builder() {
                params = new Params();
            }

            public Builder url(String url) {
                params.url = url;
                return this;
            }

            public Builder xApiKey(String xApiKey) {
                params.xApiKey = xApiKey;
                return this;
            }

            public Builder authToken(String authToken) {
                params.authToken = authToken;
                return this;
            }

            public Builder loadFromFile(boolean loadFromFile) {
                params.loadFromFile = loadFromFile;
                return this;
            }

            public Builder bustCache(boolean bustCache) {
                params.bustCache = bustCache;
                return this;
            }

            public Builder metaPage(MetaPage metaPage) {
                params.metaPage = metaPage;
                return this;
            }

            public Params build() {
                return params;
            }
        }
    }

    public GetAppCMSResourceAsyncTask(AppCMSResourceCall call, Action1<Resources> readyAction) {
        this.call = call;
        this.readyAction = readyAction;
    }

    public void execute(Params params) {
        if (params != null) {
            Observable
                    .fromCallable(() -> {
                        try {
                            return call.call(params.url, params.xApiKey, params.bustCache, params.loadFromFile);
                        } catch (IOException e) {
                            //Log.e(TAG, "Could not retrieve Page UI data - " + params.url + ": " + e.toString());
                        }
                        return null;
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(RxJavaInterop.toV1Scheduler(AndroidSchedulers.mainThread()))
                    .onErrorResumeNext(throwable -> Observable.empty())
                    .subscribe((result) -> Observable.just(result).subscribe(readyAction));
        }
    }

    public void writeToFile(Resources appCMSPageUI, String url) {
        if (appCMSPageUI != null && !TextUtils.isEmpty(url)) {
            Observable
                    .fromCallable(() -> call.writeToFile(appCMSPageUI, url))
                    .subscribeOn(Schedulers.io())
                    .observeOn(RxJavaInterop.toV1Scheduler(AndroidSchedulers.mainThread()))
                    .onErrorResumeNext(throwable -> Observable.empty())
                    .subscribe(result -> {});
        }
    }
}
