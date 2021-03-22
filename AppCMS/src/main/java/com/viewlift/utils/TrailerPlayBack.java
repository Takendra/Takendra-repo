package com.viewlift.utils;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.CustomVideoPlayerView;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.listener.TrailerCompletedCallback;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class TrailerPlayBack {
    private static TrailerPlayBack trailerPlayBack;
    public Boolean isTrailerPlaying = false;

    public static synchronized TrailerPlayBack trailerPlayBack() {
        if (trailerPlayBack == null)
            trailerPlayBack = new TrailerPlayBack();
        return trailerPlayBack;
    }

    private TrailerPlayBack() {

    }

    Handler trailerHold;
    String trailerID = null;
    String promoID = null;
    Boolean isLiveStream = false;

    public void openTrailer(CustomVideoPlayerView view, AppCMSPresenter appCMSPresenter, ContentDatum contentDatum, String videoId) {
        if (contentDatum != null && contentDatum.getContentDetails() != null) {
            if (contentDatum.getContentDetails().getTrailers() != null &&
                    contentDatum.getContentDetails().getTrailers().size() > 0 &&
                    contentDatum.getContentDetails().getTrailers().get(0).getId() != null) {
                trailerID = contentDatum.getContentDetails().getTrailers().get(0).getId();
            }
            if (contentDatum.getContentDetails().getPromos() != null &&
                    contentDatum.getContentDetails().getPromos().size() > 0 &&
                    contentDatum.getContentDetails().getPromos().get(0).getId() != null) {
                promoID = contentDatum.getContentDetails().getPromos().get(0).getId();
            }
        }

        isLiveStream = contentDatum != null && contentDatum.getGist() != null && contentDatum.getGist().isLiveStream();

        isTrailerPlaying = true;
        if (((trailerID != null || promoID != null) && !isLiveStream) || (promoID != null && isLiveStream)) {
            if (isLiveStream) {
                trailerID = promoID;
                promoID = null;
                ((CustomVideoPlayerView) view).setUseAdUrl(true);
            }
            trailerHold = new Handler();
            trailerHold.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (trailerID != null) {
//                                if(!isLiveStream)
                        //    ((CustomVideoPlayerView) view).setUseAdUrl(false);
                        ((CustomVideoPlayerView) view).setVideoUri(trailerID, appCMSPresenter.getLocalisedStrings().getLoadingVideoText(), true, true, contentDatum);
                        ((CustomVideoPlayerView) view).releasePreviousAdsPlayer();
//                                ((CustomVideoPlayerView) view).setUseController(true);
//                                ((CustomVideoPlayerView) view).enableController();
//                                ((CustomVideoPlayerView) view).setEpisodePlay(false);
                        //    appCMSPresenter.setTrailerPlayerView(((CustomVideoPlayerView) view));
                        //  ((CustomVideoPlayerView) view).disableTopBar();
                        //((CustomVideoPlayerView) view).releasePreviousAdsPlayer();
                        ((CustomVideoPlayerView) view).disableController();
                        ((CustomVideoPlayerView) view).setUseController(false);
                        ((CustomVideoPlayerView) view).setEpisodePlay(false);
                        ((CustomVideoPlayerView) view).onDestroyNotification();
                    }
                }
            }, 20);
        } else {
            isTrailerPlaying = false;
            ((CustomVideoPlayerView) view).setUseAdUrl(true);
            view.setVideoUri(videoId, appCMSPresenter.getLocalisedStrings().getLoadingVideoText(), false, false, contentDatum);
            ((CustomVideoPlayerView) view).releasePreviousAdsPlayer();
//            ((CustomVideoPlayerView) view).setUseController(true);
//            ((CustomVideoPlayerView) view).enableController();
//            ((CustomVideoPlayerView) view).setEpisodePlay(false);
        }

        ((CustomVideoPlayerView) view).setTrailerCompletedCallback(new TrailerCompletedCallback() {
            @Override
            public void videoCompleted() {
                isTrailerPlaying = false;
                if ((promoID != null)
                        && (!promoID.isEmpty())
                        && !isLiveStream) {
                    ((CustomVideoPlayerView) view).setVideoUri(promoID, appCMSPresenter.getLocalisedStrings().getLoadingVideoText(), true, true, contentDatum);
                    appCMSPresenter.setTrailerPlayerView(((CustomVideoPlayerView) view));
                    // appCMSPresenter.setEpisodePromoID(null);
                    view.setVisibility(VISIBLE);
                    ((CustomVideoPlayerView) view).disableController();
                    ((CustomVideoPlayerView) view).setUseController(false);
                    //  appCMSPresenter.dismissPopupWindowPlayer(true);
                } else if (isLiveStream) {
                    ((CustomVideoPlayerView) view).setUseAdUrl(false);
                    Handler episode = new Handler();
                    episode.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((CustomVideoPlayerView) view).setUseAdUrl(false);
                            ((CustomVideoPlayerView) view).setVideoUri(videoId, appCMSPresenter.getLocalisedStrings().getLoadingVideoText(), false, false, contentDatum);
                            ((CustomVideoPlayerView) view).releasePreviousAdsPlayer();
//                                ((CustomVideoPlayerView) view).setUseController(true);
//                                ((CustomVideoPlayerView) view).enableController();
//                                ((CustomVideoPlayerView) view).setEpisodePlay(false);
                        }
                    }, 1000);
                    //   ((CustomVideoPlayerView) view).setVideoUri(videoId, appCMSPresenter.getLocalisedStrings().getLoadingVideoText(), false, false);
                    //  appCMSPresenter.setTrailerPlayerView(((CustomVideoPlayerView) view));
                    // appCMSPresenter.setEpisodePromoID(null);
                    //   view.setVisibility(VISIBLE);
                    // ((CustomVideoPlayerView) view).enableController();
                    // ((CustomVideoPlayerView) view).setUseController(true);
                    //  appCMSPresenter.dismissPopupWindowPlayer(true);
                }
            }

            @Override
            public void videoStarted() {
                view.setVisibility(VISIBLE);
//                    ((CustomVideoPlayerView) view).disableController();
//                    ((CustomVideoPlayerView) view).setUseController(false);
//                    appCMSPresenter.dismissPopupWindowPlayer(true);
            }
        });
    }
}
