package com.viewlift.models.network.background.tasks;

import android.content.Context;

import com.viewlift.models.data.appcms.ui.main.GetRecommendationGenres;
import com.viewlift.models.network.rest.GetUserRecommendGenreCall;

import hu.akarnokd.rxjava3.interop.RxJavaInterop;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class GetRecommendationGenreAsyncTask {
    private static final String TAG = "GetRecommendationGenreAsyncTask";

    private final GetUserRecommendGenreCall call;
    private final Action1<GetRecommendationGenres> readyAction;

    public GetRecommendationGenreAsyncTask(GetUserRecommendGenreCall call,
                                           Action1<GetRecommendationGenres> readyAction) {
        this.call = call;
        this.readyAction = readyAction;
    }

    public void execute(GetRecommendationGenreAsyncTask.Params params) {
        Observable
                .fromCallable(() -> {
                    if (params != null) {
                        try {
                            return call.call(params.context,
                                    params.siteId,
                                    params.userId, params.url, params.xapiKey);
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

    public static class Params {
        Context context;
        String siteId;
        String  userId;
        String url;
        String xapiKey;
        String authToken;

        public static class Builder {
            GetRecommendationGenreAsyncTask.Params params;

            public Builder() {
                params = new GetRecommendationGenreAsyncTask.Params();
            }

            public GetRecommendationGenreAsyncTask.Params.Builder context(Context context) {
                params.context = context;
                return this;
            }

            public GetRecommendationGenreAsyncTask.Params.Builder siteId(String siteName) {
                params.siteId = siteName;
                return this;
            }

            public GetRecommendationGenreAsyncTask.Params.Builder authToken(String authToken) {
                params.authToken = authToken;
                return this;
            }

            public GetRecommendationGenreAsyncTask.Params.Builder userId(String userId) {
                params.userId = userId;
                return this;
            }

            public GetRecommendationGenreAsyncTask.Params.Builder url(String baseurl) {
                params.url = baseurl;
                return this;
            }

            public GetRecommendationGenreAsyncTask.Params.Builder xapiKey(String apiKey) {
                params.xapiKey = apiKey;
                return this;
            }

            public GetRecommendationGenreAsyncTask.Params build() {
                return params;
            }
        }
    }




}
