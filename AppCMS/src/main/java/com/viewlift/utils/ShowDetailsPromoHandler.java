package com.viewlift.utils;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.CustomVideoPlayerView;
import com.viewlift.views.customviews.VideoPlayerView;
import com.viewlift.views.listener.TrailerCompletedCallback;

import rx.functions.Action1;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class ShowDetailsPromoHandler {
    private static ShowDetailsPromoHandler showDetailsPromoHandler;

    public static synchronized ShowDetailsPromoHandler getInstance() {
        if (showDetailsPromoHandler == null)
            showDetailsPromoHandler = new ShowDetailsPromoHandler();
        return showDetailsPromoHandler;
    }

    private ShowDetailsPromoHandler() {

    }

    static Handler trailerHold;

    String trailerId = null;
    String promoID = null;
    Boolean trailerAvalible = false;
    AppCMSPresenter appCMSPresenter;
    ImageView thumbnailImage = null;
    Module moduleAPIPub = null;

    public void openTrailer(View view, ImageView trailerImage, String episodeTrailerId, String episodePromoID, Boolean isLiveStream, Module module, AppCMSPresenter appCMSPresenter, ContentDatum data) {
        this.trailerId = episodeTrailerId;
        this.appCMSPresenter = appCMSPresenter;
        this.promoID = episodePromoID;
        trailerAvalible = true;
        thumbnailImage = trailerImage;
        moduleAPIPub = module;
        ((CustomVideoPlayerView) view).setPreviousNextVisibility(false);
        if (trailerId == null || trailerId.isEmpty()) {
            trailerAvalible = false;
        }
        if ((trailerId == null && promoID != null)) {
            trailerId = promoID;
            promoID = null;
            appCMSPresenter.setEpisodePromoID(null);
        }
        if (trailerId != null || promoID != null) {
            trailerHold = new Handler();
            if (appCMSPresenter.getDefaultTrailerPlay()) {
                trailerHold.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (appCMSPresenter.getDefaultTrailerPlay()) {
                            if (isLiveStream) {
                                //  trailerId = promoID;
                                promoID = null;
                                // trailerAvalible=false;
                                appCMSPresenter.setEpisodePromoID(null);
                            }
                            if (trailerId != null) {
                                ((CustomVideoPlayerView) view).setUseAdUrl(false);
                                if (isLiveStream || (!trailerAvalible)) {
                                    ((CustomVideoPlayerView) view).setUseAdUrl(true);
                                }
                                ((CustomVideoPlayerView) view).setVideoUri(trailerId, appCMSPresenter.getLocalisedStrings().getLoadingVideoText(), true, true, data);
                                appCMSPresenter.setTrailerPlayerView(((CustomVideoPlayerView) view));
                                ((CustomVideoPlayerView) view).disableTopBar();
                                ((CustomVideoPlayerView) view).releasePreviousAdsPlayer();
                                ((CustomVideoPlayerView) view).disableController();
                                ((CustomVideoPlayerView) view).setUseController(false);
                                ((CustomVideoPlayerView) view).setEpisodePlay(false);
                                ((CustomVideoPlayerView) view).onDestroyNotification();
                            }
                        }
                    }
                }, 1000);
            }

            ((CustomVideoPlayerView) view).setTrailerCompletedCallback(new TrailerCompletedCallback() {
                @Override
                public void videoCompleted() {
                    if ((appCMSPresenter.getEpisodePromoID() != null) && (!appCMSPresenter.getEpisodePromoID().isEmpty()) && (!isLiveStream)) {
                        ((CustomVideoPlayerView) view).setUseAdUrl(true);
                        ((CustomVideoPlayerView) view).setVideoUri(promoID, appCMSPresenter.getLocalisedStrings().getLoadingVideoText(), true, true, data);
                        appCMSPresenter.setTrailerPlayerView(((CustomVideoPlayerView) view));
                        appCMSPresenter.setEpisodePromoID(null);
                        view.setVisibility(VISIBLE);
                        if (thumbnailImage != null)
                            thumbnailImage.setVisibility(INVISIBLE);
                        ((CustomVideoPlayerView) view).disableController();
                        ((CustomVideoPlayerView) view).setUseController(false);
                        appCMSPresenter.dismissPopupWindowPlayer(true);
                    } else if (((appCMSPresenter.getEpisodePromoID() == null) || (appCMSPresenter.getEpisodePromoID().isEmpty())) && appCMSPresenter.getPlayshareControl()) {
                        Handler episode = new Handler();
                        episode.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                playVideo((CustomVideoPlayerView) view, trailerAvalible, episodePromoID);
                            }
                        }, 1000);
                    } else {
                        if (!appCMSPresenter.getIsMiniPlayerPlaying())
                            appCMSPresenter.showPopupWindowPlayer(false);
                        else {
                            appCMSPresenter.showPopupWindowPlayer(true);
                            Handler miniPlayerHandler = new Handler();
                            miniPlayerHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    appCMSPresenter.showPopupWindowPlayer(true);
                                }
                            }, 1500);
                        }
                        view.setVisibility(INVISIBLE);
                        if (thumbnailImage != null)
                            thumbnailImage.setVisibility(VISIBLE);
                        //traierPlayComplete = true;
                        trailerHold.removeCallbacksAndMessages(null);
                    }
                }

                @Override
                public void videoStarted() {
                    view.setVisibility(VISIBLE);
                    if (thumbnailImage != null)
                        thumbnailImage.setVisibility(INVISIBLE);
                    ((CustomVideoPlayerView) view).disableController();
                    ((CustomVideoPlayerView) view).setUseController(false);
                    appCMSPresenter.dismissPopupWindowPlayer(true);
                }
            });
        }
    }


    public void playVideoIfPromoTrailerUnavailable(View view, ImageView trailerImage, Module module, AppCMSPresenter appCMSPresenter, ContentDatum data) {
        this.appCMSPresenter = appCMSPresenter;
        thumbnailImage = trailerImage;
        moduleAPIPub = module;
        data.setModuleApi(moduleAPIPub);
        ((CustomVideoPlayerView) view).setVideoContentData(data);
        ((CustomVideoPlayerView) view).setVideoUri(data.getGist().getId(), appCMSPresenter.getLocalisedStrings().getLoadingVideoText(), false, false, data);
        ((CustomVideoPlayerView) view).setPreviousNextVisibility(false);
        appCMSPresenter.setTrailerPlayerView(((CustomVideoPlayerView) view));
        ((CustomVideoPlayerView) view).disableTopBar();
        ((CustomVideoPlayerView) view).releasePreviousAdsPlayer();
        ((CustomVideoPlayerView) view).onDestroyNotification();

        Player.EventListener listener = new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == ExoPlayer.STATE_READY) {
                    view.setVisibility(VISIBLE);
                    if (trailerImage != null)
                        trailerImage.setVisibility(INVISIBLE);
                    ((CustomVideoPlayerView) view).enableController();
                    ((CustomVideoPlayerView) view).setUseController(true);
                    ((CustomVideoPlayerView) view).setEpisodePlay(true);
                    appCMSPresenter.dismissPopupWindowPlayer(true);
                    ((CustomVideoPlayerView) view).getPlayer().removeListener(this);
                }
            }
        };
        ((CustomVideoPlayerView) view).getPlayer().addListener(listener);
    }

    public void playVideo(CustomVideoPlayerView view, Boolean trailerAvalible, String episodePromoID) {
        ContentDatum data = appCMSPresenter.getshowdetailsContenData();
        if (data != null && data.getGist() != null) {
            if (moduleAPIPub != null)
                data.setModuleApi(moduleAPIPub);
            appCMSPresenter.setShowDetailsGist(data.getGist());
            appCMSPresenter.setPlayshareControl(true);
        }
        appCMSPresenter.setEpisodeId(null);
        appCMSPresenter.setEpisodeTrailerID(null);
        appCMSPresenter.setEpisodePromoID(null);
        if (data != null) {
            if (thumbnailImage != null && thumbnailImage.getVisibility() == VISIBLE) {
                appCMSPresenter.getTrailerPlayerView().setVisibility(VISIBLE);
                thumbnailImage.setVisibility(INVISIBLE);
            }
            appCMSPresenter.dismissPopupWindowPlayer(true);
            if (moduleAPIPub != null) {
                data.setModuleApi(moduleAPIPub);
                appCMSPresenter.getTrailerPlayerView().setVideoContentData(data);
            }
            ((CustomVideoPlayerView) view).setUseAdUrl(false);
            if ((trailerAvalible) && ((episodePromoID == null) || (episodePromoID.isEmpty()))) {
                ((CustomVideoPlayerView) view).setUseAdUrl(true);
            }
            appCMSPresenter.getTrailerPlayerView().setVideoUri(data.getGist().getId(),
                    appCMSPresenter.getLocalisedStrings().getLoadingVideoText(), false, false, data);
            appCMSPresenter.getTrailerPlayerView().releasePreviousAdsPlayer();
            appCMSPresenter.getTrailerPlayerView().enableController();
            appCMSPresenter.getTrailerPlayerView().setUseController(true);
            appCMSPresenter.getTrailerPlayerView().setEpisodePlay(true);

        }

    }

    public void setTrailerPromoAutoPlay(AppCMSPresenter appCMSPresenter, ContentDatum data, CustomVideoPlayerView customVideoPlayerView) {
        this.appCMSPresenter = appCMSPresenter;
        appCMSPresenter.setshowdetailsClickPostionDate(data);
        appCMSPresenter.refreshVideoData(data.getGist().getId(), contentDatum -> {
            {
                if (contentDatum != null) {
                    try {
                        String episodeTrailerID = null;
                        String episodePromoID = null;
                        if (contentDatum.getContentDetails() != null && contentDatum.getContentDetails().getTrailers() != null &&
                                contentDatum.getContentDetails().getTrailers().size() > 0 &&
                                contentDatum.getContentDetails().getTrailers().get(0).getId() != null) {
                            appCMSPresenter.setEpisodeTrailerID(contentDatum.getContentDetails().getTrailers().get(0).getId());
                            episodeTrailerID = contentDatum.getContentDetails().getTrailers().get(0).getId();
                        }
                        if (contentDatum.getContentDetails() != null && contentDatum.getContentDetails().getPromos() != null &&
                                contentDatum.getContentDetails().getPromos().size() > 0 &&
                                contentDatum.getContentDetails().getPromos().get(0).getId() != null) {
                            appCMSPresenter.setEpisodePromoID(contentDatum.getContentDetails().getPromos().get(0).getId());
                            episodePromoID = contentDatum.getContentDetails().getPromos().get(0).getId();
                        }
                        boolean isLiveStream = data.getGist() != null && data.getGist().isLiveStream();
                        if (((episodeTrailerID != null && !episodeTrailerID.isEmpty())) && isLiveStream && episodePromoID == null) {
                            playVideo(customVideoPlayerView, true, episodePromoID);
                        } else if ((episodeTrailerID != null && !episodeTrailerID.isEmpty()) || ((episodePromoID != null && !episodePromoID.isEmpty()))) {
                            openTrailer(customVideoPlayerView, null, episodeTrailerID, episodePromoID, isLiveStream, null, appCMSPresenter, data);
                        } else {
                            customVideoPlayerView.setVideoUri(data.getGist().getId(),
                                    appCMSPresenter.getLocalisedStrings().getLoadingVideoText(), false, false, data);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, null, true, false, null);
    }
}
