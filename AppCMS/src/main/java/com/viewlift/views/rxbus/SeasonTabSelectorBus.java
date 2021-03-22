package com.viewlift.views.rxbus;


import com.viewlift.models.data.appcms.api.ContentDatum;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.AsyncSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;


/**
 * Created by wishy.gupta on 03-04-2018.
 */

public class SeasonTabSelectorBus {
    private static SeasonTabSelectorBus instance;

    private PublishSubject<List<ContentDatum>> subject = PublishSubject.create();
    private PublishSubject<Integer> timeCarousel = PublishSubject.create();
    private PublishSubject<CustomSubscriber> customSubject = PublishSubject.create();
    private AsyncSubject<String> currentPlayingVideoId = AsyncSubject.create();

    public static SeasonTabSelectorBus instanceOf() {
        if (instance == null) {
            instance = new SeasonTabSelectorBus();
        }
        return instance;
    }

    public void setTab(List<ContentDatum> adapterData) {
        try {
            subject.onNext(adapterData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Observable<List<ContentDatum>> getSelectedTab() {
        return subject;
    }

    public void setTimeCarousel(int position) {
        try {
            timeCarousel.onNext(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Observable<Integer> getTimeCarousel() {
        return timeCarousel;
    }

    public void setCurrentPlayingVideoId(String videoId) {
        try {
            currentPlayingVideoId.onNext(videoId);
            currentPlayingVideoId.onComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Observable<String> getCurrentPlayingVideoIdl() {
        return currentPlayingVideoId;
    }

    private PublishSubject<Boolean> mFilterBusSubject = PublishSubject.create();

    public void setClearFilter(Boolean doesClearFilter) {
        try {
            mFilterBusSubject.onNext(doesClearFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Observable<Boolean> getClearFilter() {
        return mFilterBusSubject;
    }

    public Observable<CustomSubscriber> getCustomSubscriber() {
        return customSubject;
    }

    public void setCustomSubscriber(CustomSubscriber customSubscriber) {
        try {
            customSubject.onNext(customSubscriber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
