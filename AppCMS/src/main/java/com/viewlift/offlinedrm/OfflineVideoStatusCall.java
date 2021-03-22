package com.viewlift.offlinedrm;

import com.google.android.exoplayer2.offline.Download;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.customviews.constraintviews.ConstraintViewCreator;

import rx.Observable;
import rx.functions.Action1;

public class OfflineVideoStatusCall {
    public void call(String videoId, AppCMSPresenter appCMSPresenter, final Action1<ConstraintViewCreator.OfflineVideoStatusHandler> readyAction1, String userId){
        Download download = appCMSPresenter.getAppCMSApplication().getOfflineDRMManager().getDownloadTracker().getDowloadedVideoObject(videoId);

        if(download==null){
            //Video Not Downloaded and Can be Downloaded
            Observable.just((ConstraintViewCreator.OfflineVideoStatusHandler) null)
                    .onErrorResumeNext(throwable -> Observable.empty())
                    .subscribe(readyAction1);
            return;
        }

        ConstraintViewCreator.OfflineVideoStatusHandler offlineVideoStatusHandler = new ConstraintViewCreator.OfflineVideoStatusHandler();
        offlineVideoStatusHandler.setOfflineVideoDownloadStatus(download.state);
        offlineVideoStatusHandler.setVideoId(videoId);
        offlineVideoStatusHandler.setDownloadObject(download);

        Observable.just(offlineVideoStatusHandler)
                .onErrorResumeNext(throwable -> Observable.empty())
                .subscribe(readyAction1);
    }
}
